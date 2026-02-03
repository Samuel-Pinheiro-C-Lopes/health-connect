package io.github.samuel_pinheiro_c_lopes.spring_common.exceptions;

import java.time.LocalDateTime;

// our own not java 16+ compliant StandardError DTO! 
public final class StandardError {
	private final LocalDateTime timestamp;
    private final Integer status;
    private final String error;
    private final String message;
    private final String path;
    
    public StandardError(
    		LocalDateTime timestamp,
    	    Integer status,
    	    String error,
    	    String message,
    	    String path
    ) {
    	this.timestamp = timestamp;
    	this.status = status;
    	this.error = error;
    	this.message = message;
    	this.path = path;
    }
    
    public LocalDateTime timestamp() {
    	return this.timestamp;
    }
    
    public Integer status() {
    	return this.status;
    }
    
    public String error() {
    	return this.error;
    }
    
    public String message() {
    	return this.message;
    }
    
    public String path() {
    	return this.path;
    }
}
