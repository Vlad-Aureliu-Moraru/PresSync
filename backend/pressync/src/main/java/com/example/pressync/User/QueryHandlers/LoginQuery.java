package com.example.pressync.User.QueryHandlers;

import com.example.pressync.Query;
import com.example.pressync.User.Model.DTOs.LoginDTO;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginQuery implements Query<LoginDTO, String> {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
//
    @Override
    public ResponseEntity<String> execute(LoginDTO dto) {
//        User user = userRepository.findByEmail(dto.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
////
//        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
//            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
//
//        }
//        String token = JWTService.generateToken(user);
        return ResponseEntity.ok().build();
}
}
