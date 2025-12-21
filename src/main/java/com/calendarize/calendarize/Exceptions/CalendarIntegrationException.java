package com.calendarize.calendarize.Exceptions;

public class CalendarIntegrationException extends RuntimeException {
    public CalendarIntegrationException(String message) {
        super(message);
    }

    public CalendarIntegrationException(String message, Throwable e) {
        super(message, e);
    }
    
}
