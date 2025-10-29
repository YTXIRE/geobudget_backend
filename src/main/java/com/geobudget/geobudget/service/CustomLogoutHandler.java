package com.geobudget.geobudget.service;

import com.geobudget.geobudget.entity.RevokedToken;
import com.geobudget.geobudget.repository.RevokedTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final RevokedTokenRepository revokedTokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        if (!revokedTokenRepository.existsByToken(token)) {
            RevokedToken revoked = new RevokedToken();
            revoked.setToken(token);
            revoked.setRevocationDate(LocalDateTime.now());
            revokedTokenRepository.save(revoked);
        }
    }
}

