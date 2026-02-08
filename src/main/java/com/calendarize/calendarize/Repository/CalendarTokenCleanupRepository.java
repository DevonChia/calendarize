package com.calendarize.calendarize.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CalendarTokenCleanupRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CalendarTokenCleanupRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void delete() {
        List<String> conditions = new ArrayList<>();
        conditions.add("(date_updated IS NOT NULL AND date_updated < DATE_SUB(NOW(), INTERVAL 6 MONTH))");
        conditions.add("(date_updated IS NULL AND date_created < DATE_SUB(NOW(), INTERVAL 6 MONTH))");
        conditions.add("(refresh_token_expiry < NOW())");

        String sql = "DELETE FROM google_calendar_tokens WHERE " + String.join(" OR ", conditions);

        namedParameterJdbcTemplate.update(sql, EmptySqlParameterSource.INSTANCE);
    }

}
