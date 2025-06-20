package com.hexaware.amazecare.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientProfile patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctor;

    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String symptoms;

    private LocalDateTime createdAt = LocalDateTime.now();

    public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public PatientProfile getPatient() {
        return patient;
    }
    public void setPatient(PatientProfile patient) {
        this.patient = patient;
    }
    public DoctorProfile getDoctor() {
        return doctor;
    }
    public void setDoctor(DoctorProfile doctor) {
        this.doctor = doctor;
    }
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public String getSymptoms() {
        return symptoms;
    }
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
