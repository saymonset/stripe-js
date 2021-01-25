package com.us.weavx.core.model;

public class CustomerSignatureGateway {

	private long customerId;
	private int	signatureGatewayId;
	private String signatureApiParam1;
	private String signatureApiParam2;
	private String signatureApiParam3;
	private String signatureApiParam4;
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getSignatureGatewayId() {
		return signatureGatewayId;
	}
	public void setSignatureGatewayId(int signatureGatewayId) {
		this.signatureGatewayId = signatureGatewayId;
	}
	public String getSignatureApiParam1() {
		return signatureApiParam1;
	}
	public void setSignatureApiParam1(String signatureApiParam1) {
		this.signatureApiParam1 = signatureApiParam1;
	}
	public String getSignatureApiParam2() {
		return signatureApiParam2;
	}
	public void setSignatureApiParam2(String signatureApiParam2) {
		this.signatureApiParam2 = signatureApiParam2;
	}
	public String getSignatureApiParam3() {
		return signatureApiParam3;
	}
	public void setSignatureApiParam3(String signatureApiParam3) {
		this.signatureApiParam3 = signatureApiParam3;
	}
	public String getSignatureApiParam4() {
		return signatureApiParam4;
	}
	public void setSignatureApiParam4(String signatureApiParam4) {
		this.signatureApiParam4 = signatureApiParam4;
	}
	@Override
	public String toString() {
		return "CustomerSignatureGateway [customerId=" + customerId + ", signatureGatewayId=" + signatureGatewayId
				+ ", signatureApiParam1=" + signatureApiParam1 + ", signatureApiParam2=" + signatureApiParam2
				+ ", signatureApiParam3=" + signatureApiParam3 + ", signatureApiParam4=" + signatureApiParam4 + "]";
	}	
	
	
	
}
