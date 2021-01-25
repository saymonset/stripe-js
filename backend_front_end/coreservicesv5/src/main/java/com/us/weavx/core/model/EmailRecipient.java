
package com.us.weavx.core.model;

import java.util.Map;

public class EmailRecipient {

	public static final int TO = 1;
	public static final int CC = 2;
	public static final int BCC = 3;
	
	private int type = TO;
	private String name;
	private String email;
	private Map<String, String> recipientInfo;
	
	public EmailRecipient() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, String> getRecipientInfo() {
		return recipientInfo;
	}

	public void setRecipientInfo(Map<String, String> recipientInfo) {
		this.recipientInfo = recipientInfo;
	}
	
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "EmailRecipient [type=" + type + ", name=" + name + ", email=" + email + ", recipientInfo="
				+ recipientInfo + "]";
	}

	


	
	
}