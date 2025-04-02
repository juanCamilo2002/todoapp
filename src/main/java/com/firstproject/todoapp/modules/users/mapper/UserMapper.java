package com.firstproject.todoapp.modules.users.mapper;

import com.firstproject.todoapp.modules.users.dto.UserResponseDTO;
import com.firstproject.todoapp.modules.users.dto.UserUpdateDTO;
import com.firstproject.todoapp.modules.users.entity.User;

public class UserMapper {

    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole(), user.getIsActive(), user.getProfileImageUrl());
    }

    public static void updateEntity(User user, UserUpdateDTO dto) {
        user.setUsername(dto.username());
        user.setRole(dto.role());
        if (dto.isActive() != null) {
            user.setIsActive(dto.isActive());
        }
    }
}
