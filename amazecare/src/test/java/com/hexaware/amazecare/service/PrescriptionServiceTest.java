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

class PrescriptionServiceTest {
    @InjectMocks
    private PrescriptionService prescriptionService;

    @Mock
    private PrescriptionRepository prescriptionRepo;

    @Mock
    private AppointmentRepository appointmentRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPrescription() {
        Appointment app = new Appointment();
        when(appointmentRepo.findById(1)).thenReturn(Optional.of(app));

        Prescription pMock = new Prescription();
        pMock.setMedicineName("Paracetamol");
        when(prescriptionRepo.save(any(Prescription.class))).thenReturn(pMock);

        Prescription p = prescriptionService.addPrescription(1, "Paracetamol", "1-0-1", "BF");
        assertEquals("Paracetamol", p.getMedicineName());
    }

    @Test
    void testAddPrescriptionAppointmentNotFound() {
        when(appointmentRepo.findById(123)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> prescriptionService.addPrescription(123, "Test", "1-1-1", "AF"));
    }
}
