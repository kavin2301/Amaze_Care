package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDoctorProfile_Success() {
        User user = new User();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        DoctorProfile profile = new DoctorProfile();
        profile.setSpecialty("Cardiology");

        when(doctorRepo.save(any(DoctorProfile.class))).thenReturn(profile);

        DoctorProfile result = doctorService.createDoctorProfile(1, "Cardiology", "MBBS", "Consultant", 5);
        assertEquals("Cardiology", result.getSpecialty());
    }

    @Test
    void testCreateDoctorProfile_UserNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> doctorService.createDoctorProfile(1, "Neuro", "MBBS", "Consultant", 10));
    }
}