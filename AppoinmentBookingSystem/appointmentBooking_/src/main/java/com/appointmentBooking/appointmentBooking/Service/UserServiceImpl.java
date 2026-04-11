package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.ChangePasswordRequest;
import com.appointmentBooking.appointmentBooking.DTO.LoginRequest;
import com.appointmentBooking.appointmentBooking.DTO.RegisterRequest;
import com.appointmentBooking.appointmentBooking.Entity.User;
import com.appointmentBooking.appointmentBooking.Enum.Role;
import com.appointmentBooking.appointmentBooking.Repository.UserRepo;
import com.appointmentBooking.appointmentBooking.JWT.JwtProvider;
import com.appointmentBooking.appointmentBooking.exception.UserAlreadyExistsException;
import com.appointmentBooking.appointmentBooking.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

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

        logger.info("Register request for phone: {}", request.getPhone());

        if (userRepo.findByPhone(request.getPhone()).isPresent()) {
            logger.warn("User already exists with phone: {}", request.getPhone());
            throw new UserAlreadyExistsException("User already exists with this phone");
        }

        User user = new User();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

//        user.setRole(Role.USER);


        User savedUser = userRepo.save(user);

        logger.info("User registered successfully with id: {}", savedUser.getId());

        return savedUser;
    }

    // ✅ LOGIN (UPDATED WITH JWT)
    @Override
    public String login(LoginRequest request) {
        logger.info("Login attempt for phone: {}", request.getPhone());

        User user = userRepo.findByPhone(request.getPhone())
                .orElseThrow(() -> {
                    logger.error("User not found during login: {}", request.getPhone());
                    return new UserNotFoundException("User not found!");
                });



        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            logger.warn("Invalid password attempt for phone: {}", request.getPhone());
            throw new RuntimeException("Invalid Password!!");
        }

        // 🔥 RETURN TOKEN INSTEAD OF MESSAGE
//        return jwtProvider.generateToken(user.getPhone());
        String token = jwtProvider.generateToken(user.getPhone());

        logger.info("Login successful for phone: {}", request.getPhone());

        return token;
    }


    @Override
    public User getProfile(String phone) {

        return userRepo.findByPhone(phone)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", phone);
                    return new UserNotFoundException("User not found");
                });
    }


    @Override
    public User updateProfile(String phone, RegisterRequest request) {

        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", phone);
                    return new UserNotFoundException("User not found");
                });


        if (!user.getPhone().equals(request.getPhone()) &&
                userRepo.findByPhone(request.getPhone()).isPresent()) {

            logger.warn("Phone already in use: {}", request.getPhone());
            throw new RuntimeException("Phone already in use");
        }

        user.setName(request.getName());
        user.setPhone(request.getPhone());

        User updatedUser = userRepo.save(user);

        logger.info("Profile updated for user: {}", phone);

        return updatedUser;
    }



    @Override
    public String changePassword(String phone, ChangePasswordRequest request) {
        logger.info("Password change request for phone: {}", phone);


        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", phone);
                    return new UserNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            logger.warn("Invalid old password for phone: {}", phone);
            throw new RuntimeException("Old password incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);


        logger.info("Password updated successfully for phone: {}", phone);

        return "Password updated successfully";
    }

    @Override
    public String deleteUser(String phone) {

        logger.info("Delete request for user: {}", phone);

        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", phone);
                    return new UserNotFoundException("User not found");
                });


        userRepo.delete(user);

        logger.info("User deleted successfully: {}", phone);

        return "User deleted successfully";
    }



}
