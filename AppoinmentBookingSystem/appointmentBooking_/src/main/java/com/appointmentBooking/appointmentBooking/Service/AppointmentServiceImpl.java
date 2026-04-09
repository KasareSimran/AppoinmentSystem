package com.appointmentBooking.appointmentBooking.Service;


import com.appointmentBooking.appointmentBooking.Entity.Appointment;
import com.appointmentBooking.appointmentBooking.Entity.Slot;
import com.appointmentBooking.appointmentBooking.Entity.User;
import com.appointmentBooking.appointmentBooking.Enum.AppointmentStatus;
import com.appointmentBooking.appointmentBooking.Enum.SlotStatus;
import com.appointmentBooking.appointmentBooking.Repository.AppointmentRepo;
import com.appointmentBooking.appointmentBooking.Repository.SlotRepo;
import com.appointmentBooking.appointmentBooking.Repository.UserRepo;
import org.springframework.stereotype.Service;

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
    public String bookAppointment(String phone, Long slotId) {


        //get user
        User user=userRepo.findByPhone(phone)
                .orElseThrow(()->new RuntimeException("User not found"));


        //get slot
        Slot slot=slotRepo.findById(slotId)
                .orElseThrow(()->new RuntimeException("Slot is not available"));

        //prevent double booking
        if(slot.getStatus()!= SlotStatus.AVAILABLE ) {
            throw new RuntimeException("Slot is already booked");
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

        return "Appointment Booked Successfully";
    }




    @Override
    public String cancelAppointment(Long appointmentId) {

        Appointment appointment=appointmentRepo.findById(appointmentId)
                .orElseThrow(()-> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);

        //make slot available again
        Slot slot=appointment.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);

        slotRepo.save(slot);
        appointmentRepo.save(appointment);

        return "Appointment cancel Successfully!!";
    }
}
