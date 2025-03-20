package com.att.tdp.popcorn_palace.exception;

public class ShowtimeNotFoundException extends ResourceNotFoundException {

    public ShowtimeNotFoundException(String message) {
        super(message);
    }

    public ShowtimeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
