package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
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

    public List<PatientProfile> getAllPatients() {
        return patientRepo.findAll();
    }

    public PatientProfile updatePatientProfile(int id, PatientProfile updated) {
        PatientProfile existing = getPatientById(id);
        existing.setGender(updated.getGender());
        existing.setDateOfBirth(updated.getDateOfBirth());
        return patientRepo.save(existing);
    }

    public void deletePatientProfile(int id) {
        if (!patientRepo.existsById(id)) {
            throw new EntityNotFoundException("Patient not found");
        }
        patientRepo.deleteById(id);
    }
}
