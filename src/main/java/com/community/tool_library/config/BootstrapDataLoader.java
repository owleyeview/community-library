package com.community.tool_library.config;

import com.community.tool_library.models.AdminUser;
import com.community.tool_library.models.Tool;
import com.community.tool_library.models.User;
import com.community.tool_library.repositories.ItemRepository;
import com.community.tool_library.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class BootstrapDataLoader {

    @Bean
    CommandLineRunner loadData(UserRepository userRepository,
                               ItemRepository itemRepository,
                               BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            // Only runs if no users exist in the database
            if (userRepository.count() == 0)  {
                // Create Admin user
                AdminUser admin = AdminUser.adminBuilder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@toolibrary.com")
                        .role("ADMIN") // for clarity, though DiscriminatorValue("ADMIN") also does this
                        .build();
                userRepository.save(admin);

                // Create 4 Regular Users
                List<User> users = new ArrayList<>();
                for (int i = 1; i <= 4; i++) {
                    User user = User.builder()
                            .username("user" + i)
                            .password(passwordEncoder.encode("user" + i + "Pass"))
                            .email("user" + i + "@toolibrary.com")
                            .role("USER") // role is handled by @DiscriminatorColumn
                            .build();
                    users.add(user);
                }
                userRepository.saveAll(users);

                // Create 10 Tools (belongs to random owners from the created users)
                List<Tool> tools = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    // Pick an owner from the user list (including admin as an owner if you want)
                    User owner = (i % 2 == 0) ? admin : users.get(i % users.size());
                    Tool tool = Tool.toolBuilder()
                            .name("Tool " + i)
                            .description("Tool number " + i + " for testing")
                            .available(true)
                            .value(BigDecimal.valueOf(10 * i)) // arbitrary value
                            .owner(owner)
                            .build();
                    tools.add(tool);
                }
                itemRepository.saveAll(tools);

                System.out.println("Bootstrapped: 1 AdminUser, 4 Users, 10 Tools");
            } else {
                System.out.println("DataLoader: Existing users found. Skipping bootstrap.");
            }
        };
    }
}
