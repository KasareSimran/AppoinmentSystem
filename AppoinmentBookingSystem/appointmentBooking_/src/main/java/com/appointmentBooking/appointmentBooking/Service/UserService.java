package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.ChangePasswordRequest;
import com.appointmentBooking.appointmentBooking.DTO.LoginRequest;
import com.appointmentBooking.appointmentBooking.DTO.RegisterRequest;
import com.appointmentBooking.appointmentBooking.Entity.User;

public interface UserService {

    User register(RegisterRequest request);

    String login(LoginRequest request);

    User getProfile(String phone);

    User updateProfile(String phone, RegisterRequest request);

    String changePassword(String phone, ChangePasswordRequest request);

    String deleteUser(String phone);
}
