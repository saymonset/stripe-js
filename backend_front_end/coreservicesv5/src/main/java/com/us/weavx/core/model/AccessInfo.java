package com.us.weavx.core.model;

public class AccessInfo {

	private AccessToken accessToken;
	private long customerId;
	private long applicationId;
	
	public AccessInfo() {
		// TODO Auto-generated constructor stub
	}

	public AccessInfo(AccessToken accessToken, long customerId, long applicationId) {
		super();
		this.accessToken = accessToken;
		this.customerId = customerId;
		this.applicationId = applicationId;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
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

	@Override
	public String toString() {
		return "AccessInfo [accessToken=" + accessToken + ", customerId=" + customerId + ", applicationId="
				+ applicationId + "]";
	}
	
	

}
