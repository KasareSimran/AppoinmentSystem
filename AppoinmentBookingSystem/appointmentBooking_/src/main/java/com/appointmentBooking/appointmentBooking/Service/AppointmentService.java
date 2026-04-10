package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.ApiResponse;
import com.appointmentBooking.appointmentBooking.DTO.AppointmentResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    ApiResponse bookAppointment(String phone, Long slotId);

    String cancelAppointment(Long appointmentId);

//    List<AppointmentResponse> getMyAppointments(String phone);

    //with pagination

    Page<AppointmentResponse> getMyAppointments(String phone, int page, int size);

    List<AppointmentResponse> getAppointmentsByDate(LocalDate date);
}
