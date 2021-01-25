package com.us.weavx.core.model;

import java.sql.Timestamp;

public class EventCommissionSettings {

	private long id;
	private long customerId;
	private long applicationid;
	private double minimumCommission;
	private double maximumCommission;
	private int commissionTypeId;
	private int commissionPayerId;
	private double commissionValue;
	private double freeCommissionValue;
	private Timestamp createdAt;
	private Timestamp updatedAt;
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
	public long getApplicationid() {
		return applicationid;
	}
	public void setApplicationid(long applicationid) {
		this.applicationid = applicationid;
	}
	public double getMinimumCommission() {
		return minimumCommission;
	}
	public void setMinimumCommission(double minimumCommission) {
		this.minimumCommission = minimumCommission;
	}
	public double getMaximumCommission() {
		return maximumCommission;
	}
	public void setMaximumCommission(double maximumCommission) {
		this.maximumCommission = maximumCommission;
	}
	public int getCommissionTypeId() {
		return commissionTypeId;
	}
	public void setCommissionTypeId(int commissionTypeId) {
		this.commissionTypeId = commissionTypeId;
	}
	public int getCommissionPayerId() {
		return commissionPayerId;
	}
	public void setCommissionPayerId(int commissionPayerId) {
		this.commissionPayerId = commissionPayerId;
	}
	public double getCommissionValue() {
		return commissionValue;
	}
	public void setCommissionValue(double commissionValue) {
		this.commissionValue = commissionValue;
	}
	public double getFreeCommissionValue() {
		return freeCommissionValue;
	}
	public void setFreeCommissionValue(double freeCommissionValue) {
		this.freeCommissionValue = freeCommissionValue;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Override
	public String toString() {
		return "EventCommissionSettings [id=" + id + ", customerId=" + customerId + ", applicationid=" + applicationid
				+ ", minimumCommission=" + minimumCommission + ", maximumCommission=" + maximumCommission
				+ ", commissionTypeId=" + commissionTypeId + ", commissionPayerId=" + commissionPayerId
				+ ", commissionValue=" + commissionValue + ", freeCommissionValue=" + freeCommissionValue
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
	
	
}
