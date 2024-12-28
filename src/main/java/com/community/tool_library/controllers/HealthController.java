package com.community.tool_library.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthController {
    @GetMapping("/up")
    public ResponseEntity<String> up() {
        return ResponseEntity.ok("OK");
    }
}
