package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.dto.DoctorProfileRequestDTO;
import com.hexaware.amazecare.entity.DoctorProfile;
import com.hexaware.amazecare.service.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/create")
    public ResponseEntity<?> createDoctorProfile(@RequestBody DoctorProfileRequestDTO dto) {
        try {
            DoctorProfile profile = doctorService.createDoctorProfile(
                    dto.getUserId(),
                    dto.getSpecialty(),
                    dto.getQualification(),
                    dto.getDesignation(),
                    dto.getExperienceYears()
            );
            return ResponseEntity.ok(profile);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorProfile>> searchBySpecialty(@RequestParam String keyword) {
        return ResponseEntity.ok(doctorService.searchBySpecialty(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable int id) {
        try {
            DoctorProfile profile = doctorService.getDoctorById(id);
            return ResponseEntity.ok(profile);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Doctor not found");
        }
    }
}
