package com.firstproject.todoapp.modules.auth.controller;

import com.firstproject.todoapp.modules.auth.dto.*;
import com.firstproject.todoapp.modules.auth.service.AuthService;
import com.firstproject.todoapp.modules.auth.service.ChangePasswordService;
import com.firstproject.todoapp.modules.auth.service.PasswordResetService;
import com.firstproject.todoapp.modules.users.dto.UserResponseDTO;
import com.firstproject.todoapp.modules.users.entity.User;
import com.firstproject.todoapp.modules.users.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for managing user authentication")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ChangePasswordService changePasswordService;
    private final PasswordResetService passwordResetService;

    @Operation(
            summary = "Endpoint for registering users",
            description = "This returns data for the registered user."
    )
    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            summary = "Endpoint for login user",
            description = "This returns access and refresh token for user's session"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "Endpoint for retrieve profile of user authenticated",
            description = "This returns the user's session profile data."
    )
    @GetMapping("/profile")
    public UserResponseDTO getProfile(@AuthenticationPrincipal User user) {
        return UserMapper.toResponse(user);
    }

    @Operation(
            summary = "Endpoint to change the password of the authenticated user",
            description = "This returns a password change confirmation message"
    )
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
        changePasswordService.changePassword(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Password changed successfully!"));
    }

    @Operation(
            summary = "Endpoint for refresh access token session",
            description = "This returns new access and refresh token authentication"
    )
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        RefreshTokenResponseDTO response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Endpoint for sending email reset email",
            description = "This returns a confirmation message to reset the user's password."
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        passwordResetService.requestPasswordReset(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Password reset email sent successfully!"));
    }

    @Operation(
            summary = "Endpoint to change password with data received in the email ",
            description = "This returns a password change confirmation message"
    )
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordWithTokenDTO request) {
        passwordResetService.resetPasswordWithToken(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Password reset successfully!"));
    }
}