package com.community.tool_library.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }
     @GetMapping("/register")
    public String registerPage() {
         return "register";
     }
}
