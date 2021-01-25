package com.us.weavx.core.model;

import java.sql.Timestamp;

public class DonationSchedule {

	private long id;
	private long scheduled_donation_id;
	private Timestamp scheduled_date;
	private int scheduled_status;
	private Timestamp executed_at;
	
	public DonationSchedule() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getScheduled_donation_id() {
		return scheduled_donation_id;
	}

	public void setScheduled_donation_id(long scheduled_donation_id) {
		this.scheduled_donation_id = scheduled_donation_id;
	}

	public Timestamp getScheduled_date() {
		return scheduled_date;
	}

	public void setScheduled_date(Timestamp scheduled_date) {
		this.scheduled_date = scheduled_date;
	}

	public int getScheduled_status() {
		return scheduled_status;
	}

	public void setScheduled_status(int scheduled_status) {
		this.scheduled_status = scheduled_status;
	}

	public Timestamp getExecuted_at() {
		return executed_at;
	}

	public void setExecuted_at(Timestamp executed_at) {
		this.executed_at = executed_at;
	}

	@Override
	public String toString() {
		return "DonationSchedule [id=" + id + ", scheduled_donation_id=" + scheduled_donation_id + ", scheduled_date="
				+ scheduled_date + ", scheduled_status=" + scheduled_status + ", executed_at=" + executed_at + "]";
	}
	
	

}
