package com.hotel_application.Hotel.controller;

import com.hotel_application.Hotel.dto.UserDTO;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.service.FavoriteService;
import com.hotel_application.Hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteService favoriteService;

    /*@GetMapping("/user")
    public ResponseEntity<?> getUserProfile(Authentication authentication){
        try {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
    }*/

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUser(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        List<Long> favoriteRoomIds = favoriteService.getFavoriteHotelId(user);
        UserDTO dto = new UserDTO(user, favoriteRoomIds);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateProfile(Authentication auth, @RequestBody User updatedUser) {
        try{
            String email = auth.getName();
            User user = userService.updateUser(email, updatedUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
