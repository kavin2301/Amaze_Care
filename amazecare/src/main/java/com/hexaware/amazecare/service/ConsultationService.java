package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Consultation;
import com.hexaware.amazecare.entity.Prescription;
import com.hexaware.amazecare.entity.TestRecommendation;

import java.util.List;

public interface ConsultationService {
    Consultation addConsultation(Long appointmentId, Consultation consultation);
    Consultation getConsultationByAppointmentId(Long appointmentId);

    List<Prescription> addPrescriptions(Long consultationId, List<Prescription> prescriptions);
    List<TestRecommendation> addTestRecommendations(Long consultationId, List<TestRecommendation> tests);

    List<Prescription> getPrescriptions(Long consultationId);
    List<TestRecommendation> getTestRecommendations(Long consultationId);
    Consultation updateConsultation(Long consultationId, Consultation updatedData);
}
