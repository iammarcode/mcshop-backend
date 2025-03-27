package com.marco.mcshop.auth.exception.auth;

public class OtpValidationFailedException extends RuntimeException {
    public OtpValidationFailedException(String otp) {
        super("Otp validation failed: " + otp);
    }
}
