package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
}
