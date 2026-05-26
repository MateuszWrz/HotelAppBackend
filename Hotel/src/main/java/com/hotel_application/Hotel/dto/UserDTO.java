package com.hotel_application.Hotel.dto;

import com.hotel_application.Hotel.entity.User;

import java.util.List;



public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String country;
    private String city;
    private String address;
    private String zipCode;
    private List<Long> favoriteRoomIds;

    public UserDTO(Long id, String email, String name, String lastName, List<Long> favoriteRoomIds) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.favoriteRoomIds = favoriteRoomIds;

    }

    public UserDTO(User user, List<Long> favoriteRoomIds) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.favoriteRoomIds = favoriteRoomIds;
        this.phoneNumber = user.getPhoneNumber();
        this.country = user.getCountry();
        this.city = user.getCity();
        this.address = user.getAddress();
        this.zipCode = user.getZipCode();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getFavoriteRoomIds() {
        return favoriteRoomIds;
    }

    public void setFavoriteRoomIds(List<Long> favoriteRoomIds) {
        this.favoriteRoomIds = favoriteRoomIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
