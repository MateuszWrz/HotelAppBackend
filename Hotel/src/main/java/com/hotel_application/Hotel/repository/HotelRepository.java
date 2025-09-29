package com.hotel_application.Hotel.repository;

import com.hotel_application.Hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCity(String city);

    Optional<Hotel> findById(Long id);
}
