package com.us.weavx.core.model;

import java.sql.Timestamp;

public class CouponLog {
	
	private long id;
	private int couponStatusId;
	private int couponEventId;
	private Timestamp eventDate;
	private long couponId;
	private String userEmail;
	private String userAgent;
	private String ipAddress;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCouponStatusId() {
		return couponStatusId;
	}
	public void setCouponStatusId(int couponStatusId) {
		this.couponStatusId = couponStatusId;
	}
	public int getCouponEventId() {
		return couponEventId;
	}
	public void setCouponEventId(int couponEventId) {
		this.couponEventId = couponEventId;
	}
	public Timestamp getEventDate() {
		return eventDate;
	}
	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}
	public long getCouponId() {
		return couponId;
	}
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	@Override
	public String toString() {
		return "CouponLog [id=" + id + ", couponStatusId=" + couponStatusId + ", couponEventId=" + couponEventId
				+ ", eventDate=" + eventDate + ", couponId=" + couponId + ", userEmail=" + userEmail + ", userAgent="
				+ userAgent + ", ipAddress=" + ipAddress + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + couponEventId;
		result = prime * result + (int) (couponId ^ (couponId >>> 32));
		result = prime * result + couponStatusId;
		result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((userAgent == null) ? 0 : userAgent.hashCode());
		result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
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
		CouponLog other = (CouponLog) obj;
		if (couponEventId != other.couponEventId)
			return false;
		if (couponId != other.couponId)
			return false;
		if (couponStatusId != other.couponStatusId)
			return false;
		if (eventDate == null) {
			if (other.eventDate != null)
				return false;
		} else if (!eventDate.equals(other.eventDate))
			return false;
		if (id != other.id)
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (userAgent == null) {
			if (other.userAgent != null)
				return false;
		} else if (!userAgent.equals(other.userAgent))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		return true;
	}
	
	

}
