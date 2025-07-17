package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.TestRecommendation;
import com.hexaware.amazecare.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRecommendationRepository extends JpaRepository<TestRecommendation, Long> {
    List<TestRecommendation> findByConsultation(Consultation consultation);
    List<TestRecommendation> findByConsultationId(Long consultationId); // ✅ new
}
