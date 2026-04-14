package com.geobudget.geobudget.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "budgets")
@EntityListeners(AuditingEntityListener.class)
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "period_type", nullable = false, length = 24)
    private String periodType;

    @Column(name = "amount_limit", nullable = false, precision = 14, scale = 2)
    private BigDecimal amountLimit;

    @Column(name = "base_currency", nullable = false, length = 3)
    private String baseCurrency;

    @Column(name = "scope_type", nullable = false, length = 24)
    private String scopeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Category group;

    @Column(length = 160)
    private String region;

    @Column(length = 120)
    private String city;

    @Column(length = 120)
    private String country;

    @Column(name = "location_type", length = 24)
    private String locationType;

    @Column(name = "location_value", length = 120)
    private String locationValue;

    @Column(name = "starts_at", nullable = false)
    private LocalDate startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDate endsAt;

    @Column(name = "warning_threshold", nullable = false, precision = 5, scale = 2)
    private BigDecimal warningThreshold;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "partner_id")
    private Long partnerId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
