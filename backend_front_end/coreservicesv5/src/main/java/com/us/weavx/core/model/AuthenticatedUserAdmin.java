package com.us.weavx.core.model;

public class AuthenticatedUserAdmin {

	private long customerId;
	private String customerName;
	private long applicationId;
	private String applicationName;
	private String key;
	private String secret;

	public AuthenticatedUserAdmin() {
		
	}

	public AuthenticatedUserAdmin(long customerId, String customerName, long applicationId, String applicationName,
			String key, String secret) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.applicationId = applicationId;
		this.applicationName = applicationName;
		this.key = key;
		this.secret = secret;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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

	@Override
	public String toString() {
		return "AuthenticatedUserAdmin [customerId=" + customerId + ", customerName=" + customerName
				+ ", applicationId=" + applicationId + ", applicationName=" + applicationName + ", key=" + key
				+ ", secret=" + secret + "]";
	}


	
	

}
