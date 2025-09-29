package com.hotel_application.Hotel.controller;

import com.hotel_application.Hotel.entity.Room;
import com.hotel_application.Hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/room/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id){
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @PostMapping("/hotel/new/{hotelId}")
    public ResponseEntity<Room> createRoom( @PathVariable Long hotelId, @RequestBody Room room) {
        Room createdRoom = roomService.createRoom(hotelId, room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

}
