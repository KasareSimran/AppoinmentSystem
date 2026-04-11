package com.appointmentBooking.appointmentBooking.Controller;


import com.appointmentBooking.appointmentBooking.DTO.LoginRequest;
import com.appointmentBooking.appointmentBooking.DTO.RegisterRequest;
import com.appointmentBooking.appointmentBooking.JWT.JwtProvider;
import com.appointmentBooking.appointmentBooking.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @MockBean
    private JwtProvider jwtProvider;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    AuthControllerTest() {
    }

    // ✅ REGISTER TEST
    @Test
    void testRegister() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "Simran", "1234567890", "pass", "USER"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User Registered Successfully"))
                .andExpect(jsonPath("$.status").value(true));

        verify(userService, times(1)).register(any());
    }

    // ✅ LOGIN TEST
    @Test
    void testLogin() throws Exception {

        LoginRequest request = new LoginRequest("1234567890", "pass");

        when(userService.login(any())).thenReturn("token123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(content().string("token123"));
    }
}