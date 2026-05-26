package com.hotel_application.Hotel.response;

import com.hotel_application.Hotel.entity.Room;

import java.math.BigDecimal;

public class RoomResponse {
    private Long id;
    private String description;
    private BigDecimal pricePerNight;
    private int maxGuests;
    private boolean available;

    public RoomResponse(Room room, boolean available) {
        this.id = room.getId();
        this.description = room.getDescription();
        this.pricePerNight = room.getPricePerNight();
        this.maxGuests = room.getMaxGuests();
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
}
