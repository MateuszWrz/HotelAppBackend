package com.hotel_application.Hotel.repository;

import com.hotel_application.Hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
List<Room> findByHotelId(Long hotelId);
List<Room> findByHotelIdAndMaxGuests(Long hotelId, int guests);

    @Query("SELECT MIN(r.pricePerNight) FROM Room r WHERE r.hotel.id = :hotelId")
    BigDecimal findLowestPriceByHotelId(@Param("hotelId") Long hotelId);
}
