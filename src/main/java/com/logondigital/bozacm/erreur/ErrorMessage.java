package com.logondigital.bozacm.erreur;

import java.util.Date;

public class ErrorMessage {
    private Integer statusCode;
    private Date timestamp;
    private String message;
    private String error;

    public ErrorMessage() {
    }

    public ErrorMessage(Integer statusCode, Date timestamp, String message, String error) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.error = error;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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


