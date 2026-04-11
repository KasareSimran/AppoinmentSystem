package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.LoginRequest;
import com.appointmentBooking.appointmentBooking.DTO.RegisterRequest;
import com.appointmentBooking.appointmentBooking.Entity.User;
import com.appointmentBooking.appointmentBooking.Enum.Role;
import com.appointmentBooking.appointmentBooking.JWT.JwtProvider;
import com.appointmentBooking.appointmentBooking.Repository.UserRepo;
import com.appointmentBooking.appointmentBooking.Service.UserServiceImpl;
import com.appointmentBooking.appointmentBooking.exception.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequest request;

    @BeforeEach
    void setup() {
//        MockitoAnnotations.openMocks(this);

        request = new RegisterRequest();
        request.setName("Simran");
        request.setPhone("1234567890");
        request.setPassword("pass");
        request.setRole("USER");
    }

    @Test
    void testRegister_success() {

        when(userRepo.findByPhone("1234567890")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepo.save(any(User.class))).thenReturn(new User(1L, "Simran", "1234567890", "encoded", Role.USER));


        User result = userService.register(request);

        assertEquals("Simran", result.getName());
    }

    @Test
    void testRegister_userExists() {

        when(userRepo.findByPhone("1234567890")).thenReturn(Optional.of(new User()));


        assertThrows(UserAlreadyExistsException.class, () ->
                userService.register(request));
    }

    @Test
    void testLogin_success() {

        LoginRequest login = new LoginRequest();
        login.setPhone("123");
        login.setPassword("pass");

        User user = new User(1L, "Simran", "123", "encoded", Role.USER);

        when(userRepo.findByPhone("123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);
        when(jwtProvider.generateToken("123")).thenReturn("token");

        String token = userService.login(login);

        assertEquals("token", token);
    }

    @Test
    void testLogin_invalidPassword() {

        LoginRequest login = new LoginRequest();
        login.setPhone("123");
        login.setPassword("wrong");

        User user = new User(1L, "Simran", "123", "encoded", Role.USER);

        when(userRepo.findByPhone("123")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                userService.login(login));
    }
}