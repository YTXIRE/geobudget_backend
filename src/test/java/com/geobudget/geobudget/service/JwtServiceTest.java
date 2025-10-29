package com.geobudget.geobudget.service;

import com.geobudget.geobudget.config.JwtProperties;
import com.geobudget.geobudget.dto.UserDTO;
import com.geobudget.geobudget.dto.jwt.JwtAuthenticationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtServiceTest {
    private JwtProperties jwtProperties;
    private UserService userService;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        // 64 hex chars base64 decoded is not correct; we need a base64 string; use simple base64
        jwtProperties.setSecret("YWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWE=");
        userService = Mockito.mock(UserService.class);
        jwtService = new JwtService(jwtProperties, userService);
    }

    @Test
    void generateAccessToken_returnsTokens() {
        UserDTO user = UserDTO.builder().id(1L).role(com.geobudget.geobudget.dto.Role.USER).build();
        when(userService.getUserByUsername("user@example.com")).thenReturn(user);
        JwtAuthenticationDTO dto = jwtService.generateAccessToken("user@example.com");
        assertNotNull(dto.getToken());
        assertNotNull(dto.getRefreshToken());
    }

    @Test
    void validateJwtToken_returnsFalseOnInvalid() {
        assertFalse(jwtService.validateJwtToken("invalid.token"));
    }
}


