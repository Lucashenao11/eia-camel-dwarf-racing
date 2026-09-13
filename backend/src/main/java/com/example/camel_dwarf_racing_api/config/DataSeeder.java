package com.example.camel_dwarf_racing_api.config;

import com.example.camel_dwarf_racing_api.model.Role;
import com.example.camel_dwarf_racing_api.model.User;
import com.example.camel_dwarf_racing_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        seedUser("admin", "admin123", Role.ADMINISTRATOR);
        seedUser("organizer", "organizer123", Role.RACE_ORGANIZER);
        seedUser("viewer", "viewer123", Role.VIEWER);
    }

    private void seedUser(String username, String rawPassword, Role role) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            userRepository.save(user);
        }
    }
}