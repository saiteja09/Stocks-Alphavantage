package com.ddtek.common.connection;



public class Status {

	private String message;
	private boolean valid;
	
	public Status(){
		message=null;
		valid=false;
	}
	
	public Status( boolean valid, String message){
		this.valid = valid;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
}
