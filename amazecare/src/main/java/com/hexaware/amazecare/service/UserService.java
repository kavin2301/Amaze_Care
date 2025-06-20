package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public User registerUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        return userRepo.save(user);
    }

    public User login(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    public List<User> getUsersByRole(Role role) {
        return userRepo.findByRole(role);
    }

    public User getUserById(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
