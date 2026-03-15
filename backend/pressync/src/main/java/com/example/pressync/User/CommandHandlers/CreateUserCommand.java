package com.example.pressync.User.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Services.AuthenticationResponse;
import com.example.pressync.Services.JWTService;
import com.example.pressync.User.Model.DTOs.UserCreateDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserRoles;
import com.example.pressync.User.UserRepository;
import com.example.pressync.User.Validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserCommand implements Command<UserCreateDTO, AuthenticationResponse> { // Change return type
    private final PasswordEncoder passwordEncoder; // Use the interface
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final JWTService jwtService; // Add this


    @Override
    public ResponseEntity<AuthenticationResponse> execute(UserCreateDTO entity) {
        // 1. Validate
        userValidator.validate(entity.getName(), entity.getSurname(), entity.getEmail());

        // 2. Map DTO to Entity
        User userToSave = new User();
        userToSave.setName(entity.getName());
        userToSave.setEmail(entity.getEmail());
        userToSave.setSurname(entity.getSurname());
        userToSave.setActive(true); // FIX: Satisfies DB constraint
        userToSave.setRole(UserRoles.USER);

        userToSave.setPassword(passwordEncoder.encode(entity.getPassword()));

        userRepository.save(userToSave);

        String token = jwtService.generateToken(userToSave);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}