package com.us.weavx.core.model;

public class AccessKey {

	private int id;
	private String key;
	private String secret;
	private long applicationId;
	private boolean isAdmin;
	private long customerId;
	public AccessKey(int id, String key, String secret, long applicationId, boolean isAdmin, long customerId) {
		super();
		this.id = id;
		this.key = key;
		this.secret = secret;
		this.applicationId = applicationId;
		this.isAdmin = isAdmin;
		this.customerId = customerId;
	}
	public AccessKey(String key, String secret, long applicationId, boolean isAdmin, long customerId) {
		super();
		this.key = key;
		this.secret = secret;
		this.applicationId = applicationId;
		this.isAdmin = isAdmin;
		this.customerId = customerId;
	}
	public AccessKey(String key, String secret, long applicationId, long customerId) {
		super();
		this.key = key;
		this.secret = secret;
		this.applicationId = applicationId;
		this.customerId = customerId;
	}
	public AccessKey() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	@Override
	public String toString() {
		return "AccessKey [id=" + id + ", key=" + key + ", secret=" + secret + ", applicationId=" + applicationId
				+ ", isAdmin=" + isAdmin + ", customerId=" + customerId + "]";
	}
	
		
}
