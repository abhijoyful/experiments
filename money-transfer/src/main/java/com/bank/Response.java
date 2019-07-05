package com.bank;

/**
 * Response entity for client to represent response of an operation. 
 * @author Abhishek Malik
 */
public class Response {
	
	private int status;
	private String message;
	
	
	public Response() { }

	public Response(int status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
