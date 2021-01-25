package com.us.weavx.core.model;

public class ScheduledDonationFundDetail {
	
	private long scheduled_donation_id;
	private int fund_id;
	private double amount;
	public ScheduledDonationFundDetail() {
		super();
	}
	public long getScheduled_donation_id() {
		return scheduled_donation_id;
	}
	public void setScheduled_donation_id(long scheduled_donation_id) {
		this.scheduled_donation_id = scheduled_donation_id;
	}
	public int getFund_id() {
		return fund_id;
	}
	public void setFund_id(int fund_id) {
		this.fund_id = fund_id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "ScheduledDonationFundDetail [scheduled_donation_id=" + scheduled_donation_id + ", fund_id=" + fund_id
				+ ", amount=" + amount + "]";
	}
	
	

}
