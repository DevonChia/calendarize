package com.calendarize.calendarize.Repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CalendarDataRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CalendarDataRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public byte[] getAccessToken(String provider, String userId, String deviceId) {
        Map<String, String> tableMap = new HashMap<>();
        tableMap.put("google", "google_calendar_tokens");
        String sql = "SELECT access_token from " + tableMap.get(provider) + " WHERE user_id = :userId and device_id = :deviceId";

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("deviceId", deviceId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, byte[].class);
    }
}
