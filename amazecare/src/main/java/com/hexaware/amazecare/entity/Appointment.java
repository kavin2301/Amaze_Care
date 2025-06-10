package com.hexaware.amazecare.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

public class Appointment {
    private int appointmentId;
    private PatientProfile patient;
    private DoctorProfile doctor;

    private LocalDateTime appointmentDate;
    private Status status;

    private String symptoms;
    private LocalDateTime createdAt = LocalDateTime.now();

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public PatientProfile getPatient() { return patient; }
    public void setPatient(PatientProfile patient) { this.patient = patient; }
    public DoctorProfile getDoctor() { return doctor; }
    public void setDoctor(DoctorProfile doctor) { this.doctor = doctor; }
    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
