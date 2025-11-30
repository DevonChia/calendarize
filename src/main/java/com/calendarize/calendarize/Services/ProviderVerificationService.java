package com.calendarize.calendarize.Services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.calendarize.calendarize.Repository.ProviderVerificationRepository;
import com.calendarize.calendarize.Util.EncryptionUtil;

@Service
public class ProviderVerificationService {
    private final ProviderVerificationRepository providerVerificationRepository;
    private final GoogleCalendarAuthService googleCalendarAuthService;

    public ProviderVerificationService(ProviderVerificationRepository providerVerificationRepository, GoogleCalendarAuthService googleCalendarAuthService){
        this.providerVerificationRepository = providerVerificationRepository;
        this.googleCalendarAuthService = googleCalendarAuthService;
    }

    @Value("${google.calendar.api.client.encryption.secret}")
    private String secret;

    public Boolean isValidLogin(String deviceId, String userId, String provider){
        Map<String, Object> data = providerVerificationRepository.getTokenData(deviceId, userId, provider);

        // when user has a change in deviceId
        if (data.containsKey("status") && "missing token data".equals(data.get("status").toString())) {
            return false;
        }

        LocalDateTime accessTokenExpiry = ((Timestamp)data.get("access_token_expiry")).toLocalDateTime();
        if (accessTokenExpiry.isBefore(LocalDateTime.now())) {
            LocalDateTime refreshTokenExpiry = ((Timestamp)data.get("refresh_token_expiry")).toLocalDateTime();
            if (refreshTokenExpiry.isBefore(LocalDateTime.now())) {
                return false;
            }
            byte[] encryptedRefreshToken = (byte[])data.get("refresh_token");
            String refreshToken = EncryptionUtil.decrypt(encryptedRefreshToken, secret);
            if (!googleCalendarAuthService.hasRefreshAccessToken(refreshToken, userId, deviceId, provider)) {
                return false;
            }
        }
        return true;
    }
}
