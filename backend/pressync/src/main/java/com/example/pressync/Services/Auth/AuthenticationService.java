package com.example.pressync.Services.Auth;

import com.example.pressync.User.CommandHandlers.CreateUserCommand;
import com.example.pressync.User.Model.DTOSs.UserCreateDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserRoles;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger auditLogger = LoggerFactory.getLogger("com.example.pressync.audit");
    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private static final int LOCKOUT_MINUTES = 15;
    private static final int MFA_EXPIRY_MINUTES = 5;

    private final UserRepository repository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CreateUserCommand createUserCommand;
    private final EmailService emailService;

    public AuthenticationResponse register(UserCreateDTO userCreateDTO) {
        return createUserCommand.execute(userCreateDTO).getBody();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        repository.findByEmail(request.email()).ifPresent(this::ensureNotLocked);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            processFailedLoginAttempt(request.email());
            throw ex;
        }

        User user = repository.findByEmail(request.email())
                .orElseThrow();

        clearLoginFailures(user);

        if (user.getRole() == UserRoles.ADMIN || Boolean.TRUE.equals(user.getMfaEnabled())) {
            String otpCode = generateOtp();
            user.setMfaCode(otpCode);
            user.setMfaExpiry(LocalDateTime.now().plusMinutes(MFA_EXPIRY_MINUTES));
            repository.save(user);

            emailService.sendOtpCode(user.getEmail(), otpCode);
            auditLogger.info("action=MFA_OTP_SENT actorId={} targetUserId={}", user.getId(), user.getId());
            return AuthenticationResponse.mfaRequired();
        }

        return createJwtResponse(user);
    }

    public AuthenticationResponse verifyMfa(VerifyMfaRequest request) {
        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid MFA request"));

        if (user.getMfaCode() == null || user.getMfaExpiry() == null) {
            throw new IllegalArgumentException("MFA challenge not found");
        }

        if (user.getMfaExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("MFA code expired");
        }

        if (!user.getMfaCode().equals(request.otpCode())) {
            throw new IllegalArgumentException("Invalid MFA code");
        }

        user.setMfaCode(null);
        user.setMfaExpiry(null);
        repository.save(user);

        auditLogger.info("action=MFA_VERIFIED actorId={} targetUserId={}", user.getId(), user.getId());
        return createJwtResponse(user);
    }

    private AuthenticationResponse createJwtResponse(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());

        String jwtToken = jwtService.generateToken(extraClaims, user);

        return new AuthenticationResponse(jwtToken);
    }

    private void ensureNotLocked(User user) {
        if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
            throw new LockedException("Account is locked. Try again later.");
        }
    }

    private void processFailedLoginAttempt(String email) {
        repository.findByEmail(email).ifPresent(user -> {
            int failedAttempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(failedAttempts);

            if (failedAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
                user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCKOUT_MINUTES));
                user.setFailedLoginAttempts(0);
                auditLogger.info("action=ACCOUNT_LOCKED actorId={} targetUserId={}", user.getId(), user.getId());
            }

            repository.save(user);
        });
    }

    private void clearLoginFailures(User user) {
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        repository.save(user);
    }

    private String generateOtp() {
        int value = new Random().nextInt(900000) + 100000;
        return String.valueOf(value);
    }
}
