package com.us.weavx.core.model;

import java.sql.Timestamp;

public class EmailAgentHist {
	
	private long id;
	private int emailAgentId;
	private long minResponseTime;
	private long maxResponseTime;
	private double avgResponseTime;
	private Timestamp checkDate;
	private int emailType;
	
	public EmailAgentHist() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getEmailAgentId() {
		return emailAgentId;
	}
	public void setEmailAgentId(int emailAgentId) {
		this.emailAgentId = emailAgentId;
	}
	public long getMinResponseTime() {
		return minResponseTime;
	}
	public void setMinResponseTime(long minResponseTime) {
		this.minResponseTime = minResponseTime;
	}
	public long getMaxResponseTime() {
		return maxResponseTime;
	}
	public void setMaxResponseTime(long maxResponseTime) {
		this.maxResponseTime = maxResponseTime;
	}
	public double getAvgResponseTime() {
		return avgResponseTime;
	}
	public void setAvgResponseTime(double avgResponseTime) {
		this.avgResponseTime = avgResponseTime;
	}
	public Timestamp getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Timestamp checkDate) {
		this.checkDate = checkDate;
	}
	
	public int getEmailType() {
		return emailType;
	}
	public void setEmailType(int emailType) {
		this.emailType = emailType;
	}
	@Override
	public String toString() {
		return "EmailAgentHist [id=" + id + ", emailAgentId=" + emailAgentId + ", minResponseTime=" + minResponseTime
				+ ", maxResponseTime=" + maxResponseTime + ", avgResponseTime=" + avgResponseTime + ", checkDate="
				+ checkDate + ", emailType=" + emailType + "]";
	}
	
	
	

}
