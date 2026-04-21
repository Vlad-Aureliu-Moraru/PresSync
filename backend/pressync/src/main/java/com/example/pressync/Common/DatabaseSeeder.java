package com.example.pressync.Common;

import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserRoles;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.email:admin@pressync.local}")
    private String defaultAdminEmail;

    @Value("${app.default-admin.password:change-me-admin-password}")
    private String defaultAdminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole(UserRoles.ADMIN)) {
            logger.info("Bootstrap admin already exists. Skipping seeding.");
            return;
        }

        if (userRepository.existsByEmail(defaultAdminEmail)) {
            logger.warn("Default admin email already exists for a non-admin user. Skipping seeding.");
            return;
        }

        User admin = new User();
        admin.setName("System");
        admin.setSurname("Admin");
        admin.setEmail(defaultAdminEmail);
        admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
        admin.setRole(UserRoles.ADMIN);
        admin.setActive(true);

        userRepository.save(admin);
        logger.info("Default admin user seeded successfully with email {}", defaultAdminEmail);
    }
}
