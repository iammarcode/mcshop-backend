package com.marcoindev.mcshop.user.exception.address;

public class AddressNotOwnedByUserException extends RuntimeException {
    public AddressNotOwnedByUserException(String message) {
        super(message);
    }
} 