package com.appointmentBooking.appointmentBooking.Repository;

import com.appointmentBooking.appointmentBooking.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
    List<Appointment> findByUser_Phone(String phone);

    List<Appointment> findBySlot_StartTimeBetween(
            LocalDateTime start,
            LocalDateTime end
    );

}
