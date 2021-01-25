package com.us.weavx.core.model;

public class RestrictedEventAttendee {
	
	private long id;
	private long customerId;
	private long applicationId;
	private String email;
	private boolean isActive;
	public RestrictedEventAttendee() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "RestrictedEventAttendee [id=" + id + ", customerId=" + customerId + ", applicationId=" + applicationId
				+ ", email=" + email + ", isActive=" + isActive + "]";
	}
	
	
	

}
