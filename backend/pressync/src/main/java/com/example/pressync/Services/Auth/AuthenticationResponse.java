package com.example.pressync.Services.Auth;

// For sending the token back
public record AuthenticationResponse(String token, boolean requiresMfa) {
    public AuthenticationResponse(String token) {
        this(token, false);
    }

    public static AuthenticationResponse mfaRequired() {
        return new AuthenticationResponse(null, true);
    }
}
