package com.ecommerce.backend.exception.user;

public class NotAllowedToChangeCredentialsException extends RuntimeException {
    public NotAllowedToChangeCredentialsException(String message) {
        super(message);
    }
}
