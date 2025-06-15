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

    public Appointment bookAppointment(int patientId, int doctorId, LocalDateTime date, String symptoms) {
        PatientProfile patient = patientRepo.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        DoctorProfile doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setDoctor(doctor);
        appt.setAppointmentDate(date);
        appt.setSymptoms(symptoms);
        appt.setStatus(Status.SCHEDULED);
        appt.setCreatedAt(LocalDateTime.now());
        return appointmentRepo.save(appt);
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointmentRepo.findByDoctorDoctorId(doctorId);
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentRepo.findByPatientPatientId(patientId);
    }

    public Appointment cancelAppointment(int appointmentId) {
        Appointment appt = appointmentRepo.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        appt.setStatus(Status.CANCELLED);
        return appointmentRepo.save(appt);
    }
}