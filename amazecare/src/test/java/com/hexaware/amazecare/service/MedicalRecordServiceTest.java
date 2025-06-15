package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.MedicalRecord;
import com.hexaware.amazecare.repository.AppointmentRepository;
import com.hexaware.amazecare.repository.MedicalRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository recordRepo;

    @Mock
    private AppointmentRepository appointmentRepo;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMedicalRecord_Success() {
        Appointment appt = new Appointment();
        when(appointmentRepo.findById(1)).thenReturn(Optional.of(appt));

        MedicalRecord record = new MedicalRecord();
        record.setDiagnosis("Flu");

        when(recordRepo.save(any(MedicalRecord.class))).thenReturn(record);

        MedicalRecord result = medicalRecordService.addMedicalRecord(1, "Flu", "Normal", "Blood Test");
        assertEquals("Flu", result.getDiagnosis());
    }

    @Test
    void testAddMedicalRecord_AppointmentNotFound() {
        when(appointmentRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> medicalRecordService.addMedicalRecord(1, "Cough", "Mild", "X-Ray"));
    }
}
