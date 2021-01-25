package com.us.weavx.core.model;

import java.sql.Timestamp;

public class UserAccessToken {
	private long id;
	private long customerUserId;
	private String token;
	private Timestamp createdAt;
	private Timestamp expiresAt;
	private int	status;
	private long applicationId;
	private String ipAddress;
	private String userAgent;
	
	
	
	public UserAccessToken() {
		super();
	}
	public UserAccessToken(long id, long customerUserId, String token, Timestamp createdAt, Timestamp expiresAt,
			int status, long applicationId, String ipAddress, String userAgent) {
		super();
		this.id = id;
		this.customerUserId = customerUserId;
		this.token = token;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.status = status;
		this.applicationId = applicationId;
		this.ipAddress = ipAddress;
		this.userAgent = userAgent;
	}
	public UserAccessToken(long customerUserId, String token, Timestamp createdAt, Timestamp expiresAt, int status,
			long applicationId, String ipAddress, String userAgent) {
		super();
		this.customerUserId = customerUserId;
		this.token = token;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.status = status;
		this.applicationId = applicationId;
		this.ipAddress = ipAddress;
		this.userAgent = userAgent;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerUserId() {
		return customerUserId;
	}
	public void setCustomerUserId(long customerUserId) {
		this.customerUserId = customerUserId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(Timestamp expiresAt) {
		this.expiresAt = expiresAt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	@Override
	public String toString() {
		return "UserAccessToken [id=" + id + ", customerUserId=" + customerUserId + ", token=" + token + ", createdAt="
				+ createdAt + ", expiresAt=" + expiresAt + ", status=" + status + ", applicationId=" + applicationId
				+ ", ipAddress=" + ipAddress + ", userAgent=" + userAgent + "]";
	}
	@Override
	public boolean equals(Object arg0) {
		UserAccessToken uAT = (UserAccessToken) arg0;
		return this.id == uAT.getId();
	}
	
	
	
	
	
	
}
