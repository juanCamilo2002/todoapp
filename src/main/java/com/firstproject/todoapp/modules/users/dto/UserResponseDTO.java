package com.firstproject.todoapp.modules.users.dto;

public record UserResponseDTO(
        Long id,
        String username,
        String role,
        Boolean isActive,
        String profileImageUrl
) {
}
