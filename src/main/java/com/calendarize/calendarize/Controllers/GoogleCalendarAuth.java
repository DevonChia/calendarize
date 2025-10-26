package com.calendarize.calendarize.Controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
public class GoogleCalendarAuth {
    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);
    
    @Value("${google.calendar.api.client.id}")
    private String clientId;
    
    @Value("${google.calendar.api.client.secret}")
    private String clientSecret;

    @Value("${google.calendar.api.client.redirect.uri}")
    private String redirectURI;

    @GetMapping("/oauth2/googleauth")
    public void authCheck(@RequestParam("code") String code, @RequestParam("scope") String scope, @RequestParam("state") String deviceId, HttpServletResponse httpResponse) throws IOException, GeneralSecurityException {

        try { 
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectURI);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            RestTemplate rest = new RestTemplate();
            ResponseEntity<Map> response = rest.postForEntity( //stop here - debug the 401 unauthorized error
                "https://oauth2.googleapis.com/token",
                request,
                Map.class
            );

            Map<String, Object> jsonBody = response.getBody();
            if (jsonBody == null) {
                throw new Exception("Null response body.");
            }
            if (!jsonBody.containsKey("access_token")) {
                throw new Exception("Missing access token from response body.");
            }
            if (!jsonBody.containsKey("refresh_token")) {
                throw new Exception("Missing refresh token from response body.");
            }

            String accessToken = jsonBody.get("access_token").toString();
            String refreshToken = jsonBody.get("refresh_token").toString();


            httpResponse.sendRedirect("http://localhost:8080/user-dashboard.html?login_status=ok");

        } catch (Exception e) {
            //TODO: log error message to db
            httpResponse.sendRedirect("http://localhost:8080/user-dashboard.html?login_status=failed");
        }
    }
    
}
