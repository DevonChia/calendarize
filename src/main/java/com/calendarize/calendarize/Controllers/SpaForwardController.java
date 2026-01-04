package com.calendarize.calendarize.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {
    /**
     * Captures all non-API and non static requests and forward to index.html for components routing
     * @return forward back to root index.html instead of a redirect
     */
    @GetMapping(value = "/{path:^(?!api|assets)[^\\.]*}/**")
    public String forward() {
        return "forward:/index.html";
    }
}
