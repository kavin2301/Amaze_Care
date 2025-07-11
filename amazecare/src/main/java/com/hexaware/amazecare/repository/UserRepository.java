package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email); // For login

    boolean existsByEmail(String email); // Prevent duplicate

    List<User> findByRole(Role role); // Get all admins/doctors/patients
}