package com.geobudget.geobudget.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "revoked_tokens")
public class RevokedToken {
    @Id
    @Column(name = "token", length = 512)
    private String token;

    @Column(name = "revocation_date", nullable = false)
    private LocalDateTime revocationDate;
}