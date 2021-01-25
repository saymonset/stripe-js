package com.us.weavx.core.model;

public class ScheduledDonationStatusDescription {
	
	private int donation_status_id;
	private int lang_id;
	private String label;
	public ScheduledDonationStatusDescription() {
		super();
	}
	public int getDonation_status_id() {
		return donation_status_id;
	}
	public void setDonation_status_id(int donation_status_id) {
		this.donation_status_id = donation_status_id;
	}
	public int getLang_id() {
		return lang_id;
	}
	public void setLang_id(int lang_id) {
		this.lang_id = lang_id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "ScheduledDonationStatusDescription [donation_status_id=" + donation_status_id + ", lang_id=" + lang_id
				+ ", label=" + label + ", getDonation_status_id()=" + getDonation_status_id() + ", getLang_id()="
				+ getLang_id() + ", getLabel()=" + getLabel() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
