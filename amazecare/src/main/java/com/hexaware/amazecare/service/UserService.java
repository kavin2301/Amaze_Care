package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Role;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }


    public User login(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    public User getUserById(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<User> getUsersByRole(Role role) {
        return userRepo.findByRole(role);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(int id) {
        if (!userRepo.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepo.deleteById(id);
    }
    
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
