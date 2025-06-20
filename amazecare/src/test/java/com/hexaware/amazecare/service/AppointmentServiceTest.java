package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {
    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepo;

    @Mock
    private DoctorRepository doctorRepo;

    @Mock
    private PatientRepository patientRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookAppointmentSuccess() {
        DoctorProfile doc = new DoctorProfile();
        PatientProfile pat = new PatientProfile();
        when(doctorRepo.findById(1)).thenReturn(Optional.of(doc));
        when(patientRepo.findById(2)).thenReturn(Optional.of(pat));

        Appointment mockApp = new Appointment();
        mockApp.setSymptoms("Cough");
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(mockApp);

        Appointment app = appointmentService.bookAppointment(2, 1, LocalDateTime.now(), "Cough");
        assertEquals("Cough", app.getSymptoms());
    }

    @Test
    void testBookAppointmentDoctorNotFound() {
        when(doctorRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> appointmentService.bookAppointment(2, 1, LocalDateTime.now(), "Headache"));
    }

    @Test
    void testBookAppointmentPatientNotFound() {
        DoctorProfile doc = new DoctorProfile();
        when(doctorRepo.findById(1)).thenReturn(Optional.of(doc));
        when(patientRepo.findById(2)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> appointmentService.bookAppointment(2, 1, LocalDateTime.now(), "Headache"));
    }
}