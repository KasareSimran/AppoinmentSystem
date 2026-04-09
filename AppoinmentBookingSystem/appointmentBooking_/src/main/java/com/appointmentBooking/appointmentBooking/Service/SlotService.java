package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.SlotRequest;
import com.appointmentBooking.appointmentBooking.Entity.Slot;

import java.util.List;

public interface SlotService {
    Slot createSlot(SlotRequest request);

    List<Slot> getAllSlots();


    List<Slot> getAvailableSlots();


}
