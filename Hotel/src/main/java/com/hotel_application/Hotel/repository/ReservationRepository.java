package com.hotel_application.Hotel.repository;

import com.hotel_application.Hotel.entity.Reservation;
import com.hotel_application.Hotel.entity.ReservationStatus;
import com.hotel_application.Hotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation> findByUser(User user);
    Optional<Reservation> findById(Long id);
    List<Reservation> findByUserAndStatus(User user, ReservationStatus status);
    List<Reservation> findByUserAndStatusAndCheckOutDateGreaterThanEqual(User user, ReservationStatus status, LocalDate date
    );

    List<Reservation> findByUserAndCheckOutDateBeforeOrUserAndStatus(User user1, LocalDate date, User user2, ReservationStatus status
    );

    boolean existsByRoomIdAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Long roomId,
            ReservationStatus status,
            LocalDate checkOut,
            LocalDate checkIn
    );
    List<Reservation> findByStatusAndCheckOutDateBefore(
            ReservationStatus status,
            LocalDate date
    );
}
