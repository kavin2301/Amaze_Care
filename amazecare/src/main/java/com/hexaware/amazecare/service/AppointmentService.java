package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Appointment bookAppointment(int patientId, int doctorId, LocalDateTime dateTime, String symptoms) {
        PatientProfile patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        DoctorProfile doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setDoctor(doctor);
        appt.setAppointmentDate(dateTime);
        appt.setSymptoms(symptoms);
        appt.setStatus(Status.SCHEDULED);

        return appointmentRepo.save(appt);
    }

    @Transactional
    public void cancelAppointment(int appointmentId) {
        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        appt.setStatus(Status.CANCELLED);
        appointmentRepo.save(appt);
    }

    public Appointment getAppointmentById(int id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
    }

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        return appointmentRepo.findByPatientPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        return appointmentRepo.findByDoctorDoctorId(doctorId);
    }

    public List<Appointment> getAppointmentsByStatus(Status status) {
        return appointmentRepo.findByStatus(status);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }
}
