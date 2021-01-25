package com.us.weavx.core.model;

import java.sql.Timestamp;

public class UniqueCustomerApplicationUserCode {
	
	private long id;
	private long customerUserId;
	private long applicationId;
	private String userCode;
	private Timestamp createdAt;
	private int unsuccessfulValidationTries;
	private boolean isEnabled;
	public UniqueCustomerApplicationUserCode() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerUserId() {
		return customerUserId;
	}
	public void setCustomerUserId(long customerUserId) {
		this.customerUserId = customerUserId;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public int getUnsuccessfulValidationTries() {
		return unsuccessfulValidationTries;
	}
	public void setUnsuccessfulValidationTries(int unsuccessfulValidationTries) {
		this.unsuccessfulValidationTries = unsuccessfulValidationTries;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (applicationId ^ (applicationId >>> 32));
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + (int) (customerUserId ^ (customerUserId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (isEnabled ? 1231 : 1237);
		result = prime * result + unsuccessfulValidationTries;
		result = prime * result + ((userCode == null) ? 0 : userCode.hashCode());
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
		UniqueCustomerApplicationUserCode other = (UniqueCustomerApplicationUserCode) obj;
		if (applicationId != other.applicationId)
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (customerUserId != other.customerUserId)
			return false;
		if (id != other.id)
			return false;
		if (isEnabled != other.isEnabled)
			return false;
		if (unsuccessfulValidationTries != other.unsuccessfulValidationTries)
			return false;
		if (userCode == null) {
			if (other.userCode != null)
				return false;
		} else if (!userCode.equals(other.userCode))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UniqueCustomerApplicationUserCode [id=" + id + ", customerUserId=" + customerUserId + ", applicationId="
				+ applicationId + ", userCode=" + userCode + ", createdAt=" + createdAt
				+ ", unsuccessfulValidationTries=" + unsuccessfulValidationTries + ", isEnabled=" + isEnabled + "]";
	}
	
	

}
