package com.firstproject.todoapp.modules.auth.service;

import com.firstproject.todoapp.modules.auth.dto.ChangePasswordRequestDTO;
import com.firstproject.todoapp.modules.users.entity.User;
import com.firstproject.todoapp.modules.users.repository.UserRepository;
import com.firstproject.todoapp.shared.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChangePasswordServiceTest {

    @InjectMocks
    private ChangePasswordService passwordService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        // Arrange
        String username = "usuarioTest";
        String currentPassword = "oldPassword";
        String newPassword = "newPassword123";
        String encodedNewPassword = "encodedNewPassword123";

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);

        User mockUser = User.builder()
                .id(1L)
                .username(username)
                .password(passwordEncoder.encode(currentPassword))
                .role("ROLE_USER")
                .build();

        when(authentication.getPrincipal()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(currentPassword, mockUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        // Act
        passwordService.changePassword(request);

        // Assert
        assertEquals(encodedNewPassword, mockUser.getPassword());
        verify(userRepository).save(mockUser);
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        // Arrange
        String username = "usuarioTest";
        String wrongPassword = "wrongPassword";
        String currentPassword = "oldPassword";

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setCurrentPassword(wrongPassword);
        request.setNewPassword("newPassword123");

        User mockUser = User.builder()
                .id(1L)
                .username(username)
                .password(passwordEncoder.encode(currentPassword))
                .role("ROLE_USER")
                .build();

        when(authentication.getPrincipal()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(wrongPassword, mockUser.getPassword())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> passwordService.changePassword(request));

        assertEquals("Current password is incorrect", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String username = "notExistingUser";

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setCurrentPassword("oldPass");
        request.setNewPassword("newPass123");

        when(authentication.getPrincipal()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> passwordService.changePassword(request));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
