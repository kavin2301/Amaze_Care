package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<DoctorProfile, Integer> {

    boolean existsByDoctorId(Integer doctorId);

    List<DoctorProfile> findBySpecialtyContainingIgnoreCase(String keyword); // Search doctors by specialty
}
