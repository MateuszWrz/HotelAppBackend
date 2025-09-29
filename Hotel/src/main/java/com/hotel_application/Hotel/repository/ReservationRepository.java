package com.hotel_application.Hotel.repository;

import com.hotel_application.Hotel.entity.Reservation;
import com.hotel_application.Hotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findByUser(User user);
}
