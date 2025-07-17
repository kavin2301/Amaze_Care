package com.hexaware.amazecare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;


import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = PatientController.class,
    excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        com.hexaware.amazecare.security.JwtAuthFilter.class,
        com.hexaware.amazecare.security.JwtUtil.class
    })
)
@AutoConfigureMockMvc(addFilters = false)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient testPatient;
    private String testEmail = "test@example.com";

    @BeforeEach
    public void setUp() {
        // Simulate an authenticated user
        SecurityContextHolder.getContext().setAuthentication(
            new TestingAuthenticationToken(testEmail, null)
        );

        User user = new User();
        user.setEmail(testEmail);

        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setUser(user);
        testPatient.setDob(LocalDate.of(1990, 1, 1));
        testPatient.setGender("Male");
        testPatient.setContactNumber("1234567890");
        testPatient.setMedicalHistory("None");
    }

    @Test
    public void testGetProfile() throws Exception {
        Mockito.when(patientService.getPatientProfile(testEmail)).thenReturn(testPatient);

        mockMvc.perform(get("/api/patient/profile"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gender").value("Male"))
            .andExpect(jsonPath("$.contactNumber").value("1234567890"))
            .andExpect(jsonPath("$.medicalHistory").value("None"));
    }

    @Test
    public void testUpdateProfile() throws Exception {
        Patient updatedPatient = new Patient();
        updatedPatient.setUser(testPatient.getUser());
        updatedPatient.setDob(LocalDate.of(1990, 1, 1));
        updatedPatient.setGender("Male");
        updatedPatient.setContactNumber("9999999999");
        updatedPatient.setMedicalHistory("Updated history");

       
        Mockito.when(patientService.updatePatientProfile(eq(testEmail), any(Patient.class)))
            .thenReturn(updatedPatient);

        mockMvc.perform(put("/api/patient/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPatient)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.contactNumber").value("9999999999"))
            .andExpect(jsonPath("$.medicalHistory").value("Updated history"));
    }
}
