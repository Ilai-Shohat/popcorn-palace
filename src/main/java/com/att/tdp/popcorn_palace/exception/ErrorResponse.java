package com.att.tdp.popcorn_palace.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> errors;

    public ErrorResponse() {
        this.timestamp = Instant.now();
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(int status, String error, String message) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public void addError(String error) {
        this.errors.add(error);
    }
}
