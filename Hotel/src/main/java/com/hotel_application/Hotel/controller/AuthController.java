package com.hotel_application.Hotel.controller;

import com.hotel_application.Hotel.dto.LoginUserDto;
import com.hotel_application.Hotel.dto.RegisterUserDto;
import com.hotel_application.Hotel.dto.VerifyUserDto;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.responses.VerifyResponse;
import com.hotel_application.Hotel.service.AuthService;
import com.hotel_application.Hotel.service.JWTService;
import com.hotel_application.Hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDto userDto) {
        try {
            authService.register(userDto);
            return ResponseEntity.ok("Na podany adres email został wysłany kod weryfikacyjny");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Throwable e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto userDto) {
        try {
            String token = authService.authenticate(userDto);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nieprawidłowy email lub hasło");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verifyUser(@RequestBody VerifyUserDto dto) {
        try {
            VerifyResponse response = authService.verifyUser(dto.getToken(), dto.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new VerifyResponse("error", e.getMessage()));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try{
            authService.resendVerificationCode(email);
            return ResponseEntity.ok("Kod został wysłany na twój adres email");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}


