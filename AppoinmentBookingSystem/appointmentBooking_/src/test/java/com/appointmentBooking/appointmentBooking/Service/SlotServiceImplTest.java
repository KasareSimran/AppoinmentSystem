package com.appointmentBooking.appointmentBooking.Service;

import com.appointmentBooking.appointmentBooking.DTO.SlotRequest;
import com.appointmentBooking.appointmentBooking.Entity.Slot;
import com.appointmentBooking.appointmentBooking.Enum.SlotStatus;
import com.appointmentBooking.appointmentBooking.Repository.SlotRepo;
import com.appointmentBooking.appointmentBooking.Service.SlotServiceImpl;
import com.appointmentBooking.appointmentBooking.exception.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlotServiceImplTest {

    @Mock
    private SlotRepo slotRepo;

    @InjectMocks
    private SlotServiceImpl slotService;

    private SlotRequest request;

    @BeforeEach
    void setup() {
//        MockitoAnnotations.openMocks(this);

        request = new SlotRequest();
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));
    }

    @Test
    void testCreateSlot_success() {

        when(slotRepo.existsByStartTimeAndEndTime(any(), any())).thenReturn(false);

        Slot savedSlot = new Slot(1L, request.getStartTime(), request.getEndTime(), SlotStatus.AVAILABLE);
        when(slotRepo.save(any(Slot.class))).thenReturn(savedSlot);

        Slot result = slotService.createSlot(request);

        assertNotNull(result);
        assertEquals(SlotStatus.AVAILABLE, result.getStatus());
    }

    @Test
    void testCreateSlot_invalidTime() {

        request.setEndTime(request.getStartTime().minusHours(1));

        assertThrows(InvalidSlotTimeException.class, () ->
                slotService.createSlot(request));
    }

    @Test
    void testCreateSlot_duplicate() {

        when(slotRepo.existsByStartTimeAndEndTime(any(), any())).thenReturn(true);

        assertThrows(SlotAlreadyExistsException.class, () ->
                slotService.createSlot(request));
    }
}
