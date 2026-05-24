package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            
            System.out.println("Registration attempt - Username: " + username + ", Email: " + email);
            
            // Check if username exists
            if (userRepository.existsByUsername(username)) {
                response.put("error", "Username already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Check if email exists
            if (userRepository.existsByEmail(email)) {
                response.put("error", "Email already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Create user (auto-verified)
            User user = new User(username, email, passwordEncoder.encode(password));
            user.setVerified(true); // Auto-verify users
            
            User savedUser = userRepository.save(user);
            System.out.println("User saved successfully with ID: " + savedUser.getId());
            
            // Generate token and log user in immediately
            String token = jwtUtil.generateToken(username);
            
            response.put("token", token);
            response.put("username", username);
            response.put("role", user.getRole());
            response.put("message", "Registration successful!");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            response.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        
        try {
            String username = request.get("username");
            String password = request.get("password");
            
            System.out.println("Login attempt - Username: " + username);
            
            User user = userRepository.findByUsername(username).orElse(null);
            
            if (user == null) {
                response.put("error", "User not found");
                return ResponseEntity.status(401).body(response);
            }
            
            // Check if user is banned
            if (user.isBanned()) {
                response.put("error", "Your account has been banned. Please contact admin.");
                return ResponseEntity.status(403).body(response);
            }
            
            if (!passwordEncoder.matches(password, user.getPassword())) {
                response.put("error", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }
            
            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            String token = jwtUtil.generateToken(username);
            
            response.put("token", token);
            response.put("username", username);
            response.put("role", user.getRole());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            response.put("error", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}