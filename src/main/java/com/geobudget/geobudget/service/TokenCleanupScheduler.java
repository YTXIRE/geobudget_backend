package com.geobudget.geobudget.service;

import com.geobudget.geobudget.repository.RevokedTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {
    private final RevokedTokenRepository revokedTokenRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        revokedTokenRepository.deleteExpiredTokens(now);
    }
}

