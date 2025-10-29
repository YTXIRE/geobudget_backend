package com.geobudget.geobudget.dto.auth;

import com.geobudget.geobudget.dto.Country;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @Pattern(regexp = ".+", message = "Имя пользователя не должно быть пустым")
    @Schema(description = "Username", example = "username")
    private String username;

    @Pattern(regexp = ".+", message = "Пароль не должен быть пустым")
    @Schema(description = "Пароль", example = "password")
    private String password;

    @Pattern(regexp = ".+", message = "Пароль не должен быть пустым")
    @Schema(description = "Пароль", example = "password")
    private String repeatPassword;

    @Pattern(regexp = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$", message = "Некорректный email")
    @Schema(description = "Email", example = "user@example.com")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{1,15}$", message = "Некорректный номер телефона")
    @Schema(description = "Телефон", example = "+79999999999")
    private String phone;

    @Pattern(regexp = ".+", message = "Город не должен быть пустым")
    @Schema(description = "Город", example = "Москва")
    private String city;

    @Min(1)
    @Schema(description = "ID страны", example = "126")
    private Long countryId;
}
