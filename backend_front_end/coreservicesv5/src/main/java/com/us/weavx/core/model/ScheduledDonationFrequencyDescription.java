package com.us.weavx.core.model;

public class ScheduledDonationFrequencyDescription {
	
	private int donation_frequency_id;
	private int lang_id;
	private String label;
	public ScheduledDonationFrequencyDescription() {
		super();
	}
	public int getDonation_frequency_id() {
		return donation_frequency_id;
	}
	public void setDonation_frequency_id(int donation_frequency_id) {
		this.donation_frequency_id = donation_frequency_id;
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
		return "ScheduledDonationFrequencyDescription [donation_frequency_id=" + donation_frequency_id + ", lang_id="
				+ lang_id + ", label=" + label + "]";
	}
	
	

}
