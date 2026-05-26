package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.entity.Reservation;
import com.hotel_application.Hotel.entity.ReservationStatus;
import com.hotel_application.Hotel.entity.Room;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.repository.ReservationRepository;
import com.hotel_application.Hotel.repository.RoomRepository;
import com.hotel_application.Hotel.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public Reservation createReservation(Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut) {

        if (checkOut.isBefore(checkIn) || checkOut.equals(checkIn)) {
            throw new RuntimeException("Data wymeldowania musi być późniejsza niż zameldowania");
        }

        boolean conflict = reservationRepository.existsByRoomIdAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                roomId,
                ReservationStatus.ACTIVE,
                checkOut,
                checkIn
        );

        if (conflict) {
            return null;
        }

        User user = userService.findById(userId);
        Room room = roomService.getRoomById(roomId);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setReservationNumber(generateReservationNumber());
        reservation.setTotalPrice(calculateTotalPrice(room.getPricePerNight(), checkIn, checkOut));
        reservation.setStatus(ReservationStatus.ACTIVE);

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId, String userEmail) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono rezerwacji"));

        if (!reservation.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Nie masz uprawnień do anulowania tej rezerwacji");
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInDate = reservation.getCheckInDate();

        long daysUntilCheckIn = ChronoUnit.DAYS.between(today, checkInDate);

        if (daysUntilCheckIn < 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rezerwację można anulować maksymalnie 2 dni przed zameldowaniem"
            );
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
    }

    public List<Reservation> getActiveUserReservations(User user){
        return reservationRepository.findByUserAndStatusAndCheckOutDateGreaterThanEqual(
                        user,
                        ReservationStatus.ACTIVE,
                        LocalDate.now()
                );
    }

    public List<Reservation> getHistoryUserReservations(User user){
        return reservationRepository.findByUserAndCheckOutDateBeforeOrUserAndStatus(
                        user,
                        LocalDate.now(),
                        user,
                        ReservationStatus.CANCELED
                );
    }

    private double calculateTotalPrice(BigDecimal pricePerNight, LocalDate checkIn, LocalDate checkOut) {
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricePerNight.multiply(BigDecimal.valueOf(numberOfNights)).doubleValue();
    }

    @Transactional
    public void updateCompletedReservations() {
        List<Reservation> expiredReservations =
                reservationRepository.findByStatusAndCheckOutDateBefore(
                        ReservationStatus.ACTIVE,
                        LocalDate.now()
                );

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(ReservationStatus.COMPLETED);
        }

        reservationRepository.saveAll(expiredReservations);
    }

    private String generateReservationNumber() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


}
