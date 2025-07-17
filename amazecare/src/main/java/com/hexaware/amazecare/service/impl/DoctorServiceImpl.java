package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.entity.Doctor;
import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.repository.DoctorRepository;
import com.hexaware.amazecare.repository.PatientRepository;
import com.hexaware.amazecare.repository.UserRepository;
import com.hexaware.amazecare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private PatientRepository patientRepo;

    @Override
    public Doctor getDoctorProfile(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return doctorRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
    }

    @Override
    public Doctor updateDoctorProfile(String email, Doctor updatedDoctor) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor existing = doctorRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        existing.setSpecialty(updatedDoctor.getSpecialty());
        existing.setExperience(updatedDoctor.getExperience());
        existing.setQualification(updatedDoctor.getQualification());
        existing.setDesignation(updatedDoctor.getDesignation());

        return doctorRepo.save(existing);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    @Override
    public List<Doctor> searchDoctorsBySpecialty(String keyword) {
        return doctorRepo.findBySpecialtyContainingIgnoreCase(keyword);
    }
    
    @Override
    public Patient updatePatientMedicalHistory(Long patientId, String history) {
        Patient patient = patientRepo.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setMedicalHistory(history);
        return patientRepo.save(patient);
    }
}
