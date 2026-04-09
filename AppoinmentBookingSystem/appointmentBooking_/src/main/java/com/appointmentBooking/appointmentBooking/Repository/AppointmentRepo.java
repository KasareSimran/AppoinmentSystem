package com.appointmentBooking.appointmentBooking.Repository;

import com.appointmentBooking.appointmentBooking.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
}
