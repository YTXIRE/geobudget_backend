package com.geobudget.geobudget.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tag_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
