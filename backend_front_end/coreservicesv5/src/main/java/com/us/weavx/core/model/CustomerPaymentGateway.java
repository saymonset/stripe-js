package com.us.weavx.core.model;

public class CustomerPaymentGateway {
	
	private long customerId;
	private int paymentGatewayId;
	private String authKey1;
	private String authKey2;
	private String authKey3;
	private String authKey4;
	private boolean publicAuthKey1;
	private boolean publicAuthKey2;
	private boolean publicAuthKey3;
	private boolean publicAuthKey4;
	private String connectedAccountId;

	private int paymentModeId;
	public CustomerPaymentGateway(long customerId, int paymentGatewayId, String authKey1, String authKey2,
			String authKey3, String authKey4, int paymentModeId) {
		super();
		this.customerId = customerId;
		this.paymentGatewayId = paymentGatewayId;
		this.authKey1 = authKey1;
		this.authKey2 = authKey2;
		this.authKey3 = authKey3;
		this.authKey4 = authKey4;
		this.paymentModeId = paymentModeId;
	}
	
	public CustomerPaymentGateway(long customerId, int paymentGatewayId, String authKey1, String authKey2,
			String authKey3, String authKey4, boolean publicAuthKey1, boolean publicAuthKey2, boolean publicAuthKey3,
			boolean publicAuthKey4, int paymentModeId) {
		super();
		this.customerId = customerId;
		this.paymentGatewayId = paymentGatewayId;
		this.authKey1 = authKey1;
		this.authKey2 = authKey2;
		this.authKey3 = authKey3;
		this.authKey4 = authKey4;
		this.publicAuthKey1 = publicAuthKey1;
		this.publicAuthKey2 = publicAuthKey2;
		this.publicAuthKey3 = publicAuthKey3;
		this.publicAuthKey4 = publicAuthKey4;
		this.paymentModeId = paymentModeId;
	}
	public CustomerPaymentGateway() {
		super();
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getPaymentGatewayId() {
		return paymentGatewayId;
	}
	public void setPaymentGatewayId(int paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}
	public String getAuthKey1() {
		return authKey1;
	}
	public void setAuthKey1(String authKey1) {
		this.authKey1 = authKey1;
	}
	public String getAuthKey2() {
		return authKey2;
	}
	public void setAuthKey2(String authKey2) {
		this.authKey2 = authKey2;
	}
	public String getAuthKey3() {
		return authKey3;
	}
	public void setAuthKey3(String authKey3) {
		this.authKey3 = authKey3;
	}
	public String getAuthKey4() {
		return authKey4;
	}
	public void setAuthKey4(String authKey4) {
		this.authKey4 = authKey4;
	}
	public int getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(int paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	public boolean isPublicAuthKey1() {
		return publicAuthKey1;
	}
	public void setPublicAuthKey1(boolean publicAuthKey1) {
		this.publicAuthKey1 = publicAuthKey1;
	}
	public boolean isPublicAuthKey2() {
		return publicAuthKey2;
	}
	public void setPublicAuthKey2(boolean publicAuthKey2) {
		this.publicAuthKey2 = publicAuthKey2;
	}
	public boolean isPublicAuthKey3() {
		return publicAuthKey3;
	}
	public void setPublicAuthKey3(boolean publicAuthKey3) {
		this.publicAuthKey3 = publicAuthKey3;
	}
	public boolean isPublicAuthKey4() {
		return publicAuthKey4;
	}
	public void setPublicAuthKey4(boolean publicAuthKey4) {
		this.publicAuthKey4 = publicAuthKey4;
	}
	
	

	public String getConnectedAccountId() {
		return connectedAccountId;
	}

	public void setConnectedAccountId(String connectedAccountId) {
		this.connectedAccountId = connectedAccountId;
	}

	@Override
	public String toString() {
		return "CustomerPaymentGateway [customerId=" + customerId + ", paymentGatewayId=" + paymentGatewayId
				+ ", authKey1=" + authKey1 + ", authKey2=" + authKey2 + ", authKey3=" + authKey3 + ", authKey4="
				+ authKey4 + ", publicAuthKey1=" + publicAuthKey1 + ", publicAuthKey2=" + publicAuthKey2
				+ ", publicAuthKey3=" + publicAuthKey3 + ", publicAuthKey4=" + publicAuthKey4 + ", connectedAccountId="
				+ connectedAccountId + ", paymentModeId=" + paymentModeId + "]";
	}
	
	public void cleanPrivateInfo() {
		if (!this.publicAuthKey1) {
			this.authKey1 = "*****";
		}
		if (!this.publicAuthKey2) {
			this.authKey2 = "*****";
		}
		if (!this.publicAuthKey3) {
			this.authKey3 = "*****";
		}
		if (!this.publicAuthKey4) {
			this.authKey4 = "*****";
		}
	}
	
	
	
}
