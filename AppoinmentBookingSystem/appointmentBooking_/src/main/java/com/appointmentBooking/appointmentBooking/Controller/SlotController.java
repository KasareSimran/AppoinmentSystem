package com.appointmentBooking.appointmentBooking.Controller;


import com.appointmentBooking.appointmentBooking.DTO.SlotRequest;
import com.appointmentBooking.appointmentBooking.Entity.Slot;
import com.appointmentBooking.appointmentBooking.Service.SlotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slots")
public class SlotController {

    private SlotService slotService;

    public SlotController(SlotService slotService){
        this.slotService=slotService;
    }

    //post maapping-admin create slot
    @PostMapping
    public Slot createSlot(@RequestBody SlotRequest request){
        return slotService.createSlot(request);
    }


    //admin get all slot
    @GetMapping
    public List<Slot> getAllSlots(){
        return slotService.getAllSlots();
    }


    //user will get AVAILABLE slots

    @GetMapping("/available")
    public List<Slot> getAvailableSlots(){
        return slotService.getAvailableSlots();
    }

}
