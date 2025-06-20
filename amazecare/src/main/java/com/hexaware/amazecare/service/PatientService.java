package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public PatientProfile createPatientProfile(int userId, LocalDate dob, String gender) {
        if (patientRepo.existsByPatientId(userId)) {
            throw new IllegalArgumentException("Patient profile already exists");
        }
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        PatientProfile profile = new PatientProfile();
        profile.setUser(user);
        profile.setDateOfBirth(dob);
        profile.setGender(gender);
        return patientRepo.save(profile);
    }

    public PatientProfile getPatientById(int id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
    }
}
