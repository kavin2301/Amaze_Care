package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.dto.DoctorProfileRequestDTO;
import com.hexaware.amazecare.dto.PatientProfileRequestDTO;
import com.hexaware.amazecare.entity.DoctorProfile;
import com.hexaware.amazecare.entity.PatientProfile;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.entity.Role;
import com.hexaware.amazecare.service.DoctorService;
import com.hexaware.amazecare.service.PatientService;
import com.hexaware.amazecare.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/test")
    public ResponseEntity<String> testAdminAccess() {
        return ResponseEntity.ok("✅ Admin access verified successfully!");
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsersByRole(@RequestParam Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

}