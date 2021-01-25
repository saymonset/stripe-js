package com.us.weavx.core.model;

public class HealthCheckRequest {
	
	private int emailAgentId;
	private long responseTime;
	public HealthCheckRequest() {
		super();
	}
	public int getEmailAgentId() {
		return emailAgentId;
	}
	public void setEmailAgentId(int emailAgentId) {
		this.emailAgentId = emailAgentId;
	}
	public long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
	@Override
	public String toString() {
		return "HealthCheckRequest [emailAgentId=" + emailAgentId + ", responseTime=" + responseTime + "]";
	}
	
	
}
