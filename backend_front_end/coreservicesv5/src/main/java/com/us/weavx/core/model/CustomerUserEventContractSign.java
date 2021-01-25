package com.us.weavx.core.model;

import java.sql.Timestamp;

public class CustomerUserEventContractSign {

	private long id;
	private long customerUserId;
	private long applicationId;
	private String signatureUrl;
	private boolean signatureStatus;
	private Timestamp signedAt;
	private String signatureData;
	private long transactionId;
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
	public String getSignatureUrl() {
		return signatureUrl;
	}
	public void setSignatureUrl(String signatureUrl) {
		this.signatureUrl = signatureUrl;
	}
	public boolean isSignatureStatus() {
		return signatureStatus;
	}
	public void setSignatureStatus(boolean signatureStatus) {
		this.signatureStatus = signatureStatus;
	}
	public Timestamp getSignedAt() {
		return signedAt;
	}
	public void setSignedAt(Timestamp signedAt) {
		this.signedAt = signedAt;
	}
	public String getSignatureData() {
		return signatureData;
	}
	public void setSignatureData(String signatureData) {
		this.signatureData = signatureData;
	}
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	@Override
	public String toString() {
		return "CustomerUserEventContractSign [id=" + id + ", customerUserId=" + customerUserId + ", applicationId="
				+ applicationId + ", signatureUrl=" + signatureUrl + ", signatureStatus=" + signatureStatus
				+ ", signedAt=" + signedAt + ", signatureData=" + signatureData + ", transactionId=" + transactionId
				+ "]";
	}

	
	
}
