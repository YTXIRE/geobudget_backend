package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.docs.passwordReset.ResetConfirmDoc;
import com.geobudget.geobudget.docs.passwordReset.ResetRequestDoc;
import com.geobudget.geobudget.dto.password.Reset;
import com.geobudget.geobudget.service.PasswordResetTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Reset Password", description = "Работа с паролем")
@Validated
public class PasswordResetController {
    private final PasswordResetTokenService tokenService;
    private final ResourceLoader resourceLoader;

    @ResetRequestDoc
    @PostMapping("/password/reset-request")
    public ResponseEntity<Reset> resetRequest(@RequestParam String email) throws MessagingException, IOException {
        tokenService.createToken(email);
        return ResponseEntity.ok(Reset.builder().message("Ссылка для подтверждения сброса пароля отправлена на email").build());
    }

    @ResetConfirmDoc
    @GetMapping("/password/reset-confirm")
    public ResponseEntity<String> confirmReset(@RequestParam String token) throws MessagingException, IOException {
        tokenService.validateTokenAndCreateTempPassword(token);

        Resource resource = resourceLoader.getResource("classpath:templates/send_temp_password.html");
        String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }
}
