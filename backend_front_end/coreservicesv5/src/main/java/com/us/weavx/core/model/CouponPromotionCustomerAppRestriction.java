package com.us.weavx.core.model;

import java.sql.Timestamp;

public class CouponPromotionCustomerAppRestriction {
	
	private long customerId;
	private long applicationId;
	private long couponPromotionId;
	private Timestamp createdAt;
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
	public long getCouponPromotionId() {
		return couponPromotionId;
	}
	public void setCouponPromotionId(long couponPromotionId) {
		this.couponPromotionId = couponPromotionId;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "CouponPromotionCustomerAppRestriction [customerId=" + customerId + ", applicationId=" + applicationId
				+ ", couponPromotionId=" + couponPromotionId + ", createdAt=" + createdAt + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (applicationId ^ (applicationId >>> 32));
		result = prime * result + (int) (couponPromotionId ^ (couponPromotionId >>> 32));
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
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
		CouponPromotionCustomerAppRestriction other = (CouponPromotionCustomerAppRestriction) obj;
		if (applicationId != other.applicationId)
			return false;
		if (couponPromotionId != other.couponPromotionId)
			return false;
		if (customerId != other.customerId)
			return false;
		return true;
	}
	
	
}
