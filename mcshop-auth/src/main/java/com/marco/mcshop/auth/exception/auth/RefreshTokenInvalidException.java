package com.marco.mcshop.auth.exception.auth;

public class RefreshTokenInvalidException extends RuntimeException {
    public RefreshTokenInvalidException(String refreshToken) {
        super("Invalid refresh token: " + refreshToken);
    }
}
