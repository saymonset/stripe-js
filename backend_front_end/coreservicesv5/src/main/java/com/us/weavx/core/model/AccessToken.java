package com.us.weavx.core.model;

import java.sql.Timestamp;

public class AccessToken {
	private long id;
	private String token;
	private Timestamp expirationDate;
	private int status;
	private Timestamp createdAt;
	private long accessKeyId;
	public AccessToken() {
		super();
	}
	public AccessToken(String token, long accessKeyId) {
		super();
		this.token = token;
		this.accessKeyId = accessKeyId;
	}
	public AccessToken(long id, String token, Timestamp expirationDate, int status, Timestamp createdAt,
			long accessKeyId) {
		super();
		this.id = id;
		this.token = token;
		this.expirationDate = expirationDate;
		this.status = status;
		this.createdAt = createdAt;
		this.accessKeyId = accessKeyId;
	}
	public AccessToken(String token, Timestamp expirationDate, int status, long accessKeyId) {
		super();
		this.token = token;
		this.expirationDate = expirationDate;
		this.status = status;
		this.accessKeyId = accessKeyId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Timestamp getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public long getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(long accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	@Override
	public String toString() {
		return "AccessToken [id=" + id + ", token=" + token + ", expirationDate=" + expirationDate + ", status="
				+ status + ", createdAt=" + createdAt + ", accessKeyId=" + accessKeyId + "]";
	}
	
	
	
	
	

}
