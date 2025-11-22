package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.dto.LoginUserDto;
import com.hotel_application.Hotel.dto.RegisterUserDto;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.repository.UserRepository;
import com.hotel_application.Hotel.responses.VerifyResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;

    public User register(RegisterUserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Użytkownik o podanym adresie email już istnieje");
        }
        User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));

        if (user.getPassword().length() < 4) {
            throw new RuntimeException("Hasło musi mieć conajmniej 4 znaki");
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpireAt(LocalDateTime.now().plusHours(24));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    public String authenticate(LoginUserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Nieprawidłowy email lub hasło"));
        if (!user.isEnabled()) {
            throw new RuntimeException("Konto nie zostało zweryfikowane. Zweryfikuj swoje konto");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        return jwtService.generateToken(userDto.getEmail());
    }


    public VerifyResponse verifyUser(String email, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        if (user.isEnabled()) {
            return new VerifyResponse("info", "Konto jest już aktywowane");
        }

        if (!token.equals(user.getVerificationCode())) {
            return new VerifyResponse("error", "Nieprawidłowy token weryfikacyjny");
        }

        if (user.getVerificationCodeExpireAt().isBefore(LocalDateTime.now())) {
            return new VerifyResponse("error", "Kod weryfikacyjny wygasł");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpireAt(null);
        userRepository.save(user);

        return new VerifyResponse("success", "Konto zostało zweryfikowane");
    }


    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Konto zostało już aktywowane");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpireAt(LocalDateTime.now().plusHours(24));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Nie znaleziono użytkownika");
        }
    }


    public void sendVerificationEmail(User user) {
        String subject = "Weryfikacja konta";

        String verificationLink = String.format("http://localhost:4200/verify?email=%s&token=%s",
                URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8),
                URLEncoder.encode(user.getVerificationCode(), StandardCharsets.UTF_8)
        );

        String htmlMessage = """
                <html>
                    <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                        <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 30px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
                            <h2 style="color: #333;">Witaj w naszej aplikacji!</h2>
                            <p style="font-size: 16px; color: #555;">
                                Aby aktywować swoje konto, kliknij poniższy przycisk:
                            </p>
                            <p style="text-align: center; margin: 30px 0;">
                                <a href="%s" style="background-color: #007bff; color: #ffffff; text-decoration: none; padding: 12px 24px; border-radius: 5px; font-weight: bold;">
                                    Zweryfikuj konto
                                </a>
                            </p>
                            <p style="font-size: 14px; color: #777;">
                                Jeśli nie zakładałeś konta, zignoruj tę wiadomość.<br>
                                Link wygaśnie po 24 godzinach.
                            </p>
                        </div>
                    </body>
                </html>
                """.formatted(verificationLink);

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
