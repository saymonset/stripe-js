package com.us.weavx.core.model;

import java.sql.Timestamp;

public class CouponPromotion {
	private long id;
	private String name;
	private long createdByCustomerId;
	private Timestamp createdAt;
	private int discountTypeId;
	private double discountAmount;
	private long maxCoupons;
	private long currentCoupons;
	private Timestamp validFrom;
	private Timestamp validTo;
	private long applicationLimit;
	private long userApplicationLimit;
	private boolean isGeneric;
	private boolean isEnabled;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCreatedByCustomerId() {
		return createdByCustomerId;
	}
	public void setCreatedByCustomerId(long createdByCustomerId) {
		this.createdByCustomerId = createdByCustomerId;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public int getDiscountTypeId() {
		return discountTypeId;
	}
	public void setDiscountTypeId(int discountTypeId) {
		this.discountTypeId = discountTypeId;
	}
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public long getMaxCoupons() {
		return maxCoupons;
	}
	public void setMaxCoupons(long maxCoupons) {
		this.maxCoupons = maxCoupons;
	}
	public long getCurrentCoupons() {
		return currentCoupons;
	}
	public void setCurrentCoupons(long currentCoupons) {
		this.currentCoupons = currentCoupons;
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
	public long getApplicationLimit() {
		return applicationLimit;
	}
	public void setApplicationLimit(long applicationLimit) {
		this.applicationLimit = applicationLimit;
	}
	public long getUserApplicationLimit() {
		return userApplicationLimit;
	}
	public void setUserApplicationLimit(long userApplicationLimit) {
		this.userApplicationLimit = userApplicationLimit;
	}
	public boolean isGeneric() {
		return isGeneric;
	}
	public void setGeneric(boolean isGeneric) {
		this.isGeneric = isGeneric;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	@Override
	public String toString() {
		return "CouponPromotion [id=" + id + ", name=" + name + ", createdByCustomerId=" + createdByCustomerId
				+ ", createdAt=" + createdAt + ", discountTypeId=" + discountTypeId + ", discountAmount="
				+ discountAmount + ", maxCoupons=" + maxCoupons + ", currentCoupons=" + currentCoupons + ", validFrom="
				+ validFrom + ", validTo=" + validTo + ", applicationLimit=" + applicationLimit
				+ ", userApplicationLimit=" + userApplicationLimit + ", isGeneric=" + isGeneric + ", isEnabled="
				+ isEnabled + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (applicationLimit ^ (applicationLimit >>> 32));
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + (int) (createdByCustomerId ^ (createdByCustomerId >>> 32));
		result = prime * result + (int) (currentCoupons ^ (currentCoupons >>> 32));
		long temp;
		temp = Double.doubleToLongBits(discountAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + discountTypeId;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (isEnabled ? 1231 : 1237);
		result = prime * result + (isGeneric ? 1231 : 1237);
		result = prime * result + (int) (maxCoupons ^ (maxCoupons >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (userApplicationLimit ^ (userApplicationLimit >>> 32));
		result = prime * result + ((validFrom == null) ? 0 : validFrom.hashCode());
		result = prime * result + ((validTo == null) ? 0 : validTo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CouponPromotion other = (CouponPromotion) obj;
		if (applicationLimit != other.applicationLimit)
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (createdByCustomerId != other.createdByCustomerId)
			return false;
		if (currentCoupons != other.currentCoupons)
			return false;
		if (Double.doubleToLongBits(discountAmount) != Double.doubleToLongBits(other.discountAmount))
			return false;
		if (discountTypeId != other.discountTypeId)
			return false;
		if (id != other.id)
			return false;
		if (isEnabled != other.isEnabled)
			return false;
		if (isGeneric != other.isGeneric)
			return false;
		if (maxCoupons != other.maxCoupons)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (userApplicationLimit != other.userApplicationLimit)
			return false;
		if (validFrom == null) {
			if (other.validFrom != null)
				return false;
		} else if (!validFrom.equals(other.validFrom))
			return false;
		if (validTo == null) {
			if (other.validTo != null)
				return false;
		} else if (!validTo.equals(other.validTo))
			return false;
		return true;
	}
	
	

}
