package com.hexaware.amazecare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prescriptionId;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String medicineName;
    private String dosagePattern; // example: 1-0-1
    private String intakeTime;    // example: BF / AF
    
	public int getPrescriptionId() {
		return prescriptionId;
	}
	public void setPrescriptionId(int prescriptionId) {
		this.prescriptionId = prescriptionId;
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
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
