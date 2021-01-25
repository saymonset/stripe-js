package com.us.weavx.core.model;

import java.sql.Timestamp;

public class Fund {
	
	private int id;
	private long customerId;
	private String businessCode;
	private boolean isDefault;
	private String name;
	private Timestamp validFrom;
	private Timestamp validTo;
	private boolean isSchedulable;
	
	public Fund(int id, long customerId, String businessCode, boolean isDefault, String name) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.businessCode = businessCode;
		this.isDefault = isDefault;
		this.name = name;
	}
	
	
	
	public Fund(int id, long customerId, String businessCode, boolean isDefault, String name, Timestamp validFrom,
			Timestamp validTo) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.businessCode = businessCode;
		this.isDefault = isDefault;
		this.name = name;
		this.validFrom = validFrom;
		this.validTo = validTo;
	}

	public Fund(int id, long customerId, String businessCode, String name) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.businessCode = businessCode;
		this.isDefault = false;
		this.name = name;
	}


	public Fund(long customerId, String businessCode, String name) {
		super();
		this.customerId = customerId;
		this.businessCode = businessCode;
		this.id = -1;
		this.name = name;
	}
	public Fund() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}


	public boolean isDefault() {
		return isDefault;
	}


	public void setDefault(boolean isSDefault) {
		this.isDefault = isSDefault;
	}
	
	


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	


	public Timestamp getValidFrom() {
		return validFrom;
	}



	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}



	public Timestamp getValidTo() {
		return validTo;
	}



	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}



	public boolean isSchedulable() {
		return isSchedulable;
	}



	public void setIsSchedulable(boolean isScheludable) {
		this.isSchedulable = isScheludable;
	}



	@Override
	public String toString() {
		return "Fund [id=" + id + ", customerId=" + customerId + ", businessCode=" + businessCode + ", isDefault="
				+ isDefault + ", name=" + name + ", validFrom=" + validFrom + ", validTo=" + validTo + "]";
	}



	

	
	
}
