package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.Role;
import com.geobudget.geobudget.dto.UserDTO;
import com.geobudget.geobudget.dto.jwt.JwtAuthenticationDTO;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.exception.JwtAuthenticationException;
import com.geobudget.geobudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserJwtService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    public UserDTO me(String token) {
        Long userId = jwtService.extractUserId(jwtService.getToken(token));
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        return userService.toDto(user.get());
    }

    public JwtAuthenticationDTO refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new JwtAuthenticationException("Токен не найден");
        }

        Long userId = jwtService.extractUserId(jwtService.getToken(refreshToken));
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new JwtAuthenticationException("Пользователь не найден");
        }

        return jwtService.generateAccessToken(user.get().getUsername());
    }
}
