// c:/Users/HP/OneDrive/Desktop/projek UAS PBO/backendPBO/backend/src/main/java/com/example/backend/BackendApplication.java
package com.example.backend;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.backend.model.Category;
import com.example.backend.model.User;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.UserRepository;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "AdMutCy";
            String password = "AdMutCy24260612";

            userRepository.findByUsername(username).ifPresentOrElse(user -> {
                boolean changed = false;
                if (!"admin".equals(user.getRole())) {
                    user.setRole("admin");
                    changed = true;
                }
                if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                    user.setPasswordHash(passwordEncoder.encode(password));
                    changed = true;
                }
                if (changed) {
                    userRepository.save(user);
                }
            }, () -> {
                User user = User.builder()
                        .username(username)
                        .passwordHash(passwordEncoder.encode(password))
                        .role("admin")
                        .build();
                userRepository.save(user);
            });
        };
    }

    @Bean
    public CommandLineRunner seedCategories(CategoryRepository categoryRepository) {
        return args -> {
            List<String> defaultCategories = List.of(
                    "Digital Art",
                    "Traditional",
                    "Photography",
                    "3D",
                    "Other"
            );
            for (String name : defaultCategories) {
                categoryRepository.findByName(name).orElseGet(() -> categoryRepository.save(Category.builder().name(name).build()));
            }
        };
    }
}
