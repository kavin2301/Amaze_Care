package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.hexaware.amazecare.entity.Role;
import com.hexaware.amazecare.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        User user = new User();
        user.setEmail("test@mail.com");
        when(userRepo.existsByEmail("test@mail.com")).thenReturn(false);
        when(userRepo.save(user)).thenReturn(user);

        User saved = userService.registerUser(user);
        assertEquals("test@mail.com", saved.getEmail());
    }

    @Test
    void testRegisterUserDuplicate() {
        User user = new User();
        user.setEmail("test@mail.com");
        when(userRepo.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testGetUserByIdSuccess() {
        User user = new User();
        user.setUserId(1);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);
        assertEquals(1, result.getUserId());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepo.findById(2)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(2));
    }
}