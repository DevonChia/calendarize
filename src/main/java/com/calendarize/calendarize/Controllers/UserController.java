package com.calendarize.calendarize.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calendarize.calendarize.Services.UserService;
import com.calendarize.calendarize.Models.User;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getuser")
    public List<User> getData() {
        return userService.getUserData();
    }
}
