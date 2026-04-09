package com.appointmentBooking.appointmentBooking.Controller;


import com.appointmentBooking.appointmentBooking.Service.AppointmentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService=appointmentService;
    }


    @PostMapping("/book/{slotId}")
    public String book(Authentication auth, @PathVariable Long slotId){
        return appointmentService.bookAppointment(auth.getName(), slotId);
    }


    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id){
        return appointmentService.cancelAppointment(id);
    }





}
