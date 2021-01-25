package com.us.weavx.core.model;

public class CustomerSupportedPaymentType {
	
	private long customerId;
	private int paymentTypeId;
	private boolean enabled;
	public CustomerSupportedPaymentType(long customerId, int paymentTypeId, boolean enabled) {
		super();
		this.customerId = customerId;
		this.paymentTypeId = paymentTypeId;
		this.enabled = enabled;
	}
	public CustomerSupportedPaymentType() {
		super();
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		return "CustomerSupportedPaymentType [customerId=" + customerId + ", paymentTypeId=" + paymentTypeId
				+ ", enabled=" + enabled + "]";
	}
	
	

}
