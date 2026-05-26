package com.hotel_application.Hotel.dto;

import java.math.BigDecimal;


public class HotelWithPriceDTO {
    private Long id;
    private String name;
    private BigDecimal lowestPrice;

    public HotelWithPriceDTO(Long id,String name, BigDecimal lowestPrice ) {
        this.id = id;
        this.lowestPrice = lowestPrice;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
