package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository recordRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public MedicalRecord addMedicalRecord(int appointmentId, String diagnosis, String exam, String test) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDiagnosis(diagnosis);
        record.setPhysicalExam(exam);
        record.setTestRecommended(test);

        return recordRepo.save(record);
    }
}
