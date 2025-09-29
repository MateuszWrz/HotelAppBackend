package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;


    public User register(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Użytkownik o podanym adresie email już istnieje");
        }
        if(user.getPassword().length()<4){
            throw new RuntimeException("Hasło musi mieć conajmniej 4 znaki");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public String verify(User user) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Niepoprawny email lub hasło");
        }
        return jwtService.generateToken(user.getEmail());
    }


    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->
                new RuntimeException("Nie znaleziono użytkownika"));
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Nie znaleziono użytkownika"));
    }

    @Transactional
    public User updateUser(String email, User updatedUser) {
        User existingUser = findByEmail(email);

        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (updatedUser.getCountry() != null) {
            existingUser.setCountry(updatedUser.getCountry());
        }
        if (updatedUser.getCity() != null) {
            existingUser.setCity(updatedUser.getCity());
        }
        if (updatedUser.getAddress() != null) {
            existingUser.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getZipCode() != null) {
            existingUser.setZipCode(updatedUser.getZipCode());
        }

        return userRepository.save(existingUser);
    }
    
}
