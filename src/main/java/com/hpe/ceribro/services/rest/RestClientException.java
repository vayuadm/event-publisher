package com.hpe.ceribro.services.rest;

public class RestClientException extends RuntimeException {
    
    private static final long serialVersionUID = -3884419033773907260L;
    
    private final int statusCode;
    private String reason;
    
    public RestClientException(String message, int statusCode, String reason) {
        
        super(message);
        this.statusCode = statusCode;
        this.reason = reason;
    }
    
    public int getStatusCode() {
        
        return statusCode;
    }

    public String getReason() {

        return reason;
    }
}
