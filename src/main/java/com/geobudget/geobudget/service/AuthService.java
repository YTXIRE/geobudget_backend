package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.auth.LoginDTO;
import com.geobudget.geobudget.dto.auth.RegisterDTO;
import com.geobudget.geobudget.dto.auth.RegisterResponseDTO;
import com.geobudget.geobudget.dto.jwt.JwtAuthenticationDTO;
import com.geobudget.geobudget.entity.RevokedToken;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.CountryRepository;
import com.geobudget.geobudget.repository.RevokedTokenRepository;
import com.geobudget.geobudget.repository.UserRepository;
import com.geobudget.geobudget.utils.TokenUtil;
import com.geobudget.geobudget.validator.AuthValidator;
import com.geobudget.geobudget.validator.CountryValidator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthValidator authValidator;
    private final CountryValidator countryValidator;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RevokedTokenRepository revokedTokenRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    public RegisterResponseDTO register(RegisterDTO dto) throws MessagingException, IOException {
        log.info("Регистрация пользователя " + dto);
        authValidator.checkRepeatPassword(dto.getPassword(), dto.getRepeatPassword());
        authValidator.checkLoginUnique(dto.getUsername());
        authValidator.checkEmailUnique(dto.getEmail());
        authValidator.checkPhone(dto.getPhone());

        countryValidator.checkCountryExistence(dto.getCountryId());

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .city(dto.getCity())
                .country(countryRepository.findById(dto.getCountryId()).orElseThrow())
                .build();

        userRepository.save(user);

        String token = TokenUtil.generateToken();
        confirmationTokenService.saveConfirmationToken(token, user);

        emailService.sendConfirmationEmail(user.getEmail(), token);

        log.info("Пользователь " + user.getUsername() + " зарегистрирован");

        return RegisterResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .city(user.getCity())
                .country(user.getCountry().getTitle())
                .phone(user.getPhone())
                .build();
    }

    public JwtAuthenticationDTO login(LoginDTO dto) {
        authValidator.checkLogin(dto.getUsername());
        authValidator.checkPassword(dto.getPassword());
        authValidator.checkIsActive(dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), userRepository.findByUsername(dto.getUsername()).orElseThrow().getPassword())) {
            log.info("Неверный логин или пароль");
            throw new IllegalArgumentException("Неверный логин или пароль");
        }

        return jwtService.generateAccessToken(dto.getUsername());
    }

    public void logout(String token) {
        String jwtToken = jwtService.getToken(token);
        if (!revokedTokenRepository.existsByToken(jwtToken)) {
            RevokedToken revokedToken = RevokedToken.builder()
                    .token(jwtToken)
                    .revocationDate(LocalDateTime.now())
                    .build();
            revokedTokenRepository.save(revokedToken);
        }
    }

    public String generateTemporaryPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String tempPassword = TokenUtil.generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
        return tempPassword;
    }
}
