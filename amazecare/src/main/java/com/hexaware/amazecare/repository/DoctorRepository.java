package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Doctor;
import com.hexaware.amazecare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    List<Doctor> findBySpecialtyContainingIgnoreCase(String keyword);
}
