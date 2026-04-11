package com.appointmentBooking.appointmentBooking.Controller;


import com.appointmentBooking.appointmentBooking.JWT.JwtAuthFilter;
import com.appointmentBooking.appointmentBooking.JWT.JwtProvider;
import com.appointmentBooking.appointmentBooking.Service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;


    @Test
    @WithMockUser(username = "testUser")
    void testBookAppointment() throws Exception {

        when(appointmentService.bookAppointment(any(), anyLong()))
                .thenReturn("Appointment Booked Successfully");

        mockMvc.perform(post("/appointments/book/1"))
                .andExpect(status().isOk());
    }
}
