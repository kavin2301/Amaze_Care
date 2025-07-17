package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
}
