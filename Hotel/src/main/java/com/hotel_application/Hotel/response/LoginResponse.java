package com.hotel_application.Hotel.response;


public class LoginResponse {
    private String token;
    private long expiredIn;

    public LoginResponse(long expiredIn, String token) {
        this.expiredIn = expiredIn;
        this.token = token;
    }

    public long getExpiredIn() {
        return expiredIn;
    }

    public void setExpiredIn(long expiredIn) {
        this.expiredIn = expiredIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
