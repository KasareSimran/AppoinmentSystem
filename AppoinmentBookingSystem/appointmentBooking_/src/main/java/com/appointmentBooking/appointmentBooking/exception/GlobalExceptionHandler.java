package com.appointmentBooking.appointmentBooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(UserAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SlotAlreadyExistsException.class)
    public ResponseEntity<?> handleSlotExists(SlotAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUser(UserNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SlotNotAvailableException.class)
    public ResponseEntity<?> handleSlot(SlotNotAvailableException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<?> handleAppointment(AppointmentNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSlotTimeException.class)
    public ResponseEntity<?> handleInvalidTime(InvalidSlotTimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ✅ COMMON METHOD
    private ResponseEntity<?> buildResponse(String msg, HttpStatus status) {
        Map<String, Object> res = new HashMap<>();
        res.put("message", msg);
        res.put("status", false);
        return new ResponseEntity<>(res, status);
    }
}
