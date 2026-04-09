package com.appointmentBooking.appointmentBooking.Service;

public interface AppointmentService {

    String bookAppointment(String phone,Long slotId);

    String cancelAppointment(Long appointmentId);
}
