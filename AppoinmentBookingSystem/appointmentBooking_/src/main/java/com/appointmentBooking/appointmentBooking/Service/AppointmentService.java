package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.ApiResponse;
import com.appointmentBooking.appointmentBooking.DTO.AppointmentResponse;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    ApiResponse bookAppointment(String phone, Long slotId);

    String cancelAppointment(Long appointmentId);

    List<AppointmentResponse> getMyAppointments(String phone);

    List<AppointmentResponse> getAppointmentsByDate(LocalDate date);
}
