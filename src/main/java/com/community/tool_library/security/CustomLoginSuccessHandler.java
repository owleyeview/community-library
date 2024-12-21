package com.community.tool_library.security;

import com.community.tool_library.models.User;
import com.community.tool_library.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomLoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Get the username of the logged in user
        String username = authentication.getName();

//        // Get the role of the logged in user
//        String role = userRepository.findByUsername(username).getRole();
//        // Redirect the user based on their role
//        if (role.equalsIgnoreCase("ADMIN")) {
//            response.sendRedirect("/admin");
//        } else {
//            response.sendRedirect("/user");
//        }

        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
        response.sendRedirect("/items");
    }
}
