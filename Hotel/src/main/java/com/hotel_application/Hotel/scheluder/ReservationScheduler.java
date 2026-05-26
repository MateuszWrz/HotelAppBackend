package com.hotel_application.Hotel.scheluder;

import com.hotel_application.Hotel.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {

    private final ReservationService reservationService;

    @Autowired
    public ReservationScheduler(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(cron = "59 59 23 * * *")
    public void updateCompletedReservations() {
        reservationService.updateCompletedReservations();
    }
}
