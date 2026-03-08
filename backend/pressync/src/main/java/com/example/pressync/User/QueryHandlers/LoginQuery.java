package com.example.pressync.User.QueryHandlers;

import com.example.pressync.Query;
import com.example.pressync.Services.JwtService;
import com.example.pressync.User.Model.DTOs.LoginDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginQuery implements Query<LoginDTO, String> {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<String> execute(LoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        // Generate the token for the authenticated user
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(token);
    }
}