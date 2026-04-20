package com.example.pressync.User.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Services.Auth.AuthenticationResponse;
import com.example.pressync.Services.Auth.JWTService;
import com.example.pressync.User.Model.DTOSs.UserCreateDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserRoles;
import com.example.pressync.User.UserRepository;
import com.example.pressync.User.Validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateUserCommand implements Command<UserCreateDTO, AuthenticationResponse> { // Change return type
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final JWTService jwtService;


    @Override
    public ResponseEntity<AuthenticationResponse> execute(UserCreateDTO entity) {
        userValidator.validate(entity.getName(), entity.getSurname(), entity.getEmail());

        User userToSave = new User();
        userToSave.setName(entity.getName());
        userToSave.setEmail(entity.getEmail());
        userToSave.setSurname(entity.getSurname());
        userToSave.setActive(true);
        userToSave.setRole(UserRoles.USER);

        userToSave.setPassword(passwordEncoder.encode(entity.getPassword()));

        userRepository.save(userToSave);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId",userToSave.getId());
        extraClaims.put("role", userToSave.getRole().name());

        var token = jwtService.generateToken(extraClaims,userToSave);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}