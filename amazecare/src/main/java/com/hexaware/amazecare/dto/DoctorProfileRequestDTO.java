// 📁 com.hexaware.amazecare.dto.DoctorProfileRequestDTO.java
package com.hexaware.amazecare.dto;

public class DoctorProfileRequestDTO {
    private int userId;
    private String specialty;
    private String qualification;
    private String designation;
    private int experienceYears;

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
}
