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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService{


    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

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
    @Transactional
    public String bookAppointment(String phone, Long slotId) {


        logger.info("Booking request started for user: {} and slot: {}", phone, slotId);

        //get user
        User user=userRepo.findByPhone(phone)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", phone);
                    return new UserNotFoundException("User not found");
                });


        //get slot
        Slot slot=slotRepo.findById(slotId)
                .orElseThrow(() -> {
                    logger.error("Slot not found: {}", slotId);
                    return new SlotNotAvailableException("Slot not found");
                });


        if (slot.getStartTime().isBefore(LocalDateTime.now())) {
            logger.warn("Attempt to book past slot: {}", slotId);
            throw new SlotNotAvailableException("Cannot book past slot");
        }


        //prevent double booking
        if(slot.getStatus()!= SlotStatus.AVAILABLE ) {
            logger.warn("Slot already booked: {}", slotId);
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

        logger.info("Appointment booked successfully for user: {} and slot: {}", phone, slotId);

        return "Appointment Booked Successfully";
    }




    @Override
    @Transactional
    public String cancelAppointment(Long appointmentId) {

        logger.info("Cancel request for appointment: {}", appointmentId);

        Appointment appointment=appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> {
                    logger.error("Appointment not found: {}", appointmentId);
                    return new AppointmentNotFoundException("Appointment not found");
                });



        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            logger.warn("Appointment already cancelled: {}", appointmentId);
            throw new RuntimeException("Appointment already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        //make slot available again
        Slot slot=appointment.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);

        slotRepo.save(slot);
        appointmentRepo.save(appointment);

        logger.info("Appointment cancelled successfully: {}", appointmentId);

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
