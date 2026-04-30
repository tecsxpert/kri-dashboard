package com.internship.tool.exception;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final Map<String, String> details; // field-level validation errors (nullable)

    private ErrorResponse(Builder builder) {
        this.timestamp = Instant.now();          // set eagerly in constructor
        this.status    = builder.status;
        this.error     = builder.error;
        this.message   = builder.message;
        this.path      = builder.path;
        this.details   = builder.details;
    }

    // ------------------------------------------------------------------ //
    //  Static factory methods                                             //
    // ------------------------------------------------------------------ //

    public static ErrorResponse of(
            org.springframework.http.HttpStatus httpStatus,
            String message,
            String path) {

        return builder()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }

    public static ErrorResponse withDetails(
            org.springframework.http.HttpStatus httpStatus,
            String message,
            String path,
            Map<String, String> details) {

        return builder()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .path(path)
                .details(details)
                .build();
    }

    // ------------------------------------------------------------------ //
    //  Builder                                                            //
    // ------------------------------------------------------------------ //

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status;
        private String error;
        private String message;
        private String path;
        private Map<String, String> details;

        public Builder status(int status)                   { this.status  = status;  return this; }
        public Builder error(String error)                  { this.error   = error;   return this; }
        public Builder message(String message)              { this.message = message; return this; }
        public Builder path(String path)                    { this.path    = path;    return this; }
        public Builder details(Map<String, String> details) { this.details = details; return this; }

        public ErrorResponse build() { return new ErrorResponse(this); }
    }

    // ------------------------------------------------------------------ //
    //  Getters (Jackson serialises public getters by default)             //
    // ------------------------------------------------------------------ //

    public Instant getTimestamp() { return timestamp; }
    public int getStatus()        { return status;    }
    public String getError()      { return error;     }
    public String getMessage()    { return message;   }
    public String getPath()       { return path;      }
    public Map<String, String> getDetails() { return details; }
}
