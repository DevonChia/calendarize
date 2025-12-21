package com.calendarize.calendarize.Exceptions;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CalendarIntegrationException.class)
    public ResponseEntity<?> handleCalendarIntegration(CalendarIntegrationException e) {
        logger.error("[handleCalendarIntegration] Caught exception", e);

        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(Map.of (
                "error", e.getMessage()
            ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException e) {
        logger.error("[handleBadRequest] Caught exception", e);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of (
                "error", e.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpected(Exception e) {
        logger.error("[handleUnexpected] Caught exception", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of (
                "error", "Internal server error."
            ));
    }
}
