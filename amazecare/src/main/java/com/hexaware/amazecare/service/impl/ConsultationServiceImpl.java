package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import com.hexaware.amazecare.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private ConsultationRepository consultationRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private TestRecommendationRepository testRepo;

    @Override
    public Consultation addConsultation(Long appointmentId, Consultation consultationDetails) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getStatus().equals(AppointmentStatus.CONFIRMED)) {
            throw new RuntimeException("Only confirmed appointments can have consultations");
        }

        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setSymptoms(consultationDetails.getSymptoms());
        consultation.setPhysicalExamination(consultationDetails.getPhysicalExamination());
        consultation.setTreatmentPlan(consultationDetails.getTreatmentPlan());

        Consultation savedConsultation = consultationRepo.save(consultation);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepo.save(appointment);

        return savedConsultation;
    }

    @Override
    public Consultation getConsultationByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        return consultationRepo.findByAppointment(appointment)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
    }

    @Override
    public List<Prescription> addPrescriptions(Long consultationId, List<Prescription> prescriptions) {
        Consultation consultation = consultationRepo.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        for (Prescription p : prescriptions) {
            p.setConsultation(consultation);
        }

        return prescriptionRepo.saveAll(prescriptions);
    }

    @Override
    public List<TestRecommendation> addTestRecommendations(Long consultationId, List<TestRecommendation> tests) {
        Consultation consultation = consultationRepo.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        for (TestRecommendation t : tests) {
            t.setConsultation(consultation);
        }

        return testRepo.saveAll(tests);
    }

    @Override
    public List<Prescription> getPrescriptions(Long consultationId) {
        Consultation consultation = consultationRepo.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        return prescriptionRepo.findByConsultation(consultation);
    }

    @Override
    public List<TestRecommendation> getTestRecommendations(Long consultationId) {
        Consultation consultation = consultationRepo.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        return testRepo.findByConsultation(consultation);
    }
    
    @Override
    public Consultation updateConsultation(Long consultationId, Consultation updatedData) {
        Consultation existing = consultationRepo.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        existing.setSymptoms(updatedData.getSymptoms());
        existing.setPhysicalExamination(updatedData.getPhysicalExamination());
        existing.setTreatmentPlan(updatedData.getTreatmentPlan());

        return consultationRepo.save(existing);
    }
}
