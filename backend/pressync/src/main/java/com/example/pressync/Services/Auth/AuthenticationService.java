package com.example.pressync.Services.Auth;

import com.example.pressync.User.CommandHandlers.CreateUserCommand;
import com.example.pressync.User.Model.DTOSs.UserCreateDTO;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CreateUserCommand createUserCommand;
    private final EmailService emailService;

    public AuthenticationResponse register(UserCreateDTO userCreateDTO) {
        createUserCommand.execute(userCreateDTO);

        var user = repository.findByEmail(userCreateDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User registration failed"));

        String otpCode = generateOtpCode();
        user.setMfaEnabled(true);
        user.setMfaCode(otpCode);
        user.setMfaExpiry(LocalDateTime.now().plusMinutes(5));
        repository.save(user);

        emailService.sendOtpCode(user.getEmail(), otpCode);

        String maskedEmail = maskEmail(user.getEmail());
        return AuthenticationResponse.mfaRequired(maskedEmail);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = repository.findByEmail(request.email())
                .orElseThrow();

        if (Boolean.TRUE.equals(user.getMfaEnabled())) {
            String otpCode = generateOtpCode();
            user.setMfaCode(otpCode);
            user.setMfaExpiry(LocalDateTime.now().plusMinutes(5));
            repository.save(user);

            emailService.sendOtpCode(user.getEmail(), otpCode);

            String maskedEmail = maskEmail(user.getEmail());
            return AuthenticationResponse.mfaRequired(maskedEmail);
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());

        var jwtToken = jwtService.generateToken(extraClaims, user);

        return new AuthenticationResponse(jwtToken, false, null);
    }

    public AuthenticationResponse verifyOtp(VerifyMfaRequest request) {
        var user = repository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (user.getMfaCode() == null || user.getMfaExpiry() == null) {
            throw new BadCredentialsException("No OTP code requested. Please login again.");
        }

        if (user.getMfaExpiry().isBefore(LocalDateTime.now())) {
            user.setMfaCode(null);
            user.setMfaExpiry(null);
            repository.save(user);
            throw new BadCredentialsException("OTP code has expired. Please login again.");
        }

        if (!user.getMfaCode().equals(request.otpCode())) {
            throw new BadCredentialsException("Invalid OTP code.");
        }

        user.setMfaCode(null);
        user.setMfaExpiry(null);
        repository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());

        var jwtToken = jwtService.generateToken(extraClaims, user);

        return new AuthenticationResponse(jwtToken, false, null);
    }

    private String generateOtpCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return email;
        return email.charAt(0) + "****" + email.substring(atIndex - 1);
    }
}