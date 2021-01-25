package com.us.weavx.core.model;

public class RestrictedEventAttendeeValidationResult {
	
	private boolean isValid;
	private String message;
	private RestrictedEventAttendee item;
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public RestrictedEventAttendee getItem() {
		return item;
	}
	public void setItem(RestrictedEventAttendee item) {
		this.item = item;
	}
	@Override
	public String toString() {
		return "RestrictedEventAttendeeValidationResult [isValid=" + isValid + ", message=" + message + ", item=" + item
				+ "]";
	}
	
	

}
