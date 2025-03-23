package com.att.tdp.popcorn_palace.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    // Returns message only if there are no specific errors, otherwise returns null
    // which will be excluded from JSON due to @JsonInclude annotation
    public String getMessage() {
        return (errors == null || errors.isEmpty()) ? message : null;
    }
}
