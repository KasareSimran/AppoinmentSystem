package com.appointmentBooking.appointmentBooking.Controller;


import com.appointmentBooking.appointmentBooking.Entity.Slot;
import com.appointmentBooking.appointmentBooking.Service.SlotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlotService slotService;

    @Test
    void testGetAvailableSlots() throws Exception {

        when(slotService.getAvailableSlots()).thenReturn(List.of(new Slot()));

        mockMvc.perform(get("/slots/available"))
                .andExpect(status().isOk());

        verify(slotService, times(1)).getAvailableSlots();
    }
}

