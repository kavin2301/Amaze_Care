package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.dto.RegisterRequest;
import com.hexaware.amazecare.dto.UpdateDoctorRequest;
import com.hexaware.amazecare.dto.UpdatePatientRequest;
import com.hexaware.amazecare.dto.UpdateUserRequest;
import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import com.hexaware.amazecare.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock private UserRepository userRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private ConsultationRepository consultationRepository;
    @Mock private PrescriptionRepository prescriptionRepository;
    @Mock private TestRecommendationRepository testRecommendationRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserWithRole_Doctor() {
        RegisterRequest request = new RegisterRequest("Alice", "alice@example.com", "alice123", Role.DOCTOR);

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("alice123")).thenReturn("encodedPass");

        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("encodedPass");
        user.setRole(Role.DOCTOR);

        Doctor doctor = new Doctor();
        doctor.setUser(user);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        String result = adminService.createUserWithRole(request, Role.DOCTOR);

        assertEquals("DOCTOR created successfully.", result);
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Bob");
        request.setEmail("bob@example.com");
        request.setStatus(Status.INACTIVE);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old");
        existingUser.setEmail("old@example.com");
        existingUser.setRole(Role.PATIENT);
        existingUser.setStatus(Status.ACTIVE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        String result = adminService.updateUser(userId, request);

        assertEquals("User updated successfully.", result);
        assertEquals("Bob", existingUser.getName());
        assertEquals("bob@example.com", existingUser.getEmail());
        assertEquals(Status.INACTIVE, existingUser.getStatus());
    }

    @Test
    void testUpdatePatient() {
        Long patientId = 2L;
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setDob(LocalDate.of(1995, 4, 10));
        request.setGender("Female");
        request.setContactNumber("9876543210");
        request.setMedicalHistory("Updated history");

        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        String result = adminService.updatePatient(patientId, request);

        assertEquals("Patient profile updated.", result);
        assertEquals("Female", patient.getGender());
        assertEquals("9876543210", patient.getContactNumber());
        assertEquals("Updated history", patient.getMedicalHistory());
        assertEquals(LocalDate.of(1995, 4, 10), patient.getDob());
    }

    @Test
    void testUpdateDoctor() {
        Long doctorId = 3L;
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setSpecialty("Cardiology");
        request.setExperience(12);
        request.setQualification("MD");
        request.setDesignation("Senior Consultant");

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        String result = adminService.updateDoctor(doctorId, request);

        assertEquals("Doctor profile updated.", result);
        assertEquals("Cardiology", doctor.getSpecialty());
        assertEquals(12, doctor.getExperience());
        assertEquals("MD", doctor.getQualification());
        assertEquals("Senior Consultant", doctor.getDesignation());
    }

    @Test
    void testToggleUserStatus() {
        Long userId = 4L;
        User user = new User();
        user.setId(userId);
        user.setStatus(Status.ACTIVE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = adminService.toggleUserStatus(userId, Status.INACTIVE);

        assertEquals("User status updated to INACTIVE", result);
        assertEquals(Status.INACTIVE, user.getStatus());
    }
}
