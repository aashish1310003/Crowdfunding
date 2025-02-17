package com.s8.Crowdfunding.controller;

import com.s8.Crowdfunding.dto.ApiResponse;
import com.s8.Crowdfunding.model.Project;
import org.hibernate.annotations.NotFound;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import com.s8.Crowdfunding.dto.UserRequest;
import com.s8.Crowdfunding.service.UserService;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
        // TODO: process POST request
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getProjectByUserAndStatus(@RequestParam("id") Long userId, @RequestParam("status") String status) {
        try {
            return ResponseEntity.ok(new ApiResponse("SUCCESS", userService.getUserProjectsByStatus(userId, status)));
        } catch (Exception e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/target")
    public ResponseEntity<ApiResponse>  getProjectByUserAndTarget(@RequestParam("id") Long userId, @RequestParam("goal") Boolean goal) {
        try {
            return ResponseEntity.ok(new ApiResponse("SUCCESS", userService.getUserProjectsByTarget(userId, goal)));
        } catch (Exception e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
