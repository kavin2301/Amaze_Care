package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.dto.PatientProfileRequestDTO;
import com.hexaware.amazecare.entity.PatientProfile;
import com.hexaware.amazecare.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/create")
    public ResponseEntity<?> createPatientProfile(@RequestBody PatientProfileRequestDTO dto) {
        try {
            LocalDate dob = LocalDate.parse(dto.getDob());
            PatientProfile profile = patientService.createPatientProfile(dto.getUserId(), dob, dto.getGender());
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input or user not found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable int id) {
        try {
            PatientProfile profile = patientService.getPatientById(id);
            return ResponseEntity.ok(profile);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Patient not found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePatientProfile(@PathVariable int id, @RequestBody PatientProfileRequestDTO dto) {
        try {
            LocalDate dob = LocalDate.parse(dto.getDob());
            PatientProfile updated = new PatientProfile();
            updated.setDateOfBirth(dob);
            updated.setGender(dto.getGender());
            PatientProfile profile = patientService.updatePatientProfile(id, updated);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update patient: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePatientProfile(@PathVariable int id) {
        try {
            patientService.deletePatientProfile(id);
            return ResponseEntity.ok("Patient deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Patient not found");
        }
    }
}
