package com.us.weavx.core.model;

import java.sql.Timestamp;

public class BlackListItem {
	
	private long id;
	private Timestamp blockedAt;
	private String data;
	private int dataTypeId;
	private Long blockedBy;
	private boolean isActive;
	private Timestamp cleanedAt;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Timestamp getBlockedAt() {
		return blockedAt;
	}
	public void setBlockedAt(Timestamp blockedAt) {
		this.blockedAt = blockedAt;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(int dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	public Long getBlockedBy() {
		return blockedBy;
	}
	public void setBlockedBy(Long blockedBy) {
		this.blockedBy = blockedBy;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Timestamp getCleanedAt() {
		return cleanedAt;
	}
	public void setCleanedAt(Timestamp cleanedAt) {
		this.cleanedAt = cleanedAt;
	}
	@Override
	public String toString() {
		return "BlackListItem [id=" + id + ", blockedAt=" + blockedAt + ", data=" + data + ", dataTypeId=" + dataTypeId
				+ ", blockedBy=" + blockedBy + ", isActive=" + isActive + ", cleanedAt=" + cleanedAt + "]";
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
		BlackListItem other = (BlackListItem) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	

}
