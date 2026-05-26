package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.entity.Hotel;
import com.hotel_application.Hotel.entity.ReservationStatus;
import com.hotel_application.Hotel.entity.Room;
import com.hotel_application.Hotel.repository.HotelRepository;
import com.hotel_application.Hotel.repository.ReservationRepository;
import com.hotel_application.Hotel.repository.RoomRepository;
import com.hotel_application.Hotel.response.RoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReservationRepository reservationRepository;

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

    public List<Room> getRoomsByHotelAndGuests(Long hotelId, int guests) {
        return roomRepository.findByHotelId(hotelId)
                .stream()
                .filter(r -> r.getMaxGuests() >= guests)
                .collect(Collectors.toList());
    }

    public List<RoomResponse> getRoomsWithAvailability(
            Long hotelId,
            int guests,
            LocalDate checkIn,
            LocalDate checkOut
    ) {

        List<Room> rooms = roomRepository.findByHotelId(hotelId);

        return rooms.stream()
                .filter(room -> room.getMaxGuests() >= guests)
                .map(room -> {
                    boolean hasConflict = reservationRepository
                            .existsByRoomIdAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                    room.getId(),
                                    ReservationStatus.ACTIVE,
                                    checkOut,
                                    checkIn
                            );

                    boolean available = !hasConflict;

                    return new RoomResponse(room, available);
                })
                .toList();
    }
    public BigDecimal getLowestPriceByHotelId(Long hotelId) {
        return roomRepository.findLowestPriceByHotelId(hotelId);
    }
}

