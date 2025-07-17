package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.repository.PatientRepository;
import com.hexaware.amazecare.repository.UserRepository;
import com.hexaware.amazecare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerPatient(User user, Patient patientDetails) {
        if (patientRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Patient profile already exists for this user.");
        }

        patientDetails.setUser(user);
        patientRepository.save(patientDetails);

        System.out.println("Patient profile created for user ID: " + user.getId());
    }

    @Override
    public Patient getPatientProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user ID: " + user.getId()));
    }

    @Override
    public Patient updatePatientProfile(String email, Patient updatedPatient) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Patient existingPatient = patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user ID: " + user.getId()));

        existingPatient.setDob(updatedPatient.getDob());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setContactNumber(updatedPatient.getContactNumber());
        existingPatient.setMedicalHistory(updatedPatient.getMedicalHistory());

        return patientRepository.save(existingPatient);
    }
}
