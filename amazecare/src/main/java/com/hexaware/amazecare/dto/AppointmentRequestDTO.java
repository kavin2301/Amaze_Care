package com.hexaware.amazecare.dto;

public class AppointmentRequestDTO {
    private int patientId;
    private int doctorId;
    private String symptoms;

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
}