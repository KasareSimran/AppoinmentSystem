package com.appointmentBooking.appointmentBooking.DTO;

public class ApiResponse {

    private String message;
    private boolean status;

    public ApiResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() { return message; }
    public boolean isStatus() { return status; }
}
