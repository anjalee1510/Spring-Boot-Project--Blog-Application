package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;
public class BlogAPIException extends RuntimeException{
	
	private HttpStatus status;
	private String message;
	public BlogAPIException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	public BlogAPIException(String message,HttpStatus status, String message1) {
		super(message);
		this.message=message1;
		this.status=status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
	
	

}
