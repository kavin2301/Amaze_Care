package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Transactional
    public Prescription addPrescription(int appointmentId, String medicineName, String dosagePattern, String intakeTime) {
        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        Prescription p = new Prescription();
        p.setAppointment(appt);
        p.setMedicineName(medicineName);
        p.setDosagePattern(dosagePattern);
        p.setIntakeTime(intakeTime);

        return prescriptionRepo.save(p);
    }

    public List<Prescription> getPrescriptionsByAppointmentId(int appointmentId) {
        return prescriptionRepo.findByAppointmentAppointmentId(appointmentId);
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepo.findAll();
    }
}
