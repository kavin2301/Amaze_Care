package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/profile")
    public ResponseEntity<Patient> getProfile() {
        String email = getLoggedInEmail();
        Patient patient = patientService.getPatientProfile(email);
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/profile")
    public ResponseEntity<Patient> updateProfile(@RequestBody Patient updatedPatient) {
        String email = getLoggedInEmail();
        Patient updated = patientService.updatePatientProfile(email, updatedPatient);
        return ResponseEntity.ok(updated);
    }

    private String getLoggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
