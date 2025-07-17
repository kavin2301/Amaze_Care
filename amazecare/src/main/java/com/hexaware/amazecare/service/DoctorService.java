package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Doctor;
import com.hexaware.amazecare.entity.Patient;

import java.util.List;

public interface DoctorService {
    Doctor getDoctorProfile(String email);
    Doctor updateDoctorProfile(String email, Doctor updatedDoctor);
    List<Doctor> getAllDoctors();
    List<Doctor> searchDoctorsBySpecialty(String keyword);
    Patient updatePatientMedicalHistory(Long patientId, String history);
}
