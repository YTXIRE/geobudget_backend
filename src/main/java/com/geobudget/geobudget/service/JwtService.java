package com.geobudget.geobudget.service;


import com.geobudget.geobudget.config.JwtProperties;
import com.geobudget.geobudget.dto.TokenType;
import com.geobudget.geobudget.dto.UserDTO;
import com.geobudget.geobudget.dto.jwt.JwtAuthenticationDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private final UserService userService;

    public JwtAuthenticationDTO generateAccessToken(String email) {
        JwtAuthenticationDTO jwtDto = generateToken(email);
        jwtDto.setRefreshToken(generateJwtToken(email, TokenType.REFRESH_TOKEN));
        log.info("JwtService.generateAccessToken: tokens generated for user={}", email);
        return jwtDto;
    }

    public JwtAuthenticationDTO generateRefreshToken(String email, String refreshToken) {
        JwtAuthenticationDTO jwtDto = generateToken(email);
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JwtException", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JwtException", ex);
        } catch (MalformedJwtException ex) {
            log.error("Malformed JwtException", ex);
        } catch (SecurityException ex) {
            log.error("Security Exception", ex);
        } catch (Exception ex) {
            log.error("Invalid token", ex);
        }
        return false;
    }

    private String generateJwtToken(String email, TokenType tokenType) {
        Date date;
        if (tokenType == TokenType.ACCESS_TOKEN) {
            date = Date.from(LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        } else {
            date = Date.from(LocalDateTime.now().plusMonths(40).atZone(ZoneId.systemDefault()).toInstant());
        }

        UserDTO user = userService.getUserByUsername(email);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("roles", List.of(user.getRole()));

        return Jwts.builder()
                .subject(email)
                .expiration(date)
                .claims(claims)
                .signWith(getSignInKey())
                .compact();
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Object idObj = claims.get("id");
            if (idObj instanceof Number) {
                return ((Number) idObj).longValue();
            } else if (idObj instanceof String) {
                return Long.parseLong((String) idObj);
            }
        } catch (Exception ex) {
            log.error("Failed to extract user id from token", ex);
        }
        return null;
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Object rolesObject = claims.get("roles");
        if (rolesObject instanceof List<?>) {
            List<?> roles = (List<?>) rolesObject;
            return roles.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private JwtAuthenticationDTO generateToken(String email) {
        JwtAuthenticationDTO jwtDto = new JwtAuthenticationDTO();
        jwtDto.setToken(generateJwtToken(email, TokenType.ACCESS_TOKEN));
        return jwtDto;
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public String getToken(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
