package com.geobudget.geobudget.service;

import com.geobudget.geobudget.entity.PasswordResetToken;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.exception.JwtAuthenticationException;
import com.geobudget.geobudget.repository.PasswordResetTokenRepository;
import com.geobudget.geobudget.utils.TokenUtil;
import com.geobudget.geobudget.validator.AuthValidator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final UserService userService;
    private final AuthValidator authValidator;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AuthService authService;

    public void createToken(String email) throws MessagingException, IOException {
        authValidator.checkExistingEmail(email);

        User user = userService.findByEmail(email);
        String token = TokenUtil.generateToken();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetConfirmationEmail(user.getEmail(), token);
    }

    public void validateTokenAndCreateTempPassword(String token) throws MessagingException, IOException {
        PasswordResetToken data = passwordResetTokenRepository.getUserByToken(token);
        if (data == null) {
            throw new JwtAuthenticationException("Неверный или просроченный токен");
        }

        String tempPassword = authService.generateTemporaryPassword(data.getUser().getEmail());
        passwordResetTokenRepository.delete(data);
        emailService.sendTemporaryPasswordEmail(data.getUser().getEmail(), tempPassword);
    }
}
