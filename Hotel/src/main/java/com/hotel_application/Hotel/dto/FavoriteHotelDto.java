package com.hotel_application.Hotel.dto;

public class FavoriteHotelDto {
    private Long id;
    private String name;
    private String address;

    public FavoriteHotelDto(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
}
