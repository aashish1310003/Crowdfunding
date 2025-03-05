package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.model.Project;
import com.s8.Crowdfunding.model.Users;
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
    private UserRepository userRepository;
    private ProjectService projectService;

    public UserService(UserRepository userRepository, ProjectService projectService) {
        this.projectService = projectService;
        this.userRepository = userRepository;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUsersById(long userId) {
        return userRepository.findById(userId);
    }

    public Users createUser(UserRequest userDto) throws UserExistsException {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserExistsException("user already exists with this email !");
        }
        int len = userDto.getEmail().length();
        if (!userDto.getEmail().substring(len - 15).equals("@bitsathy.ac.in")) {
            throw new UserExistsException("Supports only BIT email addresses");
        }
        ModelMapper mapper = new ModelMapper();
        Users user = mapper.map(userDto, Users.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setPassword(encodedPassword);
        int passOut = Integer.parseInt(userDto.getEmail()
                .chars()
                .filter(Character::isDigit)
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining()));
        // System.out.println(passOut);
        user.setRole(Year.now().getValue() % 100 <= (passOut + 4) ? "STUDENT" : "DONOR");
        return userRepository.save(user);
    }

    public List<Project> getUserProjectsByStatus(Long userId, String status) {
        return getUsersById(userId).map(
                (users) -> projectService.getProjectsByStatus(status).stream()
                        .filter(project -> project.getUser().getUserId().equals(userId))
                        .toList())
                .orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

    public List<Project> getUserProjectsByTarget(Long userId, Boolean goal) {
        return getUsersById(userId).map(
                (users) -> (goal ? projectService.getProjectsByGoalReached()
                        : projectService.getProjectsByGoalNotReached()).stream()
                        .peek(System.out::println)
                        .filter(project -> project.getUser().getUserId().equals(userId))
                        .peek(System.out::println)
                        .toList())
                .orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

    public Users getUsersByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No user found"));
    }

}
