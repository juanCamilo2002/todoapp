package com.firstproject.todoapp.modules.users.controller;

import com.firstproject.todoapp.modules.users.dto.UserResponseDTO;
import com.firstproject.todoapp.modules.users.dto.UserUpdateDTO;
import com.firstproject.todoapp.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for user management")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Endpoint to get all users by applying filters",
            description = "This returns the users data by applying pagination."
    )
    @GetMapping
    public Page<UserResponseDTO> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean isActive
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable, username, role, isActive);
    }

    @Operation(
            summary = "Endpoint for retrieving data from a user.",
            description = "This returns the data for a user."
    )
    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Endpoint for updating user data",
            description = "This returns the data of an updated user"
    )
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO request) {
        return userService.updateUser(id, request);
    }

    @Operation(
            summary = "Endpoint to deactivate a user",
            description = "This returns a user deactivation confirmation response."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
    }

    @Operation(
            summary = "Endpoint to reactivate a user",
            description = "This returns a user reactivation confirmation response."
    )
    @PatchMapping("/{id}/reactivate")
    public UserResponseDTO reactivateUser(@PathVariable Long id) throws BadRequestException {
        return userService.reactivateUser(id);
    }

    @Operation(
            summary = "Endpoint to upload the user's profile image",
            description = "This returns the updated user data"
    )
    @PostMapping("/{userId}/upload-profile-image")
    public UserResponseDTO uploadProfileImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {
        return userService.uploadProfileImage(userId, file);
    }

}
