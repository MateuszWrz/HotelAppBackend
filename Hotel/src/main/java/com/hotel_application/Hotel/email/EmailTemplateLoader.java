package com.hotel_application.Hotel.email;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class EmailTemplateLoader {
    public String load(String templateName) {
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("email/" + templateName)) {

            if (is == null) {
                throw new RuntimeException("Nie znaleziono szablonu: " + templateName);
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu szablonu email", e);
        }
    }
}
