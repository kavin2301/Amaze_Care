package com.hexaware.amazecare.dto;

public class PrescriptionRequestDTO {
    private int appointmentId;
    private String medicineName;
    private String dosagePattern;
    private String intakeTime;

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosagePattern() {
        return dosagePattern;
    }

    public void setDosagePattern(String dosagePattern) {
        this.dosagePattern = dosagePattern;
    }

    public String getIntakeTime() {
        return intakeTime;
    }

    public void setIntakeTime(String intakeTime) {
        this.intakeTime = intakeTime;
    }
}