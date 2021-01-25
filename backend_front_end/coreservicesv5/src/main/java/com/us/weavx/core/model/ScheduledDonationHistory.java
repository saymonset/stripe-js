package com.us.weavx.core.model;

import java.sql.Timestamp;

public class ScheduledDonationHistory {
	
	private long scheduled_donation_id;
	private Timestamp executed_at;
	private long transaction_id;
	private boolean success_donation;
	public ScheduledDonationHistory() {
		super();
	}
	public long getScheduled_donation_id() {
		return scheduled_donation_id;
	}
	public void setScheduled_donation_id(long scheduled_donation_id) {
		this.scheduled_donation_id = scheduled_donation_id;
	}
	public Timestamp getExecuted_at() {
		return executed_at;
	}
	public void setExecuted_at(Timestamp executed_at) {
		this.executed_at = executed_at;
	}
	public long getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(long transaction_id) {
		this.transaction_id = transaction_id;
	}
	public boolean isSuccess_donation() {
		return success_donation;
	}
	public void setSuccess_donation(boolean success_donation) {
		this.success_donation = success_donation;
	}
	@Override
	public String toString() {
		return "ScheduledDonationHistory [scheduled_donation_id=" + scheduled_donation_id + ", executed_at="
				+ executed_at + ", transaction_id=" + transaction_id + ", success_donation=" + success_donation + "]";
	}
	
	

}
