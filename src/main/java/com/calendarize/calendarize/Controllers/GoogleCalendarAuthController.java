package com.calendarize.calendarize.Controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calendarize.calendarize.Services.GoogleCalendarAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class GoogleCalendarAuthController {
    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);
    private final GoogleCalendarAuthService authService;

    public GoogleCalendarAuthController(GoogleCalendarAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/oauth2/googleauth")
    public void authCheck(@RequestParam("code") String code, @RequestParam("scope") String scope, @RequestParam("state") String stateData, HttpServletResponse httpResponse) throws IOException, GeneralSecurityException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonState = mapper.readValue(stateData, Map.class);

        try {
            authService.getTokens(code, jsonState.get("device_id"), jsonState.get("user_id"));
            httpResponse.sendRedirect("http://localhost:8080/user-dashboard.html?login_status=ok");

        } catch (Exception e) {
            logger.error("Google Calendar API Authentication failed. Error: " + e);
            httpResponse.sendRedirect("http://localhost:8080/user-dashboard.html?login_status=failed");
        }
    }

    

    
}



