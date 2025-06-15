package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<DoctorProfile, Integer> {
}