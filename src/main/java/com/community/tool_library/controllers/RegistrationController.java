package com.community.tool_library.controllers;

import com.community.tool_library.dtos.UserRegistrationDTO;
import com.community.tool_library.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid UserRegistrationDTO registrationData, BindingResult bindingResult, Model model) {

        System.out.println("Received registration data: " + registrationData);  // temporary debug output

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Invalid registration data");
            return "register";
        }

        try {
            userService.registerUser(registrationData);
            model.addAttribute("successMessage", "Registration successful! Redirecting to login...");
            return "register";
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "register";
        }
    }

}
