package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {
    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepo;

    @Mock
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePatientProfileSuccess() {
        User user = new User();
        user.setUserId(2);
        when(userRepo.findById(2)).thenReturn(Optional.of(user));
        when(patientRepo.existsByPatientId(2)).thenReturn(false);

        PatientProfile profile = new PatientProfile();
        profile.setGender("Male");
        when(patientRepo.save(any(PatientProfile.class))).thenReturn(profile);

        PatientProfile result = patientService.createPatientProfile(2, LocalDate.of(2000, 1, 1), "Male");
        assertEquals("Male", result.getGender());
    }

    @Test
    void testCreatePatientProfileAlreadyExists() {
        when(patientRepo.existsByPatientId(2)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> patientService.createPatientProfile(2, LocalDate.now(), "Female"));
    }
}
