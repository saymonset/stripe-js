package com.us.weavx.core.model;

public class FailedRecipientInfo {
	
	private String email;
	private String name;
	private String errorMessage;
	public FailedRecipientInfo() {
		super();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "FailedRecipientInfo [email=" + email + ", name=" + name + ", errorMessage=" + errorMessage + "]";
	}
	
	

}
