package com.quizit.quizit.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.quizit.quizit.model.User;
import com.quizit.quizit.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUser("Admin User", "admin@gmail.com", "1234", "ADMIN");
        seedUser("Instructor User", "instructor@gmail.com", "1234", "INSTRUCTOR");
        seedUser("Student User", "student@gmail.com", "1234", "STUDENT");
    }

    private void seedUser(String name, String email, String rawPassword, String role) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        userRepository.save(user);
    }
}
