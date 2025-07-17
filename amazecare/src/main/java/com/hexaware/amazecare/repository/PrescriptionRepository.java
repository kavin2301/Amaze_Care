package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Prescription;
import com.hexaware.amazecare.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByConsultation(Consultation consultation);
    List<Prescription> findByConsultationId(Long consultationId); // ✅ new
}

