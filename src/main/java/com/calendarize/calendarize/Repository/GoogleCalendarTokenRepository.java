package com.calendarize.calendarize.Repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GoogleCalendarTokenRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GoogleCalendarTokenRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void upsert(String userId, String deviceId, byte[] accessToken, byte[] refreshToken, String accessTokenExpiry, String refreshTokenExpiry) {
        String sql = "INSERT INTO google_calendar_tokens (user_id, device_id, access_token, access_token_expiry, refresh_token, refresh_token_expiry) VALUES (:userId, :deviceId, :accessToken, :accessTokenExpiry, :refreshToken, :refreshTokenExpiry) ON DUPLICATE KEY UPDATE access_token = :accessToken, access_token_expiry = :accessTokenExpiry, refresh_token = :refreshToken, refresh_token_expiry = :refreshTokenExpiry, date_updated = :dtm";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("deviceId", deviceId)
            .addValue("accessToken", accessToken)
            .addValue("accessTokenExpiry", accessTokenExpiry)
            .addValue("refreshToken", refreshToken)
            .addValue("refreshTokenExpiry", refreshTokenExpiry)
            .addValue("dtm", "NOW()");

        namedParameterJdbcTemplate.update(sql, params);
    }
}
