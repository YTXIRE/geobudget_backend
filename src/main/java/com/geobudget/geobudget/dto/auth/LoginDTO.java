package com.geobudget.geobudget.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @Pattern(regexp = ".+", message = "Имя пользователя не должно быть пустым")
    @Schema(description = "Username", example = "username")
    private String username;

    @Pattern(regexp = ".+", message = "Пароль не должен быть пустым")
    @Schema(description = "Пароль", example = "password")
    private String password;
}
