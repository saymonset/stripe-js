package com.us.weavx.core.model;

import java.sql.Timestamp;

public class CustomerUserPurchaseInfo {
	
	private long customerUserId;
	private String email;
	private String firstName;
	private String lastName;
	private Timestamp purchaseDate;
	private long purposeId;
	private String purposeName;
	private double amount;
	private Timestamp purposeValidFrom;
	private Timestamp purposeValidTo;
	private int langId;
	
	
	public CustomerUserPurchaseInfo() {
		super();
	}


	public CustomerUserPurchaseInfo(long customerUserId, String email, String firstName, String lastName,
			Timestamp purchaseDate, long purposeId, String purposeName, double amount, Timestamp purposeValidFrom,
			Timestamp purposeValidTo, int langId) {
		super();
		this.customerUserId = customerUserId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.purchaseDate = purchaseDate;
		this.purposeId = purposeId;
		this.purposeName = purposeName;
		this.amount = amount;
		this.purposeValidFrom = purposeValidFrom;
		this.purposeValidTo = purposeValidTo;
		this.langId = langId;
	}


	public long getCustomerUserId() {
		return customerUserId;
	}


	public void setCustomerUserId(long customerUserId) {
		this.customerUserId = customerUserId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Timestamp getPurchaseDate() {
		return purchaseDate;
	}


	public void setPurchaseDate(Timestamp purchaseDate) {
		this.purchaseDate = purchaseDate;
	}


	public long getPurposeId() {
		return purposeId;
	}


	public void setPurposeId(long purposeId) {
		this.purposeId = purposeId;
	}


	public String getPurposeName() {
		return purposeName;
	}


	public void setPurposeName(String purposeName) {
		this.purposeName = purposeName;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public Timestamp getPurposeValidFrom() {
		return purposeValidFrom;
	}


	public void setPurposeValidFrom(Timestamp purposeValidFrom) {
		this.purposeValidFrom = purposeValidFrom;
	}


	public Timestamp getPurposeValidTo() {
		return purposeValidTo;
	}


	public void setPurposeValidTo(Timestamp purposeValidTo) {
		this.purposeValidTo = purposeValidTo;
	}


	public int getLangId() {
		return langId;
	}


	public void setLangId(int langId) {
		this.langId = langId;
	}


	@Override
	public String toString() {
		return "CustomerUserPurchaseInfo [customerUserId=" + customerUserId + ", email=" + email + ", firstName="
				+ firstName + ", lastName=" + lastName + ", purchaseDate=" + purchaseDate + ", purposeId=" + purposeId
				+ ", purposeName=" + purposeName + ", amount=" + amount + ", purposeValidFrom=" + purposeValidFrom
				+ ", purposeValidTo=" + purposeValidTo + ", langId=" + langId + "]";
	}

	
			

}
