package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.entity.Doctor;
import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private String email;
    private Doctor sampleDoctor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        email = "doctor@example.com";

        // Set up security context to return test email
        when(authentication.getName()).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Sample doctor object
        User user = new User();
        user.setEmail(email);

        sampleDoctor = new Doctor(user, "Cardiology", 10, "MBBS, MD", "Consultant");
        sampleDoctor.setId(1L);
    }

    @Test
    void testGetDoctorProfile() {
        when(doctorService.getDoctorProfile(email)).thenReturn(sampleDoctor);

        ResponseEntity<Doctor> response = doctorController.getDoctorProfile();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleDoctor, response.getBody());
    }

    @Test
    void testUpdateDoctorProfile() {
        Doctor updatedDoctor = new Doctor(sampleDoctor.getUser(), "Neurology", 15, "MBBS, DM", "Senior Consultant");
        updatedDoctor.setId(1L);

        when(doctorService.updateDoctorProfile(email, updatedDoctor)).thenReturn(updatedDoctor);

        ResponseEntity<Doctor> response = doctorController.updateDoctorProfile(updatedDoctor);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Neurology", response.getBody().getSpecialty());
        assertEquals(15, response.getBody().getExperience());
    }

    @Test
    void testGetAllDoctors() {
        List<Doctor> doctors = Arrays.asList(sampleDoctor);
        when(doctorService.getAllDoctors()).thenReturn(doctors);

        ResponseEntity<List<Doctor>> response = doctorController.getAllDoctors();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Cardiology", response.getBody().get(0).getSpecialty());
    }

    @Test
    void testSearchDoctors() {
        String specialty = "Cardiology";
        List<Doctor> doctors = Arrays.asList(sampleDoctor);
        when(doctorService.searchDoctorsBySpecialty(specialty)).thenReturn(doctors);

        ResponseEntity<List<Doctor>> response = doctorController.searchDoctors(specialty);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Cardiology", response.getBody().get(0).getSpecialty());
    }

    @Test
    void testUpdateMedicalHistory() {
        Long patientId = 101L;
        String history = "No known allergies. Diabetic.";
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setMedicalHistory(history);
        patient.setDob(LocalDate.of(1990, 5, 20));
        patient.setGender("Male");
        patient.setContactNumber("1234567890");

        User user = new User();
        user.setEmail("patient@example.com");
        patient.setUser(user);

        when(doctorService.updatePatientMedicalHistory(patientId, history)).thenReturn(patient);

        ResponseEntity<Patient> response = doctorController.updateMedicalHistory(patientId, history);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(history, response.getBody().getMedicalHistory());
        assertEquals("patient@example.com", response.getBody().getUser().getEmail());
    }
}
