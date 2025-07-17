package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import com.hexaware.amazecare.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Appointment bookAppointment(Long doctorId, String patientEmail, LocalDateTime dateTime, String notes) {
        User patientUser = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient user not found"));

        Patient patient = patientRepository.findByUser(patientUser)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setStatus(AppointmentStatus.REQUESTED);
        appointment.setNotes(notes);

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsForPatient(String patientEmail) {
        User patientUser = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient user not found"));

        Patient patient = patientRepository.findByUser(patientUser)
                .orElseThrow(() -> new RuntimeException("Patient profile not found"));

        return appointmentRepository.findByPatient(patient);
    }

    @Override
    public List<Appointment> getAppointmentsForDoctor(String doctorEmail) {
        User doctorUser = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("Doctor user not found"));

        Doctor doctor = doctorRepository.findByUser(doctorUser)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        return appointmentRepository.findByDoctor(doctor);
    }

    @Override
    public Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Override
    public boolean cancelAppointment(Long appointmentId, String patientEmail) {
        User patientUser = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient user not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getPatient().getUser().getEmail().equals(patientEmail)) {
            throw new RuntimeException("Unauthorized to cancel this appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        return true;
    }
}
