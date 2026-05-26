package com.hotel_application.Hotel.controller;

import com.hotel_application.Hotel.dto.FavoriteHotelDto;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.service.FavoriteService;
import com.hotel_application.Hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<List<FavoriteHotelDto>> getFavorites(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        List<FavoriteHotelDto> favorites = favoriteService.getFavoriteHotels(user);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{hotelId}")
    public ResponseEntity<Map<String, String>> addFavorite(@PathVariable Long hotelId, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        favoriteService.addFavorite(user, hotelId);
        return ResponseEntity.ok(Map.of("message", "Dodano hotel do ulubionych"));
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Map<String, String>> removeFavorite(@PathVariable Long hotelId, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        favoriteService.removeFavorite(user, hotelId);
        return ResponseEntity.ok(Map.of("message", "Usunięto hotel z ulubionych"));
    }

}
