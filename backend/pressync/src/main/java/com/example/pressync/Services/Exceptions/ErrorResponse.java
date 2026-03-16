package com.example.pressync.Services.Exceptions;

public record ErrorResponse(
        int status,
        String message,
        long timestamp,
        String path
) {}