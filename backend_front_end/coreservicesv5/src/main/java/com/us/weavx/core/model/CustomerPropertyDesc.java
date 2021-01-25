package com.us.weavx.core.model;

import java.util.Objects;

public class CustomerPropertyDesc {

	private long customerId;
	private int langId;
	private String propertyName;
	private String propertyValue;
	
	public CustomerPropertyDesc() {
		// TODO Auto-generated constructor stub
	}

	public CustomerPropertyDesc(long customerId, int langId, String propertyName, String propertyValue) {
		super();
		this.customerId = customerId;
		this.langId = langId;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	@Override
	public String toString() {
		return "CustomerPropertyDesc [customerId=" + customerId + ", langId=" + langId + ", propertyName="
				+ propertyName + ", propertyValue=" + propertyValue + "]";
	}

	@Override
	public boolean equals(Object obj) {
		try {
			CustomerPropertyDesc cp = (CustomerPropertyDesc) obj;
			return (this.customerId == cp.getCustomerId() && this.langId == cp.getLangId() && this.propertyName.equals(cp.getPropertyName()));
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, langId, propertyName);
	}
	
	

}
