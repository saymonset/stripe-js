package com.us.weavx.core.model;

public class IdentityProvider {
	
	private int id;
	private String providerName;
	private String providerImplementorClass;
	public IdentityProvider(int id, String providerName, String providerImplementorClass) {
		super();
		this.id = id;
		this.providerName = providerName;
		this.providerImplementorClass = providerImplementorClass;
	}
	public IdentityProvider(String providerName, String providerImplementorClass) {
		super();
		this.providerName = providerName;
		this.providerImplementorClass = providerImplementorClass;
	}
	public IdentityProvider() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getProviderImplementorClass() {
		return providerImplementorClass;
	}
	public void setProviderImplementorClass(String providerImplementorClass) {
		this.providerImplementorClass = providerImplementorClass;
	}
	@Override
	public String toString() {
		return "IdentityProvider [id=" + id + ", providerName=" + providerName + ", providerImplementorClass="
				+ providerImplementorClass + "]";
	}
	
	

}
