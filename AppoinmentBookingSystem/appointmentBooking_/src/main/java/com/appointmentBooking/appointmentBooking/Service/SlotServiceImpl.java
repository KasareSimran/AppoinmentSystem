package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.SlotRequest;
import com.appointmentBooking.appointmentBooking.Entity.Slot;
import com.appointmentBooking.appointmentBooking.Enum.SlotStatus;
import com.appointmentBooking.appointmentBooking.Repository.SlotRepo;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
public class SlotServiceImpl implements SlotService{

    private SlotRepo slotRepo;

    public SlotServiceImpl (SlotRepo slotRepo){
        this.slotRepo=slotRepo;
    }




    @Override
    public Slot createSlot(SlotRequest request) {
        Slot slot = new Slot();
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setStatus(SlotStatus.AVAILABLE);


        return slotRepo.save(slot) ;
    }

    @Override
    public List<Slot> getAllSlots() {
        return slotRepo.findAll();
    }

    @Override
    public List<Slot> getAvailableSlots() {
        return slotRepo.findByStatus(SlotStatus.AVAILABLE);
    }
}
