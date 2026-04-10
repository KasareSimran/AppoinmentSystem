package com.appointmentBooking.appointmentBooking.Controller;


import com.appointmentBooking.appointmentBooking.DTO.ApiResponse;
import com.appointmentBooking.appointmentBooking.DTO.AppointmentResponse;
import com.appointmentBooking.appointmentBooking.Service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService=appointmentService;
    }


    @PostMapping("/book/{slotId}")
    public ApiResponse book(Authentication auth, @PathVariable Long slotId){
        return appointmentService.bookAppointment(auth.getName(), slotId);
    }


    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id){
        return appointmentService.cancelAppointment(id);
    }


//    @GetMapping("/my")
//    public List<AppointmentResponse> getMyAppointments(Authentication auth) {
//        return appointmentService.getMyAppointments(auth.getName());
//    }

    //with pagination
    @GetMapping("/my")
    public Page<AppointmentResponse> getMyAppointments(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return appointmentService.getMyAppointments(auth.getName(), page, size);
    }


    //get by date appointmnet
    @GetMapping("/date")
    public List<AppointmentResponse> getByDate(@RequestParam String date) {

        LocalDate localDate = LocalDate.parse(date);
        return appointmentService.getAppointmentsByDate(localDate);
    }


}
