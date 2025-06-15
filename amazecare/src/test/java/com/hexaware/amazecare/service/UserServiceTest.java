package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.User;
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

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRole(Role.PATIENT);

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testRegisterUser_EmailExists() {
        User user = new User();
        user.setEmail("existing@example.com");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    public void testFindUserByEmail_UserFound() {
        User user = new User();
        user.setEmail("findme@example.com");

        when(userRepository.findByEmail("findme@example.com")).thenReturn(Optional.of(user));

        User result = userService.findUserByEmail("findme@example.com");
        assertEquals("findme@example.com", result.getEmail());
    }

    @Test
    public void testFindUserByEmail_UserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findUserByEmail("notfound@example.com"));
    }
}