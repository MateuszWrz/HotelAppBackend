package com.hotel_application.Hotel.controller;

import com.hotel_application.Hotel.entity.Hotel;
import com.hotel_application.Hotel.entity.Room;
import com.hotel_application.Hotel.service.HotelService;
import com.hotel_application.Hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels(){
        try{
            List<Hotel> hotels = hotelService.findAll();
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id){
        Hotel hotel = hotelService.findHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Hotel>> findHotelsByCity(@PathVariable String city){
        try{
            List <Hotel> hotels = hotelService.findByCity(city);
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHotel(@PathVariable Long hotelId){
        List<Room> rooms = roomService.getRoomsByHotel(hotelId);
        return ResponseEntity.ok(rooms);
    }


    @PostMapping
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
        try{
            Hotel saved = hotelService.createHotel(hotel);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
