package com.spark.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String welcome() {
        // This looks for Signup.html inside src/main/resources/static/
        return "forward:/Signup.html"; 
    }
}
