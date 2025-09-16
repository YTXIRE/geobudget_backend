package com.geobudget.geobudget.service;

import com.geobudget.geobudget.entity.ConfirmationToken;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.ConfirmationTokenRepository;
import com.geobudget.geobudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    public void saveConfirmationToken(String token, User user) {
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .user(user)
                .token(token)
                .createdAt(LocalDateTime.now())
                .build();
        confirmationTokenRepository.save(confirmationToken);
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Неверный токен"));
        User user = confirmationToken.getUser();
        user.setActive(true);
        userRepository.save(user);
        confirmationTokenRepository.delete(confirmationToken);
    }
}
