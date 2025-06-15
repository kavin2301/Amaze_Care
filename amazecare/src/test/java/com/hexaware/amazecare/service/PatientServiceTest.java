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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePatientProfile_Success() {
        User user = new User();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        PatientProfile profile = new PatientProfile();
        profile.setGender("Male");

        when(patientRepo.save(any(PatientProfile.class))).thenReturn(profile);

        PatientProfile result = patientService.createPatientProfile(1, LocalDate.of(2000, 1, 1), "Male");
        assertEquals("Male", result.getGender());
    }

    @Test
    void testCreatePatientProfile_UserNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> patientService.createPatientProfile(1, LocalDate.now(), "Female"));
    }
}