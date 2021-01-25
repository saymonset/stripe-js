package com.us.weavx.core.model;

public class CustomerProperty {

	private long customerId;
	private int	propertyId;
	private int langId;
	private String propertyValue;
	
	
	
	public CustomerProperty(long customerId, int propertyId, int langId, String propertyValue) {
		super();
		this.customerId = customerId;
		this.propertyId = propertyId;
		this.langId = langId;
		this.propertyValue = propertyValue;
	}



	public CustomerProperty() {
		// TODO Auto-generated constructor stub
	}



	public long getCustomerId() {
		return customerId;
	}



	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}



	public int getPropertyId() {
		return propertyId;
	}



	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}



	public int getLangId() {
		return langId;
	}



	public void setLangId(int langId) {
		this.langId = langId;
	}



	public String getPropertyValue() {
		return propertyValue;
	}



	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}



	@Override
	public String toString() {
		return "CustomerProperty [customerId=" + customerId + ", propertyId=" + propertyId + ", langId=" + langId
				+ ", propertyValue=" + propertyValue + "]";
	}
	
	

}
