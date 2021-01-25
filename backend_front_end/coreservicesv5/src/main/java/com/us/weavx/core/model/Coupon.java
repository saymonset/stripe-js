package com.us.weavx.core.model;

public class Coupon {
	
	private long id;
	private String code;
	private int couponStatusId;
	private String applier;
	private long appliedTimes;
	private long couponPromotionid;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getCouponStatusId() {
		return couponStatusId;
	}
	public void setCouponStatusId(int couponStatusId) {
		this.couponStatusId = couponStatusId;
	}
	public String getApplier() {
		return applier;
	}
	public void setApplier(String applier) {
		this.applier = applier;
	}
	public long getAppliedTimes() {
		return appliedTimes;
	}
	public void setAppliedTimes(long appliedTimes) {
		this.appliedTimes = appliedTimes;
	}
	public long getCouponPromotionid() {
		return couponPromotionid;
	}
	public void setCouponPromotionid(long couponPromotionid) {
		this.couponPromotionid = couponPromotionid;
	}
	@Override
	public String toString() {
		return "Coupon [id=" + id + ", code=" + code + ", couponStatusId=" + couponStatusId + ", applier=" + applier
				+ ", appliedTimes=" + appliedTimes + ", couponPromotionid=" + couponPromotionid + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (appliedTimes ^ (appliedTimes >>> 32));
		result = prime * result + ((applier == null) ? 0 : applier.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + (int) (couponPromotionid ^ (couponPromotionid >>> 32));
		result = prime * result + couponStatusId;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Coupon other = (Coupon) obj;
		if (appliedTimes != other.appliedTimes)
			return false;
		if (applier == null) {
			if (other.applier != null)
				return false;
		} else if (!applier.equals(other.applier))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (couponPromotionid != other.couponPromotionid)
			return false;
		if (couponStatusId != other.couponStatusId)
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
