package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.Role;
import com.geobudget.geobudget.dto.UserDTO;
import com.geobudget.geobudget.dto.UserPreferencesUpdateRequest;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.UserRepository;
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

        return toDto(user);
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return toDto(user);
    }

    public UserDTO updatePreferences(Long userId, UserPreferencesUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getBaseCurrency() != null && !request.getBaseCurrency().isBlank()) {
            user.setBaseCurrency(request.getBaseCurrency().trim().toUpperCase());
        }

        if (request.getHomeCity() != null) {
            String normalizedHomeCity = request.getHomeCity().trim();
            if (normalizedHomeCity.isEmpty()) {
                user.setHomeCity(null);
                user.setCity(null);
            } else {
                user.setHomeCity(normalizedHomeCity);
                user.setCity(normalizedHomeCity);
            }
        }

        return toDto(userRepository.save(user));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .city(user.getCity())
                .homeCity(user.getHomeCity() != null ? user.getHomeCity() : user.getCity())
                .phone(user.getPhone())
                .country(user.getCountry().getTitle())
                .baseCurrency(user.getBaseCurrency() != null ? user.getBaseCurrency() : "RUB")
                .username(user.getUsername())
                .role(Role.USER)
                .build();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
