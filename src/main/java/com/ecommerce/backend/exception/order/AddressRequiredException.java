package com.ecommerce.backend.exception.order;

public class AddressRequiredException extends RuntimeException {
    public AddressRequiredException(String message) {
        super(message);
    }
}
