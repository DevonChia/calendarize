package com.calendarize.calendarize.Repository;

import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderVerificationRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProviderVerificationRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Map<String, Object> getTokenData(String deviceId, String userId, String provider){
        String sql = "SELECT access_token_expiry, refresh_token, refresh_token_expiry from google_calendar_tokens where device_id = :deviceId AND user_id = :userId AND provider = :provider";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("deviceId", deviceId)
            .addValue("userId", userId)
            .addValue("provider", provider);

        try {
            return namedParameterJdbcTemplate.queryForMap(sql, params);
        }
        catch (EmptyResultDataAccessException e) {
            Map<String, Object> res = Map.of(
                "status", "missing token data"
            );
            return res;
        }
    }
}
