package com.us.weavx.core.model;

import java.sql.Timestamp;

public class EventFundSettings {
	
	private long id;
	private long customerId;
	private long applicationId;
	private long fundId;
	private int allowedDaysToAccess;
	private Timestamp startDate;
	private Timestamp endDate;
	private Boolean signatureRequired;
	private String signatureDocumentId;
	private double price;
	private int minimum;
	private Boolean eventType;
	private Long eventId;
	private int capacity;
	
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
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public long getFundId() {
		return fundId;
	}
	public void setFundId(long fundId) {
		this.fundId = fundId;
	}
	public int getAllowedDaysToAccess() {
		return allowedDaysToAccess;
	}
	public void setAllowedDaysToAccess(int allowedDaysToAccess) {
		this.allowedDaysToAccess = allowedDaysToAccess;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public Boolean getSignatureRequired() {
		return signatureRequired;
	}
	public void setSignatureRequired(Boolean signatureRequired) {
		this.signatureRequired = signatureRequired;
	}
	public String getSignatureDocumentId() {
		return signatureDocumentId;
	}
	public void setSignatureDocumentId(String signatureDocumentId) {
		this.signatureDocumentId = signatureDocumentId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getMinimum() {
		return minimum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	public Boolean getEventType() {
		return eventType;
	}
	public void setEventType(Boolean eventType) {
		this.eventType = eventType;
	}	
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	@Override
	public String toString() {
		return "EventFundSettings [id=" + id + ", customerId=" + customerId + ", applicationId=" + applicationId
				+ ", fundId=" + fundId + ", allowedDaysToAccess=" + allowedDaysToAccess + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", signatureRequired=" + signatureRequired + ", signatureDocumentId="
				+ signatureDocumentId + ", price=" + price + ", minimum=" + minimum + ", eventType=" + eventType
				+ ", eventId=" + eventId + ", capacity=" + capacity + "]";
	}
	
	

}
