package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientProfile, Integer> {

    boolean existsByPatientId(Integer patientId); 
}
