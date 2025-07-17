package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.entity.Doctor;
import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.repository.DoctorRepository;
import com.hexaware.amazecare.repository.PatientRepository;
import com.hexaware.amazecare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Mock
    private DoctorRepository doctorRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PatientRepository patientRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDoctorProfile_Success() {
        String email = "doc@example.com";
        User user = new User();
        user.setEmail(email);
        Doctor doctor = new Doctor();
        doctor.setUser(user);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(doctorRepo.findByUser(user)).thenReturn(Optional.of(doctor));

        Doctor result = doctorService.getDoctorProfile(email);
        assertNotNull(result);
        assertEquals(email, result.getUser().getEmail());
    }

    @Test
    void testUpdateDoctorProfile_Success() {
        String email = "doc@example.com";
        User user = new User();
        user.setEmail(email);

        Doctor existingDoctor = new Doctor();
        existingDoctor.setUser(user);

        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setSpecialty("Cardiology");
        updatedDoctor.setExperience(15);
        updatedDoctor.setQualification("MD");
        updatedDoctor.setDesignation("Consultant");

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(doctorRepo.findByUser(user)).thenReturn(Optional.of(existingDoctor));
        when(doctorRepo.save(any(Doctor.class))).thenAnswer(inv -> inv.getArgument(0));

        Doctor result = doctorService.updateDoctorProfile(email, updatedDoctor);

        assertEquals("Cardiology", result.getSpecialty());
        assertEquals(15, result.getExperience());
        assertEquals("MD", result.getQualification());
        assertEquals("Consultant", result.getDesignation());
    }

    @Test
    void testGetAllDoctors() {
        List<Doctor> doctors = Arrays.asList(new Doctor(), new Doctor());
        when(doctorRepo.findAll()).thenReturn(doctors);

        List<Doctor> result = doctorService.getAllDoctors();

        assertEquals(2, result.size());
    }

    @Test
    void testSearchDoctorsBySpecialty() {
        String keyword = "Dermatology";
        List<Doctor> doctors = Arrays.asList(new Doctor(), new Doctor());
        when(doctorRepo.findBySpecialtyContainingIgnoreCase(keyword)).thenReturn(doctors);

        List<Doctor> result = doctorService.searchDoctorsBySpecialty(keyword);
        assertEquals(2, result.size());
    }

    @Test
    void testUpdatePatientMedicalHistory() {
        Long patientId = 1L;
        String history = "Diabetes and Hypertension";

        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepo.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepo.save(any(Patient.class))).thenAnswer(inv -> inv.getArgument(0));

        Patient result = doctorService.updatePatientMedicalHistory(patientId, history);

        assertEquals(history, result.getMedicalHistory());
    }

    @Test
    void testGetDoctorProfile_UserNotFound() {
        String email = "unknown@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            doctorService.getDoctorProfile(email)
        );
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUpdatePatientMedicalHistory_PatientNotFound() {
        Long patientId = 999L;
        when(patientRepo.findById(patientId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            doctorService.updatePatientMedicalHistory(patientId, "history")
        );
        assertEquals("Patient not found", exception.getMessage());
    }
}
