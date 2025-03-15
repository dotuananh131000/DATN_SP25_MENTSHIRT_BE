package com.java.project.exceptions;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(String message) {
        super(message);
    }
}
