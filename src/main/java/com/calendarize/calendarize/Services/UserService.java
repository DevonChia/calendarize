package com.calendarize.calendarize.Services;

import java.util.List;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.calendarize.calendarize.Models.User;


@Service
public class UserService {
    private final JdbcTemplate db;
    
    public UserService(JdbcTemplate db) {
        this.db = db;
    }

    public List<User> getUserData(){
        String sql = "select * from users";
        return db.query(sql, (ResultSet rs, int rowNum) -> {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setCoyName(rs.getString("company"));
            user.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            return user;
        });
    }
}
