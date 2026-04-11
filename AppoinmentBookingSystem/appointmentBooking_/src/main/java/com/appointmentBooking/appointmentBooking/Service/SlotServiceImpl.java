package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.SlotRequest;
import com.appointmentBooking.appointmentBooking.Entity.Slot;
import com.appointmentBooking.appointmentBooking.Enum.SlotStatus;
import com.appointmentBooking.appointmentBooking.Repository.SlotRepo;
import com.appointmentBooking.appointmentBooking.exception.InvalidSlotTimeException;
import com.appointmentBooking.appointmentBooking.exception.SlotAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
public class SlotServiceImpl implements SlotService{
    private static final Logger logger = LoggerFactory.getLogger(SlotServiceImpl.class);


    private final SlotRepo slotRepo;

    public SlotServiceImpl (SlotRepo slotRepo){
        this.slotRepo=slotRepo;
    }




    @Override
    public Slot createSlot(SlotRequest request) {

        logger.info("Creating slot from {} to {}", request.getStartTime(), request.getEndTime());


        //validate time
        if(request.getEndTime().isBefore(request.getStartTime())){
            logger.warn("Invalid slot time: endTime before startTime");
            throw new InvalidSlotTimeException("End time must be after start time");
        }


        //prevent duplicate slot
        boolean exists = slotRepo.existsByStartTimeAndEndTime(
                request.getStartTime(),
                request.getEndTime()
        );

        if (exists) {
            logger.warn("Duplicate slot attempt: {} - {}", request.getStartTime(), request.getEndTime());
            throw new SlotAlreadyExistsException("Slot already exists for this time");
        }

        //create slot
        Slot slot = new Slot();
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setStatus(SlotStatus.AVAILABLE);

        Slot savedSlot = slotRepo.save(slot);

        logger.info("Slot created successfully with id: {}", savedSlot.getId());

        return savedSlot ;
    }

    @Override
    public List<Slot> getAllSlots() {
        return slotRepo.findAll();
    }

    @Override
    public List<Slot> getAvailableSlots() {
//        return slotRepo.findByStatus(SlotStatus.AVAILABLE);
        return slotRepo.findByStatusOrderByStartTimeAsc(SlotStatus.AVAILABLE);

    }
}
