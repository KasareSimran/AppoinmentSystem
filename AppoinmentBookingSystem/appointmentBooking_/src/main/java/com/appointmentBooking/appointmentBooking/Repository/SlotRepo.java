package com.appointmentBooking.appointmentBooking.Repository;

import com.appointmentBooking.appointmentBooking.Entity.Slot;
import com.appointmentBooking.appointmentBooking.Enum.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SlotRepo extends JpaRepository<Slot,Long> {
    List<Slot> findByStatus(SlotStatus status);
}
