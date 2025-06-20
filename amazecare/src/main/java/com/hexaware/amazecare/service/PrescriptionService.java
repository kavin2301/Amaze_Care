package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import jakarta.transaction.Transactional;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Transactional
    public Prescription addPrescription(int appointmentId, String name, String pattern, String time) {
        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        Prescription p = new Prescription();
        p.setAppointment(appt);
        p.setMedicineName(name);
        p.setDosagePattern(pattern);
        p.setIntakeTime(time);
        return prescriptionRepo.save(p);
    }

    public List<Prescription> getPrescriptionsByAppointmentId(int appointmentId) {
        return prescriptionRepo.findByAppointmentAppointmentId(appointmentId);
    }
}
