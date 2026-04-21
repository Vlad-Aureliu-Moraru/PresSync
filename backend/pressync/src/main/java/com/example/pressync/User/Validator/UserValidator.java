package com.example.pressync.User.Validator;

import com.example.pressync.User.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private final UserRepository userRepository;
    private final String nameRegEx = "^[A-Z][a-z]{2,9}$";
    private final String emailRegEx = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(String name, String surname, String email, Integer userId) {
        if (name == null || !name.matches(nameRegEx) ||
                surname == null || !surname.matches(nameRegEx) ||
                email == null || !email.matches(emailRegEx)) {
            throw new IllegalArgumentException("Invalid name, surname, or email format");
        }

        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(userId)) {
                throw new IllegalArgumentException("Email already in use");
            }
        });
    }

    public void validate(String name, String surname, String email ) {
        if (name == null || !name.matches(nameRegEx) ||
                surname == null || !surname.matches(nameRegEx) ||
                email == null || !email.matches(emailRegEx)) {
            throw new IllegalArgumentException("Invalid name, surname, or email format");
        }

        userRepository.findByEmail(email).ifPresent(existingUser -> {
                throw new IllegalArgumentException("Email already in use");
        });
    }
}
