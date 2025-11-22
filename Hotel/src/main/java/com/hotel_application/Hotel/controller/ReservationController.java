package com.hotel_application.Hotel.controller;

import com.hotel_application.Hotel.entity.Reservation;
import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.dto.ReservationDTO;
import com.hotel_application.Hotel.request.ReservationRequest;
import com.hotel_application.Hotel.service.ReservationService;
import com.hotel_application.Hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReservationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;


    @GetMapping("/my/reservations/active")
    public ResponseEntity<List<ReservationDTO>> getUserReservations(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<Reservation> reservations = reservationService.getUserReservation(user);

        List<ReservationDTO> dto
                = reservations.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/my/reservations/history")
    public ResponseEntity<List<ReservationDTO>> getHistoryReservations(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<Reservation> reservations = reservationService.getUserReservation(user);

        List<ReservationDTO> dto = reservations.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/reservations")
    public ResponseEntity <Reservation> createReservation(Authentication auth, @RequestBody ReservationRequest request) {
        String email = auth.getName();
        User user = userService.findByEmail(email);

        Reservation reservation = reservationService.createReservation(
                user.getId(),
                request.getRoomId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
}
