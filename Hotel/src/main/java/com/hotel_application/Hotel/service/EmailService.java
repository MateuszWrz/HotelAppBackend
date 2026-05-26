package com.hotel_application.Hotel.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String htmlContent){

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException("Błąd wysyłania email", e);
        }
    }

    private String loadTemplate(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("templates/" + fileName)) {

            if (inputStream == null) {
                throw new RuntimeException("Nie znaleziono szablonu: " + fileName);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu szablonu: " + fileName, e);
        }
    }

    public void sendPasswordResetEmail(String email, String resetLink){
        String html = loadTemplate("reset-password.html.html")
                .replace("${resetLink}", resetLink);

        sendEmail(email, "Reset hasła", html);
    }

    public void sendActivationEmail(String email, String activationLink){
        String html = loadTemplate("activation-email.html").replace("${activationLink}", activationLink);

        sendEmail(email, "Aktywacja konta", html);
    }
}
