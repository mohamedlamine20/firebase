package com.example.firebase;

import org.springframework.http.HttpStatus;

public class MessageResponse {

    private HttpStatus status;

    private String message;

    private String url;

    public MessageResponse(String message, String url) {
        super();
        this.message = message;
        this.url = url;
    }

    public MessageResponse(HttpStatus status, String message, String url) {
        super();
        this.status = status;
        this.message = message;
        this.url = url;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
