package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.docs.auth.ConfirmDoc;
import com.geobudget.geobudget.docs.auth.LoginDoc;
import com.geobudget.geobudget.docs.auth.LogoutDoc;
import com.geobudget.geobudget.docs.auth.MeDoc;
import com.geobudget.geobudget.docs.auth.RegisterDoc;
import com.geobudget.geobudget.docs.auth.TokenRefreshDoc;
import com.geobudget.geobudget.dto.UserDTO;
import com.geobudget.geobudget.dto.auth.LoginDTO;
import com.geobudget.geobudget.dto.auth.RegisterDTO;
import com.geobudget.geobudget.dto.auth.RegisterResponseDTO;
import com.geobudget.geobudget.dto.jwt.JwtAuthenticationDTO;
import com.geobudget.geobudget.service.AuthService;
import com.geobudget.geobudget.service.ConfirmationTokenService;
import com.geobudget.geobudget.service.UserJwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Данные пользователя")
public class UserController {
    private final AuthService authService;
    private final HttpServletRequest request;
    private final UserJwtService userJwtService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ResourceLoader resourceLoader;

    @RegisterDoc
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterDTO data) throws MessagingException, IOException {
        return ResponseEntity.status(201).body(authService.register(data));
    }

    @LoginDoc
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDTO> login(@Valid @RequestBody LoginDTO data) {
        return ResponseEntity.ok(authService.login(data));
    }

    @MeDoc
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(userJwtService.me(authHeader));
    }

    @LogoutDoc
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String header = request.getHeader("Authorization");
        authService.logout(header);
        return ResponseEntity.noContent().build();
    }

    @ConfirmDoc
    @GetMapping(value = "/confirm",  produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> confirm(@RequestParam String token) throws IOException {
        confirmationTokenService.confirmToken(token);

        Resource resource = resourceLoader.getResource("classpath:templates/confirmation_success.html");
        String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

    @TokenRefreshDoc
    @PostMapping("/token/refresh")
    public ResponseEntity<JwtAuthenticationDTO> refreshToken() {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(userJwtService.refreshToken(authHeader));
    }
}
