package com.calendarize.calendarize.Controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calendarize.calendarize.Services.CalendarDataService;

@RestController
@RequestMapping("/api/calendar-data")
public class CalendarController {
    private final CalendarDataService calendarDataService;
    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    public CalendarController(CalendarDataService calendarDataService) {
        this.calendarDataService = calendarDataService;
    }

    @GetMapping("/{provider}/{userId}/{deviceId}")
    public ResponseEntity<?> getCalendarData(@PathVariable String provider, @PathVariable String userId, @PathVariable String deviceId) {
        List<Map<String, Object>> data;
        if ("google".equals(provider)) {
            data = calendarDataService.getGoogleCalendarData(userId, deviceId);
        }
        else if ("microsoft".equals(provider)) { //TODO: to add
            // getMSData()
            data = null;
        } else {
            throw new IllegalArgumentException("Unknown provider");
        }
        return ResponseEntity.ok(data);
    }

}
