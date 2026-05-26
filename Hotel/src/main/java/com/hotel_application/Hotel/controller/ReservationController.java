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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        List<Reservation> reservations = reservationService.getActiveUserReservations(user);

        List<ReservationDTO> dto = reservations.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/my/reservations/history")
    public ResponseEntity<List<ReservationDTO>> getHistoryReservations(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<Reservation> reservations = reservationService.getHistoryUserReservations(user);

        List<ReservationDTO> dto = reservations.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/reservations")
    public ResponseEntity <?> createReservation(Authentication auth, @RequestBody ReservationRequest request) {
        String email = auth.getName();
        User user = userService.findByEmail(email);

        Reservation reservation = reservationService.createReservation(
                user.getId(),
                request.getRoomId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        if(reservation == null){
            Map<String, String> body = new HashMap<>();
            body.put("error", "Pokój jest już zarezerwowany w tym terminie");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        reservationService.cancelReservation(id, user.getEmail());
        return ResponseEntity.ok().build();
    }
}
