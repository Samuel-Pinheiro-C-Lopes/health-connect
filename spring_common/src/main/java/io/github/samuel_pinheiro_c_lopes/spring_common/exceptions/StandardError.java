package io.github.samuel_pinheiro_c_lopes.spring_common.exceptions;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StandardError {
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @JsonProperty("status")
    private Integer status;
    
    @JsonProperty("error")
    private String error;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("path")
    private String path;

    // 1. Default Constructor (Required by Jackson)
    public StandardError() {}

    // 2. All-Args Constructor
    public StandardError(LocalDateTime timestamp, Integer status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // 3. Standard Getters (Required for serialization)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}