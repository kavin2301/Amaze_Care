package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {
    @Autowired
    private MedicalRecordRepository recordRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Transactional
    public MedicalRecord saveRecord(int appointmentId, String diagnosis, String exam, String tests) {
        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appt);
        record.setDiagnosis(diagnosis);
        record.setPhysicalExam(exam);
        record.setTestRecommended(tests);
        return recordRepo.save(record);
    }

    public List<MedicalRecord> getRecordsByAppointmentId(int appointmentId) {
        return recordRepo.findByAppointmentAppointmentId(appointmentId);
    }

}