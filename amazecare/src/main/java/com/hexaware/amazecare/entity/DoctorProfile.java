package com.hexaware.amazecare.entity;

import jakarta.persistence.*;

@Entity
public class DoctorProfile {

    @Id
    private Integer doctorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "doctor_id")
    private User user;

    private String specialty;
    private String qualification;
    private String designation;
    private int experienceYears;

    public Integer getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getSpecialty() {
        return specialty;
    }
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    public String getQualification() {
        return qualification;
    }
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public int getExperienceYears() {
        return experienceYears;
    }
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
}
