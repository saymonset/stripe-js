package com.us.weavx.core.model;

import java.sql.Timestamp;

public class ScheduledDonation {
	private long id;
	private Timestamp created_at;
	private Timestamp starts_at;
	private Timestamp last_successful;
	private Timestamp last_failed;
	private Timestamp next_date;
	private long customer_user_id;
	private int donation_status_id;
	private int donation_frequency_id;
	private double amount;
	private int lang_id;
	public ScheduledDonation() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public Timestamp getStarts_at() {
		return starts_at;
	}
	public void setStarts_at(Timestamp starts_at) {
		this.starts_at = starts_at;
	}
	public Timestamp getLast_successful() {
		return last_successful;
	}
	public void setLast_successful(Timestamp last_successful) {
		this.last_successful = last_successful;
	}
	public Timestamp getLast_failed() {
		return last_failed;
	}
	public void setLast_failed(Timestamp last_failed) {
		this.last_failed = last_failed;
	}
	public Timestamp getNext_date() {
		return next_date;
	}
	public void setNext_date(Timestamp next_date) {
		this.next_date = next_date;
	}
	public long getCustomer_user_id() {
		return customer_user_id;
	}
	public void setCustomer_user_id(long customer_user_id) {
		this.customer_user_id = customer_user_id;
	}
	public int getDonation_status_id() {
		return donation_status_id;
	}
	public void setDonation_status_id(int donation_status_id) {
		this.donation_status_id = donation_status_id;
	}
	public int getDonation_frequency_id() {
		return donation_frequency_id;
	}
	public void setDonation_frequency_id(int donation_frequency_id) {
		this.donation_frequency_id = donation_frequency_id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getLang_id() {
		return lang_id;
	}
	public void setLang_id(int lang_id) {
		this.lang_id = lang_id;
	}
	@Override
	public String toString() {
		return "ScheduledDonation [id=" + id + ", created_at=" + created_at + ", starts_at=" + starts_at
				+ ", last_successful=" + last_successful + ", last_failed=" + last_failed + ", next_date=" + next_date
				+ ", customer_user_id=" + customer_user_id + ", donation_status_id=" + donation_status_id
				+ ", donation_frequency_id=" + donation_frequency_id + ", amount=" + amount + ", lang_id=" + lang_id
				+ "]";
	}
	
	
	
	

}
