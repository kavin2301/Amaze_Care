package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.Prescription;
import com.hexaware.amazecare.repository.AppointmentRepository;
import com.hexaware.amazecare.repository.PrescriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepo;

    @Mock
    private AppointmentRepository appointmentRepo;

    @InjectMocks
    private PrescriptionService prescriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPrescribe_Success() {
        Appointment appt = new Appointment();
        when(appointmentRepo.findById(1)).thenReturn(Optional.of(appt));

        Prescription prescription = new Prescription();
        prescription.setMedicineName("Paracetamol");

        when(prescriptionRepo.save(any(Prescription.class))).thenReturn(prescription);

        Prescription result = prescriptionService.prescribe(1, "Paracetamol", "1-0-1", "AF");
        assertEquals("Paracetamol", result.getMedicineName());
    }

    @Test
    void testPrescribe_AppointmentNotFound() {
        when(appointmentRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> prescriptionService.prescribe(1, "Amoxicillin", "0-1-0", "BF"));
    }
} 