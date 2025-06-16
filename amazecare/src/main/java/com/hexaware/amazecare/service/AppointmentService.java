package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    public Appointment bookAppointment(int patientId, int doctorId, LocalDateTime appointmentDate, String symptoms) {
        PatientProfile patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        DoctorProfile doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setSymptoms(symptoms);
        appointment.setStatus(Status.SCHEDULED);

        return appointmentRepo.save(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentRepo.findByPatientPatientId(patientId);
    }
}
