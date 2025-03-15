package com.java.project.exceptions;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
