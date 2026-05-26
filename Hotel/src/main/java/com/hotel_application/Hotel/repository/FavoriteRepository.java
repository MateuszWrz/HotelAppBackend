package com.hotel_application.Hotel.repository;

import com.hotel_application.Hotel.entity.FavoriteHotel;
import com.hotel_application.Hotel.entity.Hotel;
import com.hotel_application.Hotel.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteHotel,Long> {
    boolean existsByUserAndHotel(User user, Hotel hotel);
    @Transactional
    void deleteByUserAndHotel(User user, Hotel hotel);
    List<FavoriteHotel> findByUser(User user);
}
