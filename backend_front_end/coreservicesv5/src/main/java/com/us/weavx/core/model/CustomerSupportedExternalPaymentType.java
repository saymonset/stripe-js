package com.us.weavx.core.model;

public class CustomerSupportedExternalPaymentType {
	
	private long customerId;
	private int externalPTId;
	private boolean isEnabled;
	public CustomerSupportedExternalPaymentType() {
		super();
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getExternalPTId() {
		return externalPTId;
	}
	public void setExternalPTId(int externalPTId) {
		this.externalPTId = externalPTId;
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
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + externalPTId;
		result = prime * result + (isEnabled ? 1231 : 1237);
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
		CustomerSupportedExternalPaymentType other = (CustomerSupportedExternalPaymentType) obj;
		if (customerId != other.customerId)
			return false;
		if (externalPTId != other.externalPTId)
			return false;
		if (isEnabled != other.isEnabled)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CustomerSupportedExternalPaymentTypes [customerId=" + customerId + ", externalPTId=" + externalPTId
				+ ", isEnabled=" + isEnabled + "]";
	}
	
	

}
