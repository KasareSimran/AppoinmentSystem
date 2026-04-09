package com.appointmentBooking.appointmentBooking.exception;

public class InvalidSlotTimeException extends RuntimeException{
    public InvalidSlotTimeException(String message) {
        super(message);
    }
}
