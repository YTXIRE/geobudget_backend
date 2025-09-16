package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.RevokedToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RevokedTokenRepository extends CrudRepository<RevokedToken, Long> {
    boolean existsByToken(String token);

    @Modifying
    @Query("DELETE FROM RevokedToken rt WHERE rt.revocationDate < :expirationTime")
    void deleteExpiredTokens(LocalDateTime expirationTime);
}
