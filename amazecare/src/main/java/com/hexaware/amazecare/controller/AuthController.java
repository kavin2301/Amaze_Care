package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.security.JwtUtil;
import com.hexaware.amazecare.security.CustomUserDetailsService;
import com.hexaware.amazecare.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }

        
        User user = userService.getUserByEmail(email); 
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        String token = jwtUtil.generateToken(userDetails.getUsername(), user.getRole().toString());

        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("role", user.getRole().toString());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }
}
