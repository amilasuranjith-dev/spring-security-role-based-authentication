package edu.psy.service.impl;

import edu.psy.dto.LoginRequest;
import edu.psy.dto.LoginResponse;
import edu.psy.dto.RegisterRequest;
import edu.psy.dto.RegisterResponse;
import edu.psy.model.User;
import edu.psy.repository.UserRepository;
import edu.psy.security.JwtUtil;
import edu.psy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        // Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("ROLE_USER"); // default
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        // Save to database
        int result = userRepository.createUser(user);
        if (result == 0) {
            throw new RuntimeException("User registration failed");
        }

        RegisterResponse response = new RegisterResponse();
        response.setMessage("User registered successfully");
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // Find user by name
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOpt.get();
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User account is disabled");
        }
        // Generate a token
        String token = jwtUtil.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        return response;
    }
}
