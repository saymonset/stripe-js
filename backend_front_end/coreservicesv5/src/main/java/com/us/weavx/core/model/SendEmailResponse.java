package com.us.weavx.core.model;

import java.util.List;

public class SendEmailResponse {
	
	private int responseCode;
	private List<FailedRecipientInfo> failedRecipients;
	private double averageSendingTime;
	private long minSendingTime;
	private long maxSendingTime;
	private String message;
	
	public SendEmailResponse() {
		super();
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public List<FailedRecipientInfo> getFailedRecipients() {
		return failedRecipients;
	}

	public void setFailedRecipients(List<FailedRecipientInfo> failedRecipients) {
		this.failedRecipients = failedRecipients;
	}

	public double getAverageSendingTime() {
		return averageSendingTime;
	}

	public void setAverageSendingTime(double averageSendingTime) {
		this.averageSendingTime = averageSendingTime;
	}

	public long getMinSendingTime() {
		return minSendingTime;
	}

	public void setMinSendingTime(long minSendingTime) {
		this.minSendingTime = minSendingTime;
	}

	public long getMaxSendingTime() {
		return maxSendingTime;
	}

	public void setMaxSendingTime(long maxSendingTime) {
		this.maxSendingTime = maxSendingTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SendEmailResponse [responseCode=" + responseCode + ", failedRecipients=" + failedRecipients
				+ ", averageSendingTime=" + averageSendingTime + ", minSendingTime=" + minSendingTime
				+ ", maxSendingTime=" + maxSendingTime + ", message=" + message + "]";
	}
	
	

}
