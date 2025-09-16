package com.geobudget.geobudget.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geobudget.geobudget.exception.JwtAuthenticationException;
import com.geobudget.geobudget.exception.dto.ErrorResponse;
import com.geobudget.geobudget.repository.RevokedTokenRepository;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.CustomUserService;
import com.geobudget.geobudget.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserService customUserService;
    private final RevokedTokenRepository revokedTokenRepository;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        try {
            if (token != null) {
                validateToken(token);
                setCustomUserDetailsToSecurityContextHolder(token);
            }
            filterChain.doFilter(request, response);

        } catch (JwtAuthenticationException ex) {
            // Можно логировать
            logger.warn("JWT exception: " + ex.getMessage());

            // Отправляем клиенту JSON в нужном формате
            sendErrorResponse(response, ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            // На всякий случай обработка других исключений
            logger.error("Unexpected exception in JwtFilter", ex);
            sendErrorResponse(response, "Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void validateToken(String token) {
        try {
            if (!jwtService.validateJwtToken(token)) {
                throw new JwtAuthenticationException("JWT токен не валидный");
            }

            String tokenRaw = jwtService.getToken(token);
            if (revokedTokenRepository.existsByToken(tokenRaw) || !jwtService.validateJwtToken(tokenRaw)) {
                throw new JwtAuthenticationException("JWT токен отозван или недействителен");
            }

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new JwtAuthenticationException("JWT токен просрочен");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new JwtAuthenticationException("JWT токен поврежден");
        } catch (io.jsonwebtoken.SignatureException e) {
            throw new JwtAuthenticationException("JWT токен имеет неправильную подпись");
        } catch (Exception e) {
            throw new JwtAuthenticationException("Ошибка при обработке JWT токена: " + e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(new ErrorResponse(
                "JWT токен не прошёл проверку",
                message
        )));
        response.getWriter().flush();
    }

    private void setCustomUserDetailsToSecurityContextHolder(String token) {
        String email = jwtService.getEmailFromToken(token);
        List<String> roles = jwtService.getRolesFromToken(token);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        CustomUserDetails customUserDetails = customUserService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(@NotNull HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
