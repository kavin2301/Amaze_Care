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

class DoctorServiceTest {
    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepo;

    @Mock
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDoctorProfileSuccess() {
        User user = new User();
        user.setUserId(1);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(doctorRepo.existsByDoctorId(1)).thenReturn(false);

        DoctorProfile mockProfile = new DoctorProfile();
        mockProfile.setSpecialty("Cardio");
        when(doctorRepo.save(any(DoctorProfile.class))).thenReturn(mockProfile);

        DoctorProfile profile = doctorService.createDoctorProfile(1, "Cardio", "MBBS", "Sr. Doctor", 10);
        assertEquals("Cardio", profile.getSpecialty());
    }

    @Test
    void testCreateDoctorProfileAlreadyExists() {
        when(doctorRepo.existsByDoctorId(1)).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> doctorService.createDoctorProfile(1, "x", "x", "x", 5));
    }

    @Test
    void testSearchBySpecialty() {
        DoctorProfile d1 = new DoctorProfile();
        d1.setSpecialty("Cardiology");
        when(doctorRepo.findBySpecialtyContainingIgnoreCase("Cardio")).thenReturn(List.of(d1));

        List<DoctorProfile> list = doctorService.searchBySpecialty("Cardio");
        assertEquals(1, list.size());
    }
}