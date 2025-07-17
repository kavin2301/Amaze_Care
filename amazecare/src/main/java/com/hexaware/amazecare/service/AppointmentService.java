package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Appointment bookAppointment(Long doctorId, String patientEmail, LocalDateTime dateTime, String notes);
    List<Appointment> getAppointmentsForPatient(String patientEmail);
    List<Appointment> getAppointmentsForDoctor(String doctorEmail);
    Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus status);
    boolean cancelAppointment(Long appointmentId, String patientEmail);
}
