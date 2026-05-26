package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.dto.HotelWithPriceDTO;
import com.hotel_application.Hotel.entity.Hotel;
import com.hotel_application.Hotel.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hotel_application.Hotel.entity.Room;


import java.math.BigDecimal;
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
        List<Hotel> hotels = hotelRepository.findByCity(city);
        hotels.forEach(hotel -> {
            BigDecimal minPrice = hotel.getRooms().stream()
                    .map(Room::getPricePerNight)
                    .min(BigDecimal::compareTo)
                    .orElse(null);
            hotel.setLowestPrice(minPrice);
        });
        return hotels;
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

    public List<String> searchCities(String query) {

        if (query == null || query.length() < 2) {
            return List.of();
        }

        Pageable limit = PageRequest.of(0, 10);

        return hotelRepository
                .findDistinctByCityStartingWithIgnoreCase(query, limit)
                .stream()
                .map(Hotel::getCity)
                .distinct()
                .toList();
    }

    public HotelWithPriceDTO getHotelWithLowestPrice(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel nie znaleziony"));

        BigDecimal lowestPrice = roomService.getLowestPriceByHotelId(hotelId);

        return new HotelWithPriceDTO(hotel.getId(), hotel.getName(), lowestPrice);
    }
}
