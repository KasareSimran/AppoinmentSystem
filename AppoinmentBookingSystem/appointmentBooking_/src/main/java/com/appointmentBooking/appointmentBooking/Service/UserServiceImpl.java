package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.ChangePasswordRequest;
import com.appointmentBooking.appointmentBooking.DTO.LoginRequest;
import com.appointmentBooking.appointmentBooking.DTO.RegisterRequest;
import com.appointmentBooking.appointmentBooking.Entity.User;
import com.appointmentBooking.appointmentBooking.Enum.Role;
import com.appointmentBooking.appointmentBooking.Repository.UserRepo;
import com.appointmentBooking.appointmentBooking.JWT.JwtProvider;
import com.appointmentBooking.appointmentBooking.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo,
                           JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }



    // ✅ REGISTER
    @Override
    public User register(RegisterRequest request) {

        if (userRepo.findByPhone(request.getPhone()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with this phone");
        }

        User user = new User();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(Role.valueOf(request.getRole()));
        user.setRole(Role.USER);


        return userRepo.save(user);
    }

    // ✅ LOGIN (UPDATED WITH JWT)
    @Override
    public String login(LoginRequest request) {

        User user = userRepo.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Invalid Password!!");
        }

        // 🔥 RETURN TOKEN INSTEAD OF MESSAGE
        return jwtProvider.generateToken(user.getPhone());
    }


    @Override
    public User getProfile(String phone) {

        return userRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public User updateProfile(String phone, RegisterRequest request) {

        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPhone().equals(request.getPhone()) &&
                userRepo.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone already in use");
        }

        user.setName(request.getName());
        user.setPhone(request.getPhone());

        return userRepo.save(user);
    }



    @Override
    public String changePassword(String phone, ChangePasswordRequest request) {

        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);

        return "Password updated successfully";
    }

    @Override
    public String deleteUser(String phone) {

        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepo.delete(user);

        return "User deleted successfully";
    }



}
