package com.example.pressync.Services.Auth;

public record VerifyMfaRequest(String email, String otpCode) {
}
