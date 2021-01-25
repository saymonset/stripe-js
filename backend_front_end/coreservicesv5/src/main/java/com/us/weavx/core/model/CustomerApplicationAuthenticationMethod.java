package com.us.weavx.core.model;

public class CustomerApplicationAuthenticationMethod {

	private long customerId;
	private long applicationId;
	private int auhtenticationMethodId;
	private int orderAt;
	public CustomerApplicationAuthenticationMethod() {
		super();
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
	public int getAuhtenticationMethodId() {
		return auhtenticationMethodId;
	}
	public void setAuhtenticationMethodId(int auhtenticationMethodId) {
		this.auhtenticationMethodId = auhtenticationMethodId;
	}
	public int getOrderAt() {
		return orderAt;
	}
	public void setOrderAt(int orderAt) {
		this.orderAt = orderAt;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (applicationId ^ (applicationId >>> 32));
		result = prime * result + auhtenticationMethodId;
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + orderAt;
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
		CustomerApplicationAuthenticationMethod other = (CustomerApplicationAuthenticationMethod) obj;
		if (applicationId != other.applicationId)
			return false;
		if (auhtenticationMethodId != other.auhtenticationMethodId)
			return false;
		if (customerId != other.customerId)
			return false;
		if (orderAt != other.orderAt)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CustomerApplicationAuthenticationMethod [customerId=" + customerId + ", applicationId=" + applicationId
				+ ", auhtenticationMethodId=" + auhtenticationMethodId + ", orderAt=" + orderAt + "]";
	}
	
	
	
}
