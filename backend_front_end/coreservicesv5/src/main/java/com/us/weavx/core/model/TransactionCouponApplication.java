package com.us.weavx.core.model;

import java.sql.Timestamp;

public class TransactionCouponApplication {

	private long transactionId;
	private long couponId;
	private String applier;
	private Timestamp appliedAt;
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public long getCouponId() {
		return couponId;
	}
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}
	public String getApplier() {
		return applier;
	}
	public void setApplier(String applier) {
		this.applier = applier;
	}
	public Timestamp getAppliedAt() {
		return appliedAt;
	}
	public void setAppliedAt(Timestamp appliedAt) {
		this.appliedAt = appliedAt;
	}
	@Override
	public String toString() {
		return "TransactionCouponApplication [transactionId=" + transactionId + ", couponId=" + couponId + ", applier="
				+ applier + ", appliedAt=" + appliedAt + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (couponId ^ (couponId >>> 32));
		result = prime * result + (int) (transactionId ^ (transactionId >>> 32));
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
		TransactionCouponApplication other = (TransactionCouponApplication) obj;
		if (couponId != other.couponId)
			return false;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}
	
	
	
	

}
