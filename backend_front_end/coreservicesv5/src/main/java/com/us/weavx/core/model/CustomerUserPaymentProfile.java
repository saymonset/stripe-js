package com.us.weavx.core.model;

public class CustomerUserPaymentProfile {

	private long id;
	private long customerUserId;
	private long customerId;
	private long paymentGwId;
	private String paymentGwCustomerId;
	
	public CustomerUserPaymentProfile() {
	
	}

	public CustomerUserPaymentProfile(long id, long customerUserId, long customerId, long paymentGwId,
			String paymentGwCustomerId) {
		super();
		this.id = id;
		this.customerUserId = customerUserId;
		this.customerId = customerId;
		this.paymentGwId = paymentGwId;
		this.paymentGwCustomerId = paymentGwCustomerId;
	}

	public CustomerUserPaymentProfile(long customerUserId, long customerId, long paymentGwId,
			String paymentGwCustomerId) {
		super();
		this.customerUserId = customerUserId;
		this.customerId = customerId;
		this.paymentGwId = paymentGwId;
		this.paymentGwCustomerId = paymentGwCustomerId;
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

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getPaymentGwId() {
		return paymentGwId;
	}

	public void setPaymentGwId(long paymentGwId) {
		this.paymentGwId = paymentGwId;
	}

	public String getPaymentGwCustomerId() {
		return paymentGwCustomerId;
	}

	public void setPaymentGwCustomerId(String paymentGwCustomerId) {
		this.paymentGwCustomerId = paymentGwCustomerId;
	}

	@Override
	public String toString() {
		return "CustomerUserPaymentProfile [id=" + id + ", customerUserId=" + customerUserId + ", customerId="
				+ customerId + ", paymentGwId=" + paymentGwId + ", paymentGwCustomerId=" + paymentGwCustomerId + "]";
	}
	
	

}
