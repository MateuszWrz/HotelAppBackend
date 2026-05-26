package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.email.EmailTemplateLoader;
import com.hotel_application.Hotel.entity.PasswordResetToken;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.repository.TokenRepository;
import com.hotel_application.Hotel.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;


import java.time.LocalDateTime;



@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailTemplateLoader templateLoader;

    public void sendPasswordResetEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {

            String token = UUID.randomUUID().toString();

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            resetToken.setUsed(false);

            tokenRepository.save(resetToken);

            String link = "http://localhost:4200/reset-password?token=" + token;

            String html = templateLoader.load("reset-password.html")
                    .replace("${resetLink}", link);

            emailService.sendEmail(email, "Reset hasła", html);
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Nieprawidłowy lub wygasły token resetu hasła"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token został już użyty");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token wygasł");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
