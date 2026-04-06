package com.logondigital.bozacm.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class ErrorMessage {
    private Integer statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String error;

    public ErrorMessage(Integer statusCode, LocalDateTime timestamp, String message, String error) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.error = error;
    }

    public ErrorMessage() {
    }




    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

