package com.appointmentBooking.appointmentBooking.exception;

public class SlotNotAvailableException extends RuntimeException {
    public SlotNotAvailableException(String message) {
        super(message);
    }
}
