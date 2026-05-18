package com.example.pressync.Services.Auth;

public record AuthenticationResponse(
    String token,
    boolean requiresMfa,
    String otpDestination
) {
    public AuthenticationResponse(String token) {
        this(token, false, null);
    }

    public static AuthenticationResponse mfaRequired(String otpDestination) {
        return new AuthenticationResponse(null, true, otpDestination);
    }
}
