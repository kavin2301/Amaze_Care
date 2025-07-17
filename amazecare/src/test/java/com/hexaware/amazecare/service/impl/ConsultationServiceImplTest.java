package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import com.hexaware.amazecare.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultationServiceImplTest {

    @InjectMocks
    private ConsultationServiceImpl consultationService;

    @Mock
    private AppointmentRepository appointmentRepo;

    @Mock
    private ConsultationRepository consultationRepo;

    @Mock
    private PrescriptionRepository prescriptionRepo;

    @Mock
    private TestRecommendationRepository testRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddConsultation_Success() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        Consultation details = new Consultation();
        details.setSymptoms("Fever");
        details.setPhysicalExamination("Normal");
        details.setTreatmentPlan("Rest");

        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(consultationRepo.save(any(Consultation.class))).thenAnswer(inv -> inv.getArgument(0));
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(appointment);

        Consultation result = consultationService.addConsultation(appointmentId, details);

        assertNotNull(result);
        assertEquals("Fever", result.getSymptoms());
        verify(appointmentRepo).save(appointment);
    }

    @Test
    void testAddConsultation_AppointmentNotFound() {
        Long appointmentId = 99L;
        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> consultationService.addConsultation(appointmentId, new Consultation()));

        assertEquals("Appointment not found", ex.getMessage());
    }

    @Test
    void testAddConsultation_NotConfirmed() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.REQUESTED);

        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.of(appointment));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> consultationService.addConsultation(appointmentId, new Consultation()));

        assertEquals("Only confirmed appointments can have consultations", ex.getMessage());
    }

    @Test
    void testGetConsultationByAppointmentId_Success() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        Consultation consultation = new Consultation();

        when(appointmentRepo.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(consultationRepo.findByAppointment(appointment)).thenReturn(Optional.of(consultation));

        Consultation result = consultationService.getConsultationByAppointmentId(appointmentId);
        assertNotNull(result);
    }

    @Test
    void testGetConsultationByAppointmentId_NotFound() {
        when(appointmentRepo.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class,
                () -> consultationService.getConsultationByAppointmentId(1L));

        assertEquals("Appointment not found", ex.getMessage());
    }

    @Test
    void testAddPrescriptions_Success() {
        Long consultationId = 1L;
        Consultation consultation = new Consultation();
        Prescription p1 = new Prescription();
        Prescription p2 = new Prescription();
        List<Prescription> prescriptions = Arrays.asList(p1, p2);

        when(consultationRepo.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(prescriptionRepo.saveAll(anyList())).thenReturn(prescriptions);

        List<Prescription> result = consultationService.addPrescriptions(consultationId, prescriptions);

        assertEquals(2, result.size());
        verify(prescriptionRepo).saveAll(prescriptions);
    }

    @Test
    void testGetPrescriptions_Success() {
        Long consultationId = 1L;
        Consultation consultation = new Consultation();
        List<Prescription> expected = Arrays.asList(new Prescription(), new Prescription());

        when(consultationRepo.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(prescriptionRepo.findByConsultation(consultation)).thenReturn(expected);

        List<Prescription> result = consultationService.getPrescriptions(consultationId);
        assertEquals(2, result.size());
    }

    @Test
    void testAddTestRecommendations_Success() {
        Long consultationId = 1L;
        Consultation consultation = new Consultation();
        TestRecommendation t1 = new TestRecommendation();
        TestRecommendation t2 = new TestRecommendation();
        List<TestRecommendation> recommendations = Arrays.asList(t1, t2);

        when(consultationRepo.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(testRepo.saveAll(anyList())).thenReturn(recommendations);

        List<TestRecommendation> result = consultationService.addTestRecommendations(consultationId, recommendations);
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateConsultation_Success() {
        Long consultationId = 1L;
        Consultation existing = new Consultation();
        Consultation updated = new Consultation();
        updated.setSymptoms("Cough");
        updated.setPhysicalExamination("Lungs clear");
        updated.setTreatmentPlan("Medication");

        when(consultationRepo.findById(consultationId)).thenReturn(Optional.of(existing));
        when(consultationRepo.save(any(Consultation.class))).thenAnswer(inv -> inv.getArgument(0));

        Consultation result = consultationService.updateConsultation(consultationId, updated);

        assertEquals("Cough", result.getSymptoms());
        assertEquals("Lungs clear", result.getPhysicalExamination());
        assertEquals("Medication", result.getTreatmentPlan());
    }

    @Test
    void testUpdateConsultation_NotFound() {
        when(consultationRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> consultationService.updateConsultation(99L, new Consultation()));

        assertEquals("Consultation not found", ex.getMessage());
    }
}
