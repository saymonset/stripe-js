package com.us.weavx.core.model;

import java.sql.Timestamp;

public class ValidationCode {

	private long id;
	private String code;
	private String device;
	private int deviceTypeId;
	private int tryCount;
	private boolean isUsed;
	private boolean isExpired;
	private Timestamp expiresAt;
	private Timestamp createdAt;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public int getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(int deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public int getTryCount() {
		return tryCount;
	}
	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}
	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	public boolean isExpired() {
		return isExpired;
	}
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
	public Timestamp getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(Timestamp expiresAt) {
		this.expiresAt = expiresAt;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidationCode other = (ValidationCode) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ValidationCode [id=" + id + ", code=" + code + ", device=" + device + ", deviceTypeId=" + deviceTypeId
				+ ", tryCount=" + tryCount + ", isUsed=" + isUsed + ", isExpired=" + isExpired + ", expiresAt="
				+ expiresAt + ", createdAt=" + createdAt + "]";
	}
	
	

}
