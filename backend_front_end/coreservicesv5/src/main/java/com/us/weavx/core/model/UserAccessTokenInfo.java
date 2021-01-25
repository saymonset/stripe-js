package com.us.weavx.core.model;

import java.sql.Timestamp;

public class UserAccessTokenInfo {

	private String userEmail;
	private long id;
	private Timestamp createdAt;
	private String customerName;
	private String applicationName;
	private long customerId;
	private long applicationId;
	private String ipAddress;
	private String userAgent;
	private int tokenStatusId;
	private String tokenStatusName;
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public int getTokenStatusId() {
		return tokenStatusId;
	}
	public void setTokenStatusId(int tokenStatusId) {
		this.tokenStatusId = tokenStatusId;
	}
	public String getTokenStatusName() {
		return tokenStatusName;
	}
	public void setTokenStatusName(String tokenStatusName) {
		this.tokenStatusName = tokenStatusName;
	}
	@Override
	public String toString() {
		return "UserAccessTokenInfo [userEmail=" + userEmail + ", id=" + id + ", createdAt=" + createdAt
				+ ", customerName=" + customerName + ", applicationName=" + applicationName + ", customerId="
				+ customerId + ", applicationId=" + applicationId + ", ipAddress=" + ipAddress + ", userAgent="
				+ userAgent + ", tokenStatusId=" + tokenStatusId + ", tokenStatusName=" + tokenStatusName + "]";
	}
	
	

}
