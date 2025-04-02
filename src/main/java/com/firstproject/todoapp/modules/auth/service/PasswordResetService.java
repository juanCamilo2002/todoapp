package com.firstproject.todoapp.modules.auth.service;


import com.firstproject.todoapp.modules.auth.dto.ForgotPasswordRequestDTO;
import com.firstproject.todoapp.modules.auth.dto.ResetPasswordWithTokenDTO;
import com.firstproject.todoapp.modules.users.entity.User;
import com.firstproject.todoapp.modules.users.repository.UserRepository;
import com.firstproject.todoapp.shared.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final Map<String, String> resetTokens = new HashMap<>();

    public void requestPasswordReset(ForgotPasswordRequestDTO request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, user.getUsername());

        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Transactional
    public void resetPasswordWithToken(ResetPasswordWithTokenDTO request){
        String username = resetTokens.get(request.getToken());
        if (username == null) throw  new IllegalArgumentException("Invalid token");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetTokens.remove(request.getToken());
    }
}
