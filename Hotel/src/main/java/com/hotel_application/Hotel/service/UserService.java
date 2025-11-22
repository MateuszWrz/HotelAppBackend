package com.hotel_application.Hotel.service;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
