package com.calendarize.calendarize.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalendarController {
    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    @GetMapping("/getdata")
    public Map<String, Object> getdata() {
        logger.info("testing logger class logging...");
        System.out.println("Client called the endpoint");

        Map<String, Object> data = new HashMap<>();
        data.put("vals", "return as new!");
        data.put("name", "test name here");
        return data;
    }

}
