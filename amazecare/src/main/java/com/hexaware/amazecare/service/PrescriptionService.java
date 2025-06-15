package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.Prescription;
import com.hexaware.amazecare.repository.AppointmentRepository;
import com.hexaware.amazecare.repository.PrescriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepo;
    @Autowired
    private AppointmentRepository appointmentRepo;

    public Prescription prescribe(int appointmentId, String medicine, String dosage, String intakeTime) {
        Appointment appt = appointmentRepo.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appt);
        prescription.setMedicineName(medicine);
        prescription.setDosagePattern(dosage);
        prescription.setIntakeTime(intakeTime);
        return prescriptionRepo.save(prescription);
    }
}
