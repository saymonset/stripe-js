package com.us.weavx.core.model;

import java.sql.Timestamp;

public class AuditRecord {

	private long id;
	private long customerId;
	private long applicationId;
	private String who;
	private Timestamp createdAt;
	private String source;
	private String what;
	private String ipAddress;
	private String userAgent;
	private int auditLevelId;
	private String data;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getWho() {
		return who;
	}
	public void setWho(String who) {
		this.who = who;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
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
	public int getAuditLevelId() {
		return auditLevelId;
	}
	public void setAuditLevelId(int auditLevelId) {
		this.auditLevelId = auditLevelId;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "AuditRecord [id=" + id + ", customerId=" + customerId + ", applicationId=" + applicationId + ", who="
				+ who + ", createdAt=" + createdAt + ", source=" + source + ", what=" + what + ", ipAddress="
				+ ipAddress + ", userAgent=" + userAgent + ", auditLevelId=" + auditLevelId + ", data=" + data + "]";
	}
	
	
	

}
