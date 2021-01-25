package com.us.weavx.core.model;

public class PaymentData {
	
	private long id;
	private int paymentTypeId;
	public PaymentData(long id, int paymentTypeId) {
		super();
		this.id = id;
		this.paymentTypeId = paymentTypeId;
	}
	public PaymentData() {
		super();
	}
	public PaymentData(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	@Override
	public String toString() {
		return "PaymentData [id=" + id + ", paymentTypeId=" + paymentTypeId + "]";
	}
	
	

}
