package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.repository.PatientRepository;
import com.hexaware.amazecare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterPatientSuccess() {
        User user = new User();
        user.setId(1L);

        Patient patient = new Patient();

        when(patientRepository.findByUser(user)).thenReturn(Optional.empty());

        patientService.registerPatient(user, patient);

        assertEquals(user, patient.getUser());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void testRegisterPatientAlreadyExists() {
        User user = new User();
        Patient existingPatient = new Patient();

        when(patientRepository.findByUser(user)).thenReturn(Optional.of(existingPatient));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                patientService.registerPatient(user, new Patient()));

        assertEquals("Patient profile already exists for this user.", exception.getMessage());
    }

    @Test
    void testGetPatientProfileSuccess() {
        String email = "patient@example.com";
        User user = new User();
        user.setId(1L);
        Patient patient = new Patient();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(patientRepository.findByUser(user)).thenReturn(Optional.of(patient));

        Patient result = patientService.getPatientProfile(email);

        assertEquals(patient, result);
    }

    @Test
    void testGetPatientProfileUserNotFound() {
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                patientService.getPatientProfile(email));

        assertEquals("User not found: " + email, exception.getMessage());
    }

    @Test
    void testGetPatientProfilePatientNotFound() {
        String email = "noPatient@example.com";
        User user = new User();
        user.setId(2L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(patientRepository.findByUser(user)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                patientService.getPatientProfile(email));

        assertEquals("Patient profile not found for user ID: " + user.getId(), exception.getMessage());
    }

    @Test
    void testUpdatePatientProfileSuccess() {
        String email = "update@example.com";
        User user = new User();
        user.setId(3L);

        Patient existingPatient = new Patient();
        existingPatient.setDob(LocalDate.parse("1990-01-01"));
        existingPatient.setGender("Male");
        existingPatient.setContactNumber("1234567890");
        existingPatient.setMedicalHistory("None");

        Patient updatedPatient = new Patient();
        updatedPatient.setDob(LocalDate.parse("1991-01-01")); // Fixed here
        updatedPatient.setGender("Female");
        updatedPatient.setContactNumber("9876543210");
        updatedPatient.setMedicalHistory("Updated");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(patientRepository.findByUser(user)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.updatePatientProfile(email, updatedPatient);

        // ✅ Compare LocalDate directly instead of string
        assertEquals(LocalDate.parse("1991-01-01"), result.getDob());
        assertEquals("Female", result.getGender());
        assertEquals("9876543210", result.getContactNumber());
        assertEquals("Updated", result.getMedicalHistory());
    }

    @Test
    void testUpdatePatientProfileUserNotFound() {
        String email = "nouser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                patientService.updatePatientProfile(email, new Patient()));

        assertEquals("User not found: " + email, exception.getMessage());
    }

    @Test
    void testUpdatePatientProfilePatientNotFound() {
        String email = "noprofile@example.com";
        User user = new User();
        user.setId(4L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(patientRepository.findByUser(user)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                patientService.updatePatientProfile(email, new Patient()));

        assertEquals("Patient profile not found for user ID: " + user.getId(), exception.getMessage());
    }
}
