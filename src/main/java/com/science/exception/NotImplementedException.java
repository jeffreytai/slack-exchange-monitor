package com.science.exception;

public class NotImplementedException extends RuntimeException {

	private static final long serialVersionUID = -831226752330093876L;
	
	public NotImplementedException() { super(); }
	public NotImplementedException(String message) { super(message); }
	public NotImplementedException(String message, Throwable cause) { super(message, cause); }
	public NotImplementedException(Throwable cause) { super(cause); }
}
