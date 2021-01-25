package com.us.weavx.core.model;

public class CustomerDefaultPaymentGateway {
	
	private long customerId;
	private int	paymentTypeId;
	private int paymentGatewayId;
	public CustomerDefaultPaymentGateway(long customerId, int paymentTypeId, int paymentGatewayId) {
		super();
		this.customerId = customerId;
		this.paymentTypeId = paymentTypeId;
		this.paymentGatewayId = paymentGatewayId;
	}
	public CustomerDefaultPaymentGateway() {
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
	public int getPaymentGatewayId() {
		return paymentGatewayId;
	}
	public void setPaymentGatewayId(int paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}
	@Override
	public String toString() {
		return "CustomerDefaultPaymentGateway [customerId=" + customerId + ", paymentTypeId=" + paymentTypeId
				+ ", paymentGatewayId=" + paymentGatewayId + "]";
	}
	
	

}
