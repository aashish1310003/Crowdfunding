package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.dto.ApiResponse;
import com.s8.Crowdfunding.dto.LoginRequest;
import com.s8.Crowdfunding.model.Users;
import com.s8.Crowdfunding.repository.UserRepository;
import com.s8.Crowdfunding.service.OtpService;
import com.s8.Crowdfunding.webtoken.JwtService;

import java.util.HashMap;

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
    private final OtpService otpService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserRepository userRepository, UserDetailsService userDetailsService, OtpService otpService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.otpService = otpService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login API hit with email: " + loginRequest.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            System.out.println("Token generation getting started: " + authentication.toString());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            boolean isAdmin = userDetails.getAuthorities()
                    .stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));

            if (!isAdmin) {
                otpService.generateOtp(loginRequest.getEmail());
                return ResponseEntity.ok("OTP sent to " + loginRequest.getEmail());
            }

            HashMap<String, String> token = new HashMap<>();
            token.put("token", jwtService.generateToken(userDetails));

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            throw e;
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOpt(@RequestBody LoginRequest loginRequest) {
        try {
            String jwtToken = jwtService.generateToken(userDetailsService.loadUserByUsername(loginRequest.getEmail()));
            if (!otpService.validateOtp(loginRequest.getEmail(), loginRequest.getPassword())) {
                return ResponseEntity.status(401).body("Invalid OTP");
            }
            HashMap<String, String> token = new HashMap<String, String>();
            token.put("token", jwtToken);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            System.out.println("Failed to verify OTP: " + e.getMessage());
            return ResponseEntity.status(400).body(new ApiResponse("Failed to verify OTP", e.getMessage()));
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