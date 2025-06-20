package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email); 

    List<User> findByRole(Role role);

    boolean existsByEmail(String email); 
}
