package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.model.Users;
import com.s8.Crowdfunding.repository.ProjectRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.s8.Crowdfunding.dto.UserRequest;
import com.s8.Crowdfunding.exceptions.ResourceNotFoundException;
import com.s8.Crowdfunding.exceptions.UserExistsException;
import com.s8.Crowdfunding.repository.UserRepository;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

@Service
public class UserService implements IUserService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final CommonService commonService;

    public UserService(UserRepository userRepository, ProjectService projectService, ProjectRepository projectRepository, CommonService commonService) {
        this.projectService = projectService;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.commonService = commonService;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUsersById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

    public Users createUser(UserRequest userDto) throws UserExistsException {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserExistsException("User already exists with this email!");
        }

        if (!userDto.getEmail().endsWith("@bitsathy.ac.in")) {
            throw new UserExistsException("Supports only BIT email addresses");
        }

        ModelMapper mapper = new ModelMapper();
        Users user = mapper.map(userDto, Users.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode only once

        // Extract numeric values from email (if present)
        String extractedDigits = userDto.getEmail()
                .chars()
                .filter(Character::isDigit)
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());

        int passOut = extractedDigits.isEmpty() ? -1 : Integer.parseInt(extractedDigits);
        System.out.println("Extracted Year: " + passOut);

        // Assign role based on extracted year
        if (passOut == -1) {
            user.setRole("DONOR"); // No numbers in email -> Assign DONOR role
        } else {
            user.setRole(Year.now().getValue() % 100 <= (passOut + 4) ? "STUDENT" : "DONOR");
        }
//        user.setRole("ADMIN");
        return userRepository.save(user);
    }


    public List<Project> getUserProjectsByStatus(Long userId, String status) {
        getUsersById(userId); // Ensures the user exists, otherwise throws an exception
        commonService.checkStatus(status);
        return projectRepository.findByStatus(status).stream()
                .filter(project -> project.getUser().getUserId().equals(userId))
                .toList();
    }


    public List<Project> getUserProjectsByTarget(Long userId, Boolean goal) {
        getUsersById(userId); // Ensures the user exists
        return (goal ? projectService.getProjectsByGoalReached()
                : projectService.getProjectsByGoalNotReached()).stream()
                .filter(project -> project.getUser().getUserId().equals(userId))
                .toList();
    }


    public Users getUsersByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

}
