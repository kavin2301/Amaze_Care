package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.entity.Doctor;
import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/doctor/profile")
    public ResponseEntity<Doctor> getDoctorProfile() {
        String email = getLoggedInEmail();
        Doctor doctor = doctorService.getDoctorProfile(email);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/doctor/profile")
    public ResponseEntity<Doctor> updateDoctorProfile(@RequestBody Doctor updatedDoctor) {
        String email = getLoggedInEmail();
        Doctor updated = doctorService.updateDoctorProfile(email, updatedDoctor);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam String specialty) {
        List<Doctor> doctors = doctorService.searchDoctorsBySpecialty(specialty);
        return ResponseEntity.ok(doctors);
    }
    
    @PutMapping("/doctor/patient/{id}/medical-history")
    public ResponseEntity<Patient> updateMedicalHistory(
        @PathVariable Long id,
        @RequestBody String history
    ) {
        Patient updated = doctorService.updatePatientMedicalHistory(id, history);
        return ResponseEntity.ok(updated);
    }


    private String getLoggedInEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
