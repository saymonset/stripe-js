package com.us.weavx.core.model;

import java.util.HashMap;

public class PaymentGatewayInfo {
	
	private int	paymentGatewayId;
	private HashMap<String, Object> paymentGwParameters;
	public PaymentGatewayInfo(int paymentGatewayId, HashMap<String, Object> paymentGwParameters) {
		super();
		this.paymentGatewayId = paymentGatewayId;
		this.paymentGwParameters = paymentGwParameters;
	}
	
	public PaymentGatewayInfo() {
		super();
	}

	public int getPaymentGatewayId() {
		return paymentGatewayId;
	}
	public void setPaymentGatewayId(int paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}
	public HashMap<String, Object> getPaymentGwParameters() {
		return paymentGwParameters;
	}
	public void setPaymentGwParameters(HashMap<String, Object> paymentGwParameters) {
		this.paymentGwParameters = paymentGwParameters;
	}

	@Override
	public String toString() {
		return "PaymentGatewayInfo [paymentGatewayId=" + paymentGatewayId + "]";
	}
	
	

}
