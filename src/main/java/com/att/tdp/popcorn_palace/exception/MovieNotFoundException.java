package com.att.tdp.popcorn_palace.exception;

public class MovieNotFoundException extends ResourceNotFoundException {

    public MovieNotFoundException(String message) {
        super(message);
    }

    public MovieNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
