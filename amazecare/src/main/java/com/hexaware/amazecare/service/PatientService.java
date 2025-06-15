package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.PatientProfile;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.repository.PatientRepository;
import com.hexaware.amazecare.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepo;
    @Autowired
    private UserRepository userRepo;

    public PatientProfile createPatientProfile(int userId, LocalDate dob, String gender) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        PatientProfile profile = new PatientProfile();
        profile.setUser(user);
        profile.setDateOfBirth(dob);
        profile.setGender(gender);
        return patientRepo.save(profile);
    }

    public PatientProfile getPatientProfile(int patientId) {
        return patientRepo.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient not found"));
    }
}