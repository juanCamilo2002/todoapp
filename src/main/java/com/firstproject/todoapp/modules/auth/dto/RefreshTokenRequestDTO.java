package com.firstproject.todoapp.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    @NotBlank(message = "Refresh Token must not be null or empty")
    private String refreshToken;
}
