package com.us.weavx.core.model;

import java.util.List;

public class ScheduledDonationInfo implements Comparable {
	
	private ScheduledDonation scheduledDonation;
	private List<ScheduledDonationFundDetail> funds;
	public ScheduledDonationInfo() {
		super();
	}
	public ScheduledDonation getScheduledDonation() {
		return scheduledDonation;
	}
	public void setScheduledDonation(ScheduledDonation scheduledDonation) {
		this.scheduledDonation = scheduledDonation;
	}
	public List<ScheduledDonationFundDetail> getFunds() {
		return funds;
	}
	public void setFunds(List<ScheduledDonationFundDetail> funds) {
		this.funds = funds;
	}
	@Override
	public String toString() {
		return "ScheduledDonationInfo [scheduledDonation=" + scheduledDonation + ", funds=" + funds + "]";
	}
	@Override
	public int compareTo(Object arg0) {
		ScheduledDonationInfo d2 = (ScheduledDonationInfo) arg0;
		return Long.valueOf(this.scheduledDonation.getId()).compareTo(d2.getScheduledDonation().getId());
	}
	
	
	

}
