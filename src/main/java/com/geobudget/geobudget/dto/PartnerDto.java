package com.geobudget.geobudget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDto {
    private Long id;
    private Long userId;
    private Long partnerId;
    private String partnerUsername;
    private String partnerEmail;
    private String partnerPhone;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
