package com.us.weavx.core.model;

import java.sql.Timestamp;

public class DonationAmount {
	private long id;
	private long customerId;
	private Double amount;
	private Timestamp validFrom;
	private Timestamp validTo;
	public DonationAmount(long id, long customerId, Double amount) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.amount = amount;
	}
	public DonationAmount(long customerId, Double amount) {
		super();
		this.customerId = customerId;
		this.amount = amount;
		this.id = -1;
	}
	
	
	public DonationAmount(long id, long customerId, Double amount, Timestamp validFrom, Timestamp validTo) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.amount = amount;
		this.validFrom = validFrom;
		this.validTo = validTo;
	}
	public DonationAmount() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
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
	@Override
	public String toString() {
		return "DonationAmount [id=" + id + ", customerId=" + customerId + ", amount=" + amount + ", validFrom="
				+ validFrom + ", validTo=" + validTo + "]";
	}
	
	
	
	

}
