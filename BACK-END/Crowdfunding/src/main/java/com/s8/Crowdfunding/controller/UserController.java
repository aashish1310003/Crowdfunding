package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.dto.ApiResponse;

import com.s8.Crowdfunding.exceptions.InvaildStatusException;
import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.s8.Crowdfunding.dto.UserRequest;
import com.s8.Crowdfunding.exceptions.UserExistsException;
import com.s8.Crowdfunding.service.UserService;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userDto) {
        try {
            return ResponseEntity.ok(userService.createUser(userDto));
        } catch (UserExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("user already exits", e.getMessage() + ""));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("can't create user", e.getMessage()));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getProjectByUserAndStatus(@RequestParam("id") Long userId,
            @RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(userService.getUserProjectsByStatus(userId, status));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/target")
    public ResponseEntity<?> getProjectByUserAndTarget(@RequestParam("id") Long userId,
            @RequestParam("goal") Boolean goal) {
        try {
            return ResponseEntity.ok(userService.getUserProjectsByTarget(userId, goal));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam("email") String email) {
        try {
            return ResponseEntity.ok(new ApiResponse("SUCCESS", userService.getUsersByEmail(email)));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/id")
    public ResponseEntity<ApiResponse> getUserById(@RequestParam("id") Long userId) {
        try {
            return ResponseEntity.ok(new ApiResponse("SUCCESS", userService.getUsersById(userId)));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
