package com.firstproject.todoapp.modules.auth.service;

import com.cloudinary.api.exceptions.BadRequest;
import com.firstproject.todoapp.modules.auth.dto.*;
import com.firstproject.todoapp.modules.users.entity.User;
import com.firstproject.todoapp.modules.users.repository.UserRepository;
import com.firstproject.todoapp.shared.exceptions.ConflictException;
import com.firstproject.todoapp.shared.exceptions.EntityNotFoundException;
import com.firstproject.todoapp.shared.exceptions.InvalidCredentialsException;
import com.firstproject.todoapp.shared.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ConflictException("User already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role("ROLE_USER")
                .isActive(true)
                .build();


        userRepository.save(user);

        String accessToken = jwtService.generateToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public LoginResponseDTO login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Credentials Invalids");
        }

        String accessToken = jwtService.generateToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return new LoginResponseDTO(accessToken, refreshToken) ;
    }

    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO request){
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        System.out.println("Refrescando token");

        if (!jwtService.isRefreshTokenValid(refreshToken, username)){
            throw new InvalidTokenException("Invalid Refresh Token");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String AccessToken = jwtService.generateToken(user.getUsername());

        return new RefreshTokenResponseDTO(AccessToken, refreshToken);
    }
}