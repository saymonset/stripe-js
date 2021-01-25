package com.us.weavx.core.model;

import java.sql.Timestamp;

public class DeveloperKey {
	
	private long id;
	private String devKey;
	private String devSecret;
	private boolean productionEnabled;
	private Timestamp createdAt;
	private long developerId;
	public DeveloperKey(long id, String devKey, String devSecret, boolean productionEnabled, Timestamp createdAt,
			long developerId) {
		super();
		this.id = id;
		this.devKey = devKey;
		this.devSecret = devSecret;
		this.productionEnabled = productionEnabled;
		this.createdAt = createdAt;
		this.developerId = developerId;
	}
	
	
	
	public DeveloperKey(String devKey, String devSecret, boolean productionEnabled, Timestamp createdAt,
			long developerId) {
		super();
		this.devKey = devKey;
		this.devSecret = devSecret;
		this.productionEnabled = productionEnabled;
		this.createdAt = createdAt;
		this.developerId = developerId;
	}



	public DeveloperKey(String devKey, String devSecret, boolean productionEnabled, long developerId) {
		super();
		this.devKey = devKey;
		this.devSecret = devSecret;
		this.productionEnabled = productionEnabled;
		this.developerId = developerId;
	}



	public DeveloperKey() {
		super();
	}



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDevKey() {
		return devKey;
	}
	public void setDevKey(String devKey) {
		this.devKey = devKey;
	}
	public String getDevSecret() {
		return devSecret;
	}
	public void setDevSecret(String devSecret) {
		this.devSecret = devSecret;
	}
	public boolean isProductionEnabled() {
		return productionEnabled;
	}
	public void setProductionEnabled(boolean productionEnabled) {
		this.productionEnabled = productionEnabled;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public long getDeveloperId() {
		return developerId;
	}
	public void setDeveloperId(long developerId) {
		this.developerId = developerId;
	}



	@Override
	public String toString() {
		return "DeveloperKey [id=" + id + ", devKey=" + devKey + ", devSecret=" + devSecret + ", productionEnabled="
				+ productionEnabled + ", createdAt=" + createdAt + ", developerId=" + developerId + "]";
	}
	
	

}
