package com.example.Userservice.Service;

import com.example.Userservice.Model.User;
import com.example.Userservice.Repository.UserRepository;
import com.example.Userservice.config.JwtUtil;
import com.example.Userservice.DTO.*;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;  
        this.jwtUtil = jwtUtil;
    }

    
    
  

    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Entered password:" + loginRequest.getPassword());
        System.out.println("Stored Hashed Password: " + user.getPassword());
    
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");    
        }
        
        String token = jwtUtil.generateToken(user);

        String roleName = user.getRole().getRoleName(); // Assuming User has a Role entity linked
        UUID userId = user.getUserId();

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken(token);
    loginResponse.setRole(roleName);
    loginResponse.setUserId(userId); 

        return loginResponse;
        
    }



} 