package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.model.Users;
import org.springframework.stereotype.Service;
import com.s8.Crowdfunding.dto.UserRequest;
import com.s8.Crowdfunding.repository.UserRepository;

import java.time.Year;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users createUser(UserRequest userDto) {
        ModelMapper mapper = new ModelMapper();
        Users user = mapper.map(userDto, Users.class);

        int passOut = Integer.parseInt(userDto.getEmail()
                .chars()
                .filter(Character::isDigit)
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining()));
//        System.out.println(passOut);
        user.setRole(Year.now().getValue() % 100 <= (passOut + 4) ? "STUDENT" : "DONOR");
        return userRepository.save(user);
    }
}
