package com.hotel_application.Hotel.dto;

import com.hotel_application.Hotel.entity.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationDTO {

    private Long id;
    private String reservationNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;

    private Long roomId;
    private String roomDescription;
    private BigDecimal roomPrice;

    private String hotelName;
    private String hotelCity;

    private String userEmail;
    private String userName;

    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.reservationNumber = reservation.getReservationNumber();
        this.checkInDate = reservation.getCheckInDate();
        this.checkOutDate = reservation.getCheckOutDate();
        this.totalPrice = reservation.getTotalPrice();

        if (reservation.getRoom() != null) {
            this.roomId = reservation.getRoom().getId();
            this.roomDescription = reservation.getRoom().getDescription();
            this.roomPrice = reservation.getRoom().getPricePerNight();

            if (reservation.getRoom().getHotel() != null) {
                this.hotelName = reservation.getRoom().getHotel().getName();
                this.hotelCity = reservation.getRoom().getHotel().getCity();
            }
        }

        if (reservation.getUser() != null) {
            this.userEmail = reservation.getUser().getEmail();
            this.userName = reservation.getUser().getName() + " " + reservation.getUser().getLastName();
        }
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getHotelCity() {
        return hotelCity;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
