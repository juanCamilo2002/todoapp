package com.firstproject.todoapp.modules.users.service;

import com.firstproject.todoapp.modules.images.service.ImageService;
import com.firstproject.todoapp.modules.users.dto.*;
import com.firstproject.todoapp.modules.users.entity.User;
import com.firstproject.todoapp.modules.users.mapper.UserMapper;
import com.firstproject.todoapp.modules.users.repository.UserRepository;
import com.firstproject.todoapp.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;

    public Page<UserResponseDTO> getAllUsers(Pageable pageable, String username, String role, Boolean isActive) {
       Page<User> users = userRepository.findAllByFilters(username, role, isActive, pageable);
       return users.map(UserMapper::toResponse);
    }

    public UserResponseDTO getUserById(Long id){
        return userRepository.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserMapper.updateEntity(user, dto);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void softDeleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
    }

    public UserResponseDTO reactivateUser(Long id) throws BadRequestException {
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
        if (Boolean.TRUE.equals(user.getIsActive())){
            throw new BadRequestException("User is already active");
        }

        user.setIsActive(true);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    public UserResponseDTO uploadProfileImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        String imageUrl = imageService.uploadImage(file, "user-avatars");
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return UserMapper.toResponse(user);
    }
}
