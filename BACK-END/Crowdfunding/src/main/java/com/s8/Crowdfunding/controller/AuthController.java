package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.dto.LoginRequest;
import com.s8.Crowdfunding.model.Users;
import com.s8.Crowdfunding.repository.UserRepository;
import com.s8.Crowdfunding.webtoken.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login API hit with email: " + loginRequest.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            System.out.println("Token generation getting started: " + authentication.toString());

            return jwtService.generateToken((UserDetails) authentication.getPrincipal());
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            throw e; // rethrow to see full error response in logs
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(401).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error validating token: " + e.getMessage());
        }
    }

}