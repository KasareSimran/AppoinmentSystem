package com.appointmentBooking.appointmentBooking.Service;


import com.appointmentBooking.appointmentBooking.DTO.ApiResponse;
import com.appointmentBooking.appointmentBooking.DTO.AppointmentResponse;
import com.appointmentBooking.appointmentBooking.Entity.Appointment;
import com.appointmentBooking.appointmentBooking.Entity.Slot;
import com.appointmentBooking.appointmentBooking.Entity.User;
import com.appointmentBooking.appointmentBooking.Enum.AppointmentStatus;
import com.appointmentBooking.appointmentBooking.Enum.SlotStatus;
import com.appointmentBooking.appointmentBooking.Repository.AppointmentRepo;
import com.appointmentBooking.appointmentBooking.Repository.SlotRepo;
import com.appointmentBooking.appointmentBooking.Repository.UserRepo;
import com.appointmentBooking.appointmentBooking.exception.AppointmentNotFoundException;
import com.appointmentBooking.appointmentBooking.exception.SlotNotAvailableException;
import com.appointmentBooking.appointmentBooking.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepo appointmentRepo;
    private final SlotRepo slotRepo;
    private final UserRepo userRepo;


    public AppointmentServiceImpl(AppointmentRepo appointmentRepo,
                                  SlotRepo slotRepo,
                                  UserRepo userRepo)
    {
        this.appointmentRepo=appointmentRepo;
        this.slotRepo=slotRepo;
        this.userRepo=userRepo;
    }




    @Override
    public ApiResponse bookAppointment(String phone, Long slotId) {


        //get user
        User user=userRepo.findByPhone(phone)
                .orElseThrow(()->new UserNotFoundException("User not found"));


        //get slot
        Slot slot=slotRepo.findById(slotId)
                .orElseThrow(()->new SlotNotAvailableException("Slot is not available"));



        if (slot.getStartTime().isBefore(LocalDateTime.now())) {
            throw new SlotNotAvailableException("Cannot book past slot");
        }


        //prevent double booking
        if(slot.getStatus()!= SlotStatus.AVAILABLE ) {
            throw new SlotNotAvailableException("Slot is already booked");
        }

        //create appointment
        Appointment appointment= new Appointment();
        appointment.setUser(user);
        appointment.setSlot(slot);
        appointment.setStatus(AppointmentStatus.BOOKED);


        //update slot
        slot.setStatus(SlotStatus.BOOKED);

        slotRepo.save(slot);
        appointmentRepo.save(appointment);

        return new ApiResponse("Appointment booked successfully", true);

    }




    @Override
    public String cancelAppointment(Long appointmentId) {

        Appointment appointment=appointmentRepo.findById(appointmentId)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }


        appointment.setStatus(AppointmentStatus.CANCELLED);

        //make slot available again
        Slot slot=appointment.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);

        slotRepo.save(slot);
        appointmentRepo.save(appointment);

        return "Appointment cancel Successfully!!";
    }


//    @Override
//    public <AppointmentResponse> getMyAppointments(String phone) {
//
//        List<Appointment> appointments = appointmentRepo.findByUser_Phone(phone);
//
//        return appointments.stream().map(a -> {
//
//            String slotTime = a.getSlot().getStartTime().toLocalTime() +
//                    " - " +
//                    a.getSlot().getEndTime().toLocalTime();
//
//            return new AppointmentResponse(
//                    a.getId(),
//                    a.getUser().getName(),
//                    a.getUser().getPhone(),
//                    slotTime,
//                    a.getStatus().name()
//            );
//
//        }).toList();
//    }


    //with pagination

    @Override
    public Page<AppointmentResponse> getMyAppointments(String phone, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Appointment> appointmentPage =
                appointmentRepo.findByUser_Phone(phone, pageable);

        return appointmentPage.map(a -> {

            String slotTime = a.getSlot().getStartTime().toLocalTime()
                    + " - " +
                    a.getSlot().getEndTime().toLocalTime();

            return new AppointmentResponse(
                    a.getId(),
                    a.getUser().getName(),
                    a.getUser().getPhone(),
                    slotTime,
                    a.getStatus().name()
            );
        });
    }



    @Override
    public List<AppointmentResponse> getAppointmentsByDate(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Appointment> appointments =
                appointmentRepo.findBySlot_StartTimeBetween(start, end);

        return appointments.stream().map(a -> {

            String slotTime = a.getSlot().getStartTime().toLocalTime() +
                    " - " +
                    a.getSlot().getEndTime().toLocalTime();

            return new AppointmentResponse(
                    a.getId(),
                    a.getUser().getName(),
                    a.getUser().getPhone(),
                    slotTime,
                    a.getStatus().name()
            );

        }).toList();
    }




}
