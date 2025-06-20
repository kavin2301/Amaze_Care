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
            LocalDate dateOfBirth = LocalDate.parse(dto.getDob()); 
            PatientProfile profile = patientService.createPatientProfile(
                    dto.getUserId(), dateOfBirth, dto.getGender()
            );
            return ResponseEntity.ok(profile);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Invalid date format or server error");
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
}