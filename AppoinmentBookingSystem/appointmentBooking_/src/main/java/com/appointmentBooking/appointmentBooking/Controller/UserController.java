package com.appointmentBooking.appointmentBooking.Controller;

import com.appointmentBooking.appointmentBooking.DTO.ChangePasswordRequest;
import com.appointmentBooking.appointmentBooking.DTO.RegisterRequest;
import com.appointmentBooking.appointmentBooking.Entity.User;
import com.appointmentBooking.appointmentBooking.Repository.UserRepo;
import com.appointmentBooking.appointmentBooking.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        return ResponseEntity.ok(auth.getName());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.getProfile(auth.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            Authentication auth,
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                userService.updateProfile(auth.getName(), request)
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            Authentication auth,
            @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(
                userService.changePassword(auth.getName(), request)
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Authentication auth) {

        return ResponseEntity.ok(
                userService.deleteUser(auth.getName())
        );
    }
}
