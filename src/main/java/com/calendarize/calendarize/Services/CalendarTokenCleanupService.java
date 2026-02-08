package com.calendarize.calendarize.Services;

import org.springframework.stereotype.Service;

import com.calendarize.calendarize.Repository.CalendarTokenCleanupRepository;

@Service
public class CalendarTokenCleanupService {
    private final CalendarTokenCleanupRepository calendarTokenCleanupRepository;

    public CalendarTokenCleanupService(CalendarTokenCleanupRepository calendarTokenCleanupRepository) {
        this.calendarTokenCleanupRepository = calendarTokenCleanupRepository;
    }

    public void cleanupTokens() {
        calendarTokenCleanupRepository.delete();
    }

}
