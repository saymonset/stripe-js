package com.us.weavx.core.model;

public class CustomerSystemMessage {

	private long customerId;
	private long systemMessageId;
	private int	langId;
	private String message;
	
	public CustomerSystemMessage() {
		
	}

	public CustomerSystemMessage(long customerId, long systemMessageId, int langId, String message) {
		super();
		this.customerId = customerId;
		this.systemMessageId = systemMessageId;
		this.langId = langId;
		this.message = message;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getSystemMessageId() {
		return systemMessageId;
	}

	public void setSystemMessageId(long systemMessageId) {
		this.systemMessageId = systemMessageId;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CustomerSystemMessage [customerId=" + customerId + ", systemMessageId=" + systemMessageId + ", langId="
				+ langId + ", message=" + message + "]";
	}
	
	

}
