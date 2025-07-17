package com.hexaware.amazecare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexaware.amazecare.dto.*;
import com.hexaware.amazecare.entity.Role;
import com.hexaware.amazecare.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class,
    excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        com.hexaware.amazecare.security.JwtAuthFilter.class,
        com.hexaware.amazecare.security.JwtUtil.class
    })
)
@AutoConfigureMockMvc(addFilters = false) 
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setPassword("pass123");
        request.setRole(Role.PATIENT);

        Mockito.when(userService.registerUser(any(RegisterRequest.class)))
               .thenReturn("User registered");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered"));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("pass123");

        AuthResponse response = new AuthResponse("mockToken", "PATIENT");

        Mockito.when(userService.authenticateUser(any(LoginRequest.class)))
               .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"))
                .andExpect(jsonPath("$.role").value("PATIENT"));
    }
}
