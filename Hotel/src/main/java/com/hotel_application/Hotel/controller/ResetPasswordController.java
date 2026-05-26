package com.hotel_application.Hotel.controller;

import com.hotel_application.Hotel.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ResetPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        forgotPasswordService.sendPasswordResetEmail(email);
        return ResponseEntity.ok(Map.of("message", "Na podany adres email został wysłany link resetowania hasła."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("Nieprawidłowy lub brakujący token.");
        }

        try {
            forgotPasswordService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Hasło zostało zmienione.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
