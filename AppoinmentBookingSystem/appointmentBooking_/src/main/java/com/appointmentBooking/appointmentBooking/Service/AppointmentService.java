package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.AppointmentResponse;
import com.appointmentBooking.appointmentBooking.Entity.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    String bookAppointment(String phone,Long slotId);

    String cancelAppointment(Long appointmentId);

    List<AppointmentResponse> getMyAppointments(String phone);

    List<AppointmentResponse> getAppointmentsByDate(LocalDate date);
}
