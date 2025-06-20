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

class MedicalRecordServiceTest {
    @InjectMocks
    private MedicalRecordService recordService;

    @Mock
    private MedicalRecordRepository recordRepo;

    @Mock
    private AppointmentRepository appointmentRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRecord() {
        Appointment app = new Appointment();
        when(appointmentRepo.findById(1)).thenReturn(Optional.of(app));

        MedicalRecord rec = new MedicalRecord();
        rec.setDiagnosis("Flu");
        when(recordRepo.save(any(MedicalRecord.class))).thenReturn(rec);

        MedicalRecord saved = recordService.saveRecord(1, "Flu", "Normal", "X-ray");
        assertEquals("Flu", saved.getDiagnosis());
    }

    @Test
    void testSaveRecordAppointmentNotFound() {
        when(appointmentRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> recordService.saveRecord(99, "Cold", "Check", "Blood"));
    }
}
