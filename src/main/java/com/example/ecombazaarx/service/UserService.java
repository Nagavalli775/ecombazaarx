package com.example.ecombazaarx.service;

import com.example.ecombazaarx.dto.LoginRequest;
import com.example.ecombazaarx.dto.UserSignupRequest;
import com.example.ecombazaarx.entity.Role;
import com.example.ecombazaarx.entity.User;
import com.example.ecombazaarx.repository.UserRepository;
import com.example.ecombazaarx.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

      @Autowired
      private JwtUtil jwtUtil;


    public String registerUser(UserSignupRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        userRepository.save(user);
        return "User registered successfully";
    }

    public Map<String, Object> login(LoginRequest request) {

    User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    String token = jwtUtil.generateToken(user.getUsername());

    return Map.of(
        "message", "Login successful",
        "token", token,
        "role", user.getRole().name()
    );
}

}

