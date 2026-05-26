package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.dto.FavoriteHotelDto;
import com.hotel_application.Hotel.entity.FavoriteHotel;
import com.hotel_application.Hotel.entity.Hotel;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.repository.FavoriteRepository;
import com.hotel_application.Hotel.repository.HotelRepository;
import com.hotel_application.Hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public void addFavorite(User user, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel nie istnieje"));

        if (favoriteRepository.existsByUserAndHotel(user, hotel)) {
            throw new RuntimeException("Hotel już jest w ulubionych");
        }

        FavoriteHotel favorite = new FavoriteHotel();
        favorite.setUser(user);
        favorite.setHotel(hotel);

        favoriteRepository.save(favorite);
    }

    public void removeFavorite(User user, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel nie istnieje"));

        favoriteRepository.deleteByUserAndHotel(user, hotel);
    }

    public List<Long> getFavoriteHotelId(User user) {
        return favoriteRepository.findByUser(user).stream()
                .map(fav -> fav.getHotel().getId())
                .collect(Collectors.toList());
    }

    public List<FavoriteHotelDto> getFavoriteHotels(User user) {
        return favoriteRepository.findByUser(user)
                .stream()
                .map(fav -> new FavoriteHotelDto(
                        fav.getHotel().getId(),
                        fav.getHotel().getName(),
                        fav.getHotel().getAddress()
                ))
                .collect(Collectors.toList());
    }
}

