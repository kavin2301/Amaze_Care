package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.dto.RegisterRequest;
import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import com.hexaware.amazecare.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PatientRepository patientRepo;

    @Mock
    private DoctorRepository doctorRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_WithValidPatient_ShouldReturnSuccessMessage() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");
        request.setRole(Role.PATIENT);

        when(userRepo.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.PATIENT);

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        // Act
        String result = userService.registerUser(request);

        // Assert
        assertEquals("User registered successfully!", result);
        verify(userRepo, times(1)).save(any(User.class));
        verify(patientRepo, times(1)).save(any(Patient.class));
        verify(doctorRepo, never()).save(any(Doctor.class));
    }

    @Test
    public void testRegisterUser_WithExistingEmail_ShouldReturnErrorMessage() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");

        when(userRepo.existsByEmail("existing@example.com")).thenReturn(true);

        // Act
        String result = userService.registerUser(request);

        // Assert
        assertEquals("Email already registered!", result);
        verify(userRepo, never()).save(any(User.class));
        verify(patientRepo, never()).save(any(Patient.class));
        verify(doctorRepo, never()).save(any(Doctor.class));
    }

    @Test
    public void testRegisterUser_WithDoctorRole_ShouldSaveDoctorEntity() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Dr. Smith");
        request.setEmail("drsmith@example.com");
        request.setPassword("docpass");
        request.setRole(Role.DOCTOR);

        when(userRepo.existsByEmail("drsmith@example.com")).thenReturn(false);
        when(passwordEncoder.encode("docpass")).thenReturn("encodedDocPass");

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setName("Dr. Smith");
        savedUser.setEmail("drsmith@example.com");
        savedUser.setPassword("encodedDocPass");
        savedUser.setRole(Role.DOCTOR);

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        // Act
        String result = userService.registerUser(request);

        // Assert
        assertEquals("User registered successfully!", result);
        verify(userRepo, times(1)).save(any(User.class));
        verify(doctorRepo, times(1)).save(any(Doctor.class));
        verify(patientRepo, never()).save(any(Patient.class));
    }
}
