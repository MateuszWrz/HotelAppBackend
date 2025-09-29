package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.entity.Reservation;
import com.hotel_application.Hotel.entity.Room;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.repository.ReservationRepository;
import com.hotel_application.Hotel.repository.RoomRepository;
import com.hotel_application.Hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;


@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    public Reservation createReservation(Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut) {

        User user = userService.findById(userId);
        Room room = roomService.getRoomById(roomId);


        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setReservationNumber(generateReservationNumber());
        reservation.setTotalPrice(calculateTotalPrice(room.getPricePerNight(), checkIn, checkOut));

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getUserReservation(User user){
        return reservationRepository.findByUser(user);
    }

    private double calculateTotalPrice(BigDecimal pricePerNight, LocalDate checkIn, LocalDate checkOut) {
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricePerNight.multiply(BigDecimal.valueOf(numberOfNights)).doubleValue();
    }

    private String generateReservationNumber() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


}
