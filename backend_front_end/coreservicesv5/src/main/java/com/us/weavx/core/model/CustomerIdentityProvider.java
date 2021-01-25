package com.us.weavx.core.model;

public class CustomerIdentityProvider {
	
	private long customerId;
	private int providerId;
	private String providerKey;
	private String providerSecret;
	public CustomerIdentityProvider(long customerId, int providerId, String providerKey, String providerSecret) {
		super();
		this.customerId = customerId;
		this.providerId = providerId;
		this.providerKey = providerKey;
		this.providerSecret = providerSecret;
	}
	public CustomerIdentityProvider() {
		super();
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	public String getProviderKey() {
		return providerKey;
	}
	public void setProviderKey(String providerKey) {
		this.providerKey = providerKey;
	}
	public String getProviderSecret() {
		return providerSecret;
	}
	public void setProviderSecret(String providerSecret) {
		this.providerSecret = providerSecret;
	}
	@Override
	public String toString() {
		return "CustomerIdentityProvider [customerId=" + customerId + ", providerId=" + providerId + ", providerKey="
				+ providerKey + ", providerSecret=" + providerSecret + "]";
	}
	
	
	
}
