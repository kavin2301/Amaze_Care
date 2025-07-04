package com.hexaware.amazecare.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class PatientProfile {

    @Id
    private Integer patientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_id")
    private User user;

    private LocalDate dateOfBirth;
    private String gender;

    public Integer getPatientId() {
        return patientId;
    }
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
}
