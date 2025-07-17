package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;

    private User mockPatientUser;
    private User mockDoctorUser;
    private Patient mockPatient;
    private Doctor mockDoctor;
    private Appointment mockAppointment;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        mockPatientUser = new User();
        mockPatientUser.setId(1L);
        mockPatientUser.setEmail("patient@example.com");

        mockDoctorUser = new User();
        mockDoctorUser.setId(2L);
        mockDoctorUser.setEmail("doctor@example.com");

        mockPatient = new Patient();
        mockPatient.setId(1L);
        mockPatient.setUser(mockPatientUser);

        mockDoctor = new Doctor();
        mockDoctor.setId(2L);
        mockDoctor.setUser(mockDoctorUser);

        mockAppointment = new Appointment();
        mockAppointment.setId(100L);
        mockAppointment.setDoctor(mockDoctor);
        mockAppointment.setPatient(mockPatient);
        mockAppointment.setAppointmentDateTime(LocalDateTime.now());
        mockAppointment.setStatus(AppointmentStatus.REQUESTED);
    }

    @Test
    void testBookAppointment_Success() {
        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(mockPatientUser));
        when(patientRepository.findByUser(mockPatientUser)).thenReturn(Optional.of(mockPatient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(mockDoctor));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);

        Appointment result = appointmentService.bookAppointment(2L, "patient@example.com", LocalDateTime.now(), "Checkup");

        assertNotNull(result);
        assertEquals(mockPatient, result.getPatient());
        assertEquals(mockDoctor, result.getDoctor());
    }

    @Test
    void testGetAppointmentsForPatient_Success() {
        List<Appointment> appointments = List.of(mockAppointment);

        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(mockPatientUser));
        when(patientRepository.findByUser(mockPatientUser)).thenReturn(Optional.of(mockPatient));
        when(appointmentRepository.findByPatient(mockPatient)).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAppointmentsForPatient("patient@example.com");

        assertEquals(1, result.size());
    }

    @Test
    void testGetAppointmentsForDoctor_Success() {
        List<Appointment> appointments = List.of(mockAppointment);

        when(userRepository.findByEmail("doctor@example.com")).thenReturn(Optional.of(mockDoctorUser));
        when(doctorRepository.findByUser(mockDoctorUser)).thenReturn(Optional.of(mockDoctor));
        when(appointmentRepository.findByDoctor(mockDoctor)).thenReturn(appointments);

        List<Appointment> result = appointmentService.getAppointmentsForDoctor("doctor@example.com");

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateAppointmentStatus_Success() {
        when(appointmentRepository.findById(100L)).thenReturn(Optional.of(mockAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(mockAppointment);

        Appointment result = appointmentService.updateAppointmentStatus(100L, AppointmentStatus.CONFIRMED);

        assertEquals(AppointmentStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void testCancelAppointment_Success() {
        mockAppointment.setStatus(AppointmentStatus.CONFIRMED);

        when(userRepository.findByEmail("patient@example.com")).thenReturn(Optional.of(mockPatientUser));
        when(appointmentRepository.findById(100L)).thenReturn(Optional.of(mockAppointment));

        mockAppointment.setPatient(mockPatient); // ensure patient linkage

        boolean result = appointmentService.cancelAppointment(100L, "patient@example.com");

        assertTrue(result);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void testCancelAppointment_Unauthorized() {
        String emailFromRequest = "unauthorized@example.com"; // patientEmail passed in cancelAppointment()
        String emailFromAppointment = "patient@example.com";  // email linked to the appointment

        User mockPatientUser = new User();
        mockPatientUser.setEmail(emailFromRequest);

        User appointmentUser = new User();
        appointmentUser.setEmail(emailFromAppointment); // Different email to simulate unauthorized access

        Patient patient = new Patient();
        patient.setUser(appointmentUser); // Attach the other user

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(patient);

        when(userRepository.findByEmail(emailFromRequest)).thenReturn(Optional.of(mockPatientUser));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        // Expect RuntimeException due to unauthorized access
        assertThrows(RuntimeException.class, () -> {
            appointmentService.cancelAppointment(1L, emailFromRequest);
        });

        verify(appointmentRepository, never()).save(any());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
