package com.us.weavx.core.model;

import java.sql.Timestamp;

public class EmailAgent {
	
	private int id;
	private String name;
	private String url;
	private int healthyChecks;
	private boolean isHealthy;
	private boolean isEnabled;
	private Timestamp healthyDate;
	
	public EmailAgent() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getHealthyChecks() {
		return healthyChecks;
	}

	public void setHealthyChecks(int healthyChecks) {
		this.healthyChecks = healthyChecks;
	}

	public boolean isHealthy() {
		return isHealthy;
	}

	public void setIsHealthy(boolean isHealthy) {
		this.isHealthy = isHealthy;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Timestamp getHealthyDate() {
		return healthyDate;
	}

	public void setHealthyDate(Timestamp healthyDate) {
		this.healthyDate = healthyDate;
	}

	@Override
	public String toString() {
		return "EmailAgent [id=" + id + ", name=" + name + ", url=" + url + ", healthyChecks=" + healthyChecks
				+ ", isHealthy=" + isHealthy + ", isEnabled=" + isEnabled + ", healthyDate=" + healthyDate + "]";
	}
	
	

}
