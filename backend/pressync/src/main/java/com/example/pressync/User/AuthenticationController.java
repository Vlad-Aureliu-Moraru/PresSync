package com.example.pressync.User;

import com.example.pressync.Services.Auth.AuthenticationRequest;
import com.example.pressync.Services.Auth.AuthenticationResponse;
import com.example.pressync.Services.Auth.AuthenticationService;
import com.example.pressync.Services.Auth.VerifyMfaRequest;
import com.example.pressync.User.Model.DTOSs.UserCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserCreateDTO user) {
        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<AuthenticationResponse> verifyMfa(@RequestBody VerifyMfaRequest request) {
        return ResponseEntity.ok(service.verifyMfa(request));
    }
}
