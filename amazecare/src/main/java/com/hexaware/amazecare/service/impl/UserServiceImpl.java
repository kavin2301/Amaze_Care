package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.dto.AuthResponse;
import com.hexaware.amazecare.dto.LoginRequest;
import com.hexaware.amazecare.dto.RegisterRequest;
import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import com.hexaware.amazecare.security.JwtUtil;
import com.hexaware.amazecare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String registerUser(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            return "Email already registered!";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        User savedUser = userRepo.save(user);

        switch (request.getRole()) {
            case PATIENT -> {
                Patient patient = new Patient();
                patient.setUser(savedUser);
                patientRepo.save(patient);
            }
            case DOCTOR -> {
                Doctor doctor = new Doctor();
                doctor.setUser(savedUser);
                doctorRepo.save(doctor);
            }
            default -> {
            }
        }

        return "User registered successfully!";
    }

    @Override
    public AuthResponse authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow();

        if (user.getStatus() != Status.ACTIVE) {
            throw new RuntimeException("Account is inactive. Please contact admin.");
        }

        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token, user.getRole().name());
    }
}
