package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.entity.Hotel;
import com.hotel_application.Hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomService roomService;

    public Hotel createHotel(Hotel hotel){
        return hotelRepository.save(hotel);
    }

    public List<Hotel> findByCity(String city){
        return hotelRepository.findByCity(city);
    }

    public List<Hotel> findAll(){
        return hotelRepository.findAll();
    }

    public Hotel findHotelById(Long id){
        return hotelRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Nie znaleziono hotelu"));

    }

    public Hotel updateHotel(Long id, Hotel hotelDetails) {
        Hotel hotel = findHotelById(id);

        hotel.setName(hotelDetails.getName());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setCity(hotelDetails.getCity());
        hotel.setPostalCode(hotelDetails.getPostalCode());
        hotel.setNumberPhone(hotelDetails.getNumberPhone());

        return hotelRepository.save(hotel);
    }

//    public List<Room> getAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
//        if (checkIn.isAfter(checkOut)) {
//            throw new IllegalArgumentException("Check-in date must be before check-out date");
//        }
//        if (checkIn.isBefore(LocalDate.now())) {
//            throw new IllegalArgumentException("Check-in date cannot be in the past");
//        }
//
//        return roomService.getAvailableRooms(hotelId, checkIn, checkOut);
//    }
}
