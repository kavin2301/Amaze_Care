package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.MedicalRecord;
import com.hexaware.amazecare.repository.AppointmentRepository;
import com.hexaware.amazecare.repository.MedicalRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MedicalRecordService {
    @Autowired
    private MedicalRecordRepository recordRepo;
    @Autowired
    private AppointmentRepository appointmentRepo;

    public MedicalRecord addMedicalRecord(int appointmentId, String diagnosis, String physicalExam, String testRecommended) {
        Appointment appt = appointmentRepo.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appt);
        record.setDiagnosis(diagnosis);
        record.setPhysicalExam(physicalExam);
        record.setTestRecommended(testRecommended);
        record.setCreatedAt(LocalDateTime.now());
        return recordRepo.save(record);
    }
}