package com.appointmentBooking.appointmentBooking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(UserAlreadyExistsException ex) {
        logger.error("User already exist exception: {}", ex.getMessage(), ex);
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SlotAlreadyExistsException.class)
    public ResponseEntity<?> handleSlotExists(SlotAlreadyExistsException ex) {
        logger.error("Slot already exist exception: {}", ex.getMessage(), ex);
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUser(UserNotFoundException ex) {
        logger.error("User not found exception: {}", ex.getMessage(), ex);
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SlotNotAvailableException.class)
    public ResponseEntity<?> handleSlot(SlotNotAvailableException ex) {
        logger.error("Slot not found exception: {}", ex.getMessage(), ex);
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<?> handleAppointment(AppointmentNotFoundException ex) {
        logger.error("Appointment not found exception: {}", ex.getMessage(), ex);
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSlotTimeException.class)
    public ResponseEntity<?> handleInvalidTime(InvalidSlotTimeException ex) {
        logger.error("Invaid slot time exception: {}", ex.getMessage(), ex);
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDatabaseException(DataAccessException ex) {

        logger.error("Database error occurred: {}", ex.getMessage(), ex);

        return buildResponse("Database error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {

        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        return buildResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }



    // ✅ COMMON METHOD
    private ResponseEntity<?> buildResponse(String msg, HttpStatus status) {
        Map<String, Object> res = new HashMap<>();
        res.put("message", msg);
        res.put("status", false);
        return new ResponseEntity<>(res, status);
    }
}
