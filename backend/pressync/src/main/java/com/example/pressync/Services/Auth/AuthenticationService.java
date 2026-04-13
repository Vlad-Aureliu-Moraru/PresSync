package com.example.pressync.Services.Auth;

import com.example.pressync.User.CommandHandlers.CreateUserCommand;
import com.example.pressync.User.Model.DTOSs.UserCreateDTO;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CreateUserCommand createUserCommand;

    public AuthenticationResponse register(UserCreateDTO userCreateDTO) {

    var response =    createUserCommand.execute(userCreateDTO).getBody();
    return  response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. This line triggers the actual password check
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // 2. If we reach here, the password was correct
        var user = repository.findByEmail(request.email())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}