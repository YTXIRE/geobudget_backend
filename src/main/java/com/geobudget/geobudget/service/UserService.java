package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.Role;
import com.geobudget.geobudget.dto.UserDTO;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.UserRepository;
import com.geobudget.geobudget.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .city(user.getCity())
                .phone(user.getPhone())
                .country(user.getCountry().getTitle())
                .username(user.getUsername())
                .role(Role.USER)
                .build();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
