package com.rakhimov.homework.exception;

public class InvalidTaskStatusException extends RuntimeException {

    public InvalidTaskStatusException(String message) {
        super(message);
    }
}
