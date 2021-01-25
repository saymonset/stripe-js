package com.us.weavx.core.model;

public class DonationStatusDescriptionKey {
	
	private int donationStatusId;
	private int langId;
	
	
	public DonationStatusDescriptionKey() {
		super();
	}


	public DonationStatusDescriptionKey(int donationStatusId, int langId) {
		super();
		this.donationStatusId = donationStatusId;
		this.langId = langId;
	}


	public int getDonationStatusId() {
		return donationStatusId;
	}


	public void setDonationStatusId(int donationStatusId) {
		this.donationStatusId = donationStatusId;
	}


	public int getLangId() {
		return langId;
	}


	public void setLangId(int langId) {
		this.langId = langId;
	}


	@Override
	public String toString() {
		return "DonationStatusDescriptionKey [donationStatusId=" + donationStatusId + ", langId=" + langId + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + donationStatusId;
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
		DonationStatusDescriptionKey other = (DonationStatusDescriptionKey) obj;
		if (donationStatusId != other.donationStatusId)
			return false;
		if (langId != other.langId)
			return false;
		return true;
	}
	
	

}
