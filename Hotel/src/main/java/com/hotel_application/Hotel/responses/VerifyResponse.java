package com.hotel_application.Hotel.responses;

public class VerifyResponse {
        private String status;
        private String message;

        public VerifyResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        // gettery i settery
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

}
