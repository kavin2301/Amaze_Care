package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import org.springframework.transaction.annotation.Transactional;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Transactional
    public Appointment bookAppointment(int patientId, int doctorId, LocalDateTime dateTime, String symptoms) {
        DoctorProfile doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));
        PatientProfile patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(dateTime);
        appointment.setSymptoms(symptoms);
        appointment.setStatus(Status.SCHEDULED);
        return appointmentRepo.save(appointment);
    }

    @Transactional
    public void cancelAppointment(int appointmentId) {
        Appointment appt = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId));
        appt.setStatus(Status.CANCELLED);
        appointmentRepo.save(appt);
    }

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        return appointmentRepo.findByPatientPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        return appointmentRepo.findByDoctorDoctorId(doctorId);
    }

    public Appointment getAppointmentById(int appointmentId) {
        return appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId));
    }

    public List<Appointment> getAppointmentsByStatus(String status) {
        try {
            Status enumStatus = Status.valueOf(status.toUpperCase()); 
            return appointmentRepo.findByStatus(enumStatus);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Invalid status: " + status);
        }
    }
}
