package com.s8.Crowdfunding.exceptions;

import java.util.function.Supplier;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
