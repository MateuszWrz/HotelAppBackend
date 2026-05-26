package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.dto.LoginUserDto;
import com.hotel_application.Hotel.dto.RegisterUserDto;
import com.hotel_application.Hotel.email.EmailTemplateLoader;
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
    @Autowired EmailTemplateLoader emailTemplateLoader;

    public User register(RegisterUserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Użytkownik o podanym adresie email już istnieje");
        }


        int passwordLength = 4;
        if (userDto.getPassword().length() < passwordLength) {
            throw new RuntimeException("Hasło musi mieć conajmniej " + passwordLength +  " znaki");
        }

        User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpireAt(LocalDateTime.now().plusHours(24));
        user.setEnabled(false);
        sendActivationEmail(user);
        return userRepository.save(user);
    }

    public String authenticate(LoginUserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Nieprawidłowy email lub hasło"));
        if (!user.isEnabled()) {
            throw new RuntimeException("Konto nie zostało aktywowane. Aktywuj swoje konto");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        return jwtService.generateToken(userDto.getEmail());
    }


    public VerifyResponse verifyUser(String token) {
        User user = userRepository.findByVerificationCode(token)
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

        return new VerifyResponse("success", "Konto zostało aktywowane");
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
            sendActivationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Nie znaleziono użytkownika");
        }
    }


    public void sendActivationEmail(User user) {
        String link = "http://localhost:4200/verify?token=" + user.getVerificationCode() + "&email=" + user.getEmail();;

        String html = emailTemplateLoader.load("activation-email.html")
                .replace("${activationLink}", link);

        emailService.sendEmail(user.getEmail(), "Aktywacja konta", html);
    }


    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
