package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepo;
    @Mock
    private PatientRepository patientRepo;
    @Mock
    private DoctorRepository doctorRepo;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookAppointment_Success() {
        PatientProfile patient = new PatientProfile();
        patient.setPatientId(1);
        DoctorProfile doctor = new DoctorProfile();
        doctor.setDoctorId(1);

        when(patientRepo.findById(1)).thenReturn(Optional.of(patient));
        when(doctorRepo.findById(1)).thenReturn(Optional.of(doctor));

        Appointment saved = new Appointment();
        saved.setAppointmentId(100);
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(saved);

        Appointment result = appointmentService.bookAppointment(1, 1, LocalDateTime.now(), "Fever");

        assertEquals(100, result.getAppointmentId());
    }

    @Test
    void testBookAppointment_PatientNotFound() {
        when(patientRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> appointmentService.bookAppointment(1, 1, LocalDateTime.now(), "Fever"));
    }

    @Test
    void testBookAppointment_DoctorNotFound() {
        PatientProfile patient = new PatientProfile();
        when(patientRepo.findById(1)).thenReturn(Optional.of(patient));
        when(doctorRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> appointmentService.bookAppointment(1, 1, LocalDateTime.now(), "Fever"));
    }
}