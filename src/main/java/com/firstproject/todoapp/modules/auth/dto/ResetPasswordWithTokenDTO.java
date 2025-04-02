package com.firstproject.todoapp.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordWithTokenDTO {
    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "newPassword is required")
    @Size(min=8, message = "newPassword must be al least 8 characters long")
    private String newPassword;
}
