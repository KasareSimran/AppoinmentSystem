package com.appointmentBooking.appointmentBooking.Controller;


import com.appointmentBooking.appointmentBooking.DTO.LoginRequest;
import com.appointmentBooking.appointmentBooking.DTO.RegisterRequest;
import com.appointmentBooking.appointmentBooking.Entity.User;
import com.appointmentBooking.appointmentBooking.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;

    // Constructor Injection (best practice)
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register( @Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User Registered Successfully");
        response.put("status", true);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<String> login( @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok( userService.login(request));
    }
}
