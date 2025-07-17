package com.hexaware.amazecare.service;

import com.hexaware.amazecare.dto.RegisterRequest;
import com.hexaware.amazecare.dto.LoginRequest;
import com.hexaware.amazecare.dto.AuthResponse;

public interface UserService {
    String registerUser(RegisterRequest request);
    AuthResponse authenticateUser(LoginRequest request);
}
