package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.entity.Hotel;
import com.hotel_application.Hotel.entity.Room;
import com.hotel_application.Hotel.repository.HotelRepository;
import com.hotel_application.Hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public Room getRoomById(Long id){
        return roomRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Nie znaleziono pokoju"));
    }

    public Room createRoom(Long hotelId, Room room) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono hotelu"));

        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    public List<Room> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }



}
