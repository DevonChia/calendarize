package com.calendarize.calendarize.Services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.calendarize.calendarize.Repository.GoogleCalendarTokenRepository;
import com.calendarize.calendarize.Util.EncryptionUtil;

@Service
public class GoogleCalendarAuthService {
    private final GoogleCalendarTokenRepository tokenRepository;

    public GoogleCalendarAuthService(GoogleCalendarTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Value("${google.calendar.api.client.id}")
    private String clientId;
    
    @Value("${google.calendar.api.client.secret}")
    private String clientSecret;

    @Value("${google.calendar.api.client.redirect.uri}")
    private String redirectURI;

    @Value("${google.calendar.api.client.encryption.secret}")
    private String secret;

    public void getTokens(String code, String deviceId, String userId) throws Exception{
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
        ResponseEntity<Map> response = rest.postForEntity(
            "https://oauth2.googleapis.com/token",
            request,
            Map.class
        );
        Map<String, Object> responseBody = response.getBody();
        upsertTokens(userId, deviceId, responseBody);
    }

    private void upsertTokens(String userId, String deviceId, Map<String, Object> responseBody) {
        if (responseBody == null) {
            throw new IllegalArgumentException("Null response body.");
        }
        if (!responseBody.containsKey("access_token")) {
            throw new IllegalArgumentException("Missing access token from response body.");
        }
        if (!responseBody.containsKey("expires_in")) {
            throw new IllegalArgumentException("Missing access token expiry from response body.");
        }
        if (!responseBody.containsKey("refresh_token")) {
            throw new IllegalArgumentException("Missing refresh token from response body.");
        }
        if (!responseBody.containsKey("refresh_token_expires_in")) {
            throw new IllegalArgumentException("Missing refresh token expiry from response body.");
        }
        String accessToken = responseBody.get("access_token").toString();
        String refreshToken = responseBody.get("refresh_token").toString();
        String accessTokenExpiry = responseBody.get("expires_in").toString();
        String refreshTokenExpiry = responseBody.get("refresh_token_expires_in").toString();
        
        byte[] encryptedAccessToken = EncryptionUtil.encrypt(accessToken, secret);
        byte[] encryptedRefreshToken = EncryptionUtil.encrypt(refreshToken, secret);

        tokenRepository.upsert(userId, deviceId, encryptedAccessToken, encryptedRefreshToken, accessTokenExpiry, refreshTokenExpiry);
    }
}
