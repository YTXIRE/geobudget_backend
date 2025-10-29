package com.geobudget.geobudget.dto.jwt;

import lombok.Data;

@Data
public class JwtAuthenticationDTO {
    private String token;
    private String refreshToken;
}
