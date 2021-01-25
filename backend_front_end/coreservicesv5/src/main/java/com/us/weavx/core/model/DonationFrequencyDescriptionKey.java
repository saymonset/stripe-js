package com.us.weavx.core.model;

public class DonationFrequencyDescriptionKey {
	
	private int donationFrequencyId;
	private int langId;
	public DonationFrequencyDescriptionKey() {
		super();
	}
	public DonationFrequencyDescriptionKey(int donationFrequencyId, int langId) {
		super();
		this.donationFrequencyId = donationFrequencyId;
		this.langId = langId;
	}
	public int getDonationFrequencyId() {
		return donationFrequencyId;
	}
	public void setDonationFrequencyId(int donationFrequencyId) {
		this.donationFrequencyId = donationFrequencyId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + donationFrequencyId;
		result = prime * result + langId;
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
		DonationFrequencyDescriptionKey other = (DonationFrequencyDescriptionKey) obj;
		if (donationFrequencyId != other.donationFrequencyId)
			return false;
		if (langId != other.langId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DonationFrequencyDescriptionKey [donationFrequencyId=" + donationFrequencyId + ", langId=" + langId
				+ "]";
	}
	
	

}
