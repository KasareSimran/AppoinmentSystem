package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.Entity.*;
import com.appointmentBooking.appointmentBooking.Enum.*;
import com.appointmentBooking.appointmentBooking.Repository.*;
import com.appointmentBooking.appointmentBooking.Service.AppointmentServiceImpl;
import com.appointmentBooking.appointmentBooking.exception.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private SlotRepo slotRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private User user;
    private Slot slot;

    @BeforeEach
    void setup() {
//        MockitoAnnotations.openMocks(this);

        user = new User(1L, "Simran", "1234567890", "pass", Role.USER);

        slot = new Slot(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                SlotStatus.AVAILABLE
        );
    }

    // ✅ SUCCESS CASE
    @Test
    void testBookAppointment_success() {

        when(userRepo.findByPhone("1234567890")).thenReturn(Optional.of(user));
        when(slotRepo.findById(1L)).thenReturn(Optional.of(slot));

        String result = appointmentService.bookAppointment("1234567890", 1L);

        assertEquals("Appointment Booked Successfully", result);
        assertEquals(SlotStatus.BOOKED, slot.getStatus());

        verify(appointmentRepo, times(1)).save(any(Appointment.class));
        verify(slotRepo, times(1)).save(slot);
    }

    // ❌ USER NOT FOUND
    @Test
    void testBookAppointment_userNotFound() {

        when(userRepo.findByPhone("123")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                appointmentService.bookAppointment("123", 1L));
    }

    // ❌ SLOT ALREADY BOOKED
    @Test
    void testBookAppointment_slotAlreadyBooked() {

        slot.setStatus(SlotStatus.BOOKED);

        when(userRepo.findByPhone("1234567890")).thenReturn(Optional.of(user));
        when(slotRepo.findById(1L)).thenReturn(Optional.of(slot));

        assertThrows(SlotNotAvailableException.class, () ->
                appointmentService.bookAppointment("1234567890", 1L));
    }

    // ❌ PAST SLOT
    @Test
    void testBookAppointment_pastSlot() {

        slot.setStartTime(LocalDateTime.now().minusHours(1));

        when(userRepo.findByPhone("1234567890")).thenReturn(Optional.of(user));
        when(slotRepo.findById(1L)).thenReturn(Optional.of(slot));

        assertThrows(SlotNotAvailableException.class, () ->
                appointmentService.bookAppointment("1234567890", 1L));
    }

    // ✅ CANCEL SUCCESS
    @Test
    void testCancelAppointment_success() {

        Appointment appointment = new Appointment(1L, user, slot, AppointmentStatus.BOOKED);

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        String result = appointmentService.cancelAppointment(1L);

        assertEquals("Appointment cancel Successfully!!", result);
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertEquals(SlotStatus.AVAILABLE, slot.getStatus());
    }

    // ❌ ALREADY CANCELLED
    @Test
    void testCancelAppointment_alreadyCancelled() {

        Appointment appointment = new Appointment(1L, user, slot, AppointmentStatus.CANCELLED);

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(RuntimeException.class, () ->
                appointmentService.cancelAppointment(1L));
    }
}
