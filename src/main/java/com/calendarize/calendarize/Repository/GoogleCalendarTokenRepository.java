package com.calendarize.calendarize.Repository;

import java.time.LocalDateTime;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GoogleCalendarTokenRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GoogleCalendarTokenRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void upsert(String userId, String deviceId, String provider, byte[] encryptedAccessToken, byte[] encryptedRefreshToken, LocalDateTime accessTokenExpiry, LocalDateTime refreshTokenExpiry) {
        String sql = "INSERT INTO google_calendar_tokens (user_id, device_id, provider, access_token, access_token_expiry, refresh_token, refresh_token_expiry) VALUES (:userId, :deviceId, :provider, :accessToken, :accessTokenExpiry, :refreshToken, :refreshTokenExpiry) ON DUPLICATE KEY UPDATE access_token = :accessToken, access_token_expiry = :accessTokenExpiry, refresh_token = :refreshToken, refresh_token_expiry = :refreshTokenExpiry, date_updated = :dtm";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("deviceId", deviceId)
            .addValue("provider", provider)
            .addValue("accessToken", encryptedAccessToken)
            .addValue("accessTokenExpiry", accessTokenExpiry)
            .addValue("refreshToken", encryptedRefreshToken)
            .addValue("refreshTokenExpiry", refreshTokenExpiry)
            .addValue("dtm", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void update(String userId, String deviceId, String provider, byte[] encryptedAccessToken, LocalDateTime accessTokenExpiry, LocalDateTime refreshTokenExpiry) {
        String sql = "UPDATE google_calendar_tokens SET access_token = :accessToken, access_token_expiry = :accessTokenExpiry, refresh_token_expiry = :refreshTokenExpiry, date_updated = :dtm where user_id = :userId and device_id = :deviceId and provider = :provider";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("accessToken", encryptedAccessToken)
            .addValue("accessTokenExpiry", accessTokenExpiry)
            .addValue("refreshTokenExpiry", refreshTokenExpiry)
            .addValue("dtm", LocalDateTime.now())
            .addValue("userId", userId)
            .addValue("deviceId", deviceId)
            .addValue("provider", provider);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
