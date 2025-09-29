package com.hotel_application.Hotel.request;

import java.time.LocalDate;

public class ReservationRequest {

    private int userId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public Long getRoomId() {
        return roomId;
    }

    public int getUserId() {
        return userId;
    }
}
