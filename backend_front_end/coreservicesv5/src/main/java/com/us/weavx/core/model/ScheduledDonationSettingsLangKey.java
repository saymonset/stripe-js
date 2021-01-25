package com.us.weavx.core.model;

public class ScheduledDonationSettingsLangKey {

	private long customer_id;
	private long application_id;
	private int lang_id;
	
	public ScheduledDonationSettingsLangKey() {
		
	}

	
	public ScheduledDonationSettingsLangKey(long customer_id, long application_id, int lang_id) {
		super();
		this.customer_id = customer_id;
		this.application_id = application_id;
		this.lang_id = lang_id;
	}


	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

	public long getApplication_id() {
		return application_id;
	}

	public void setApplication_id(long application_id) {
		this.application_id = application_id;
	}

	public int getLang_id() {
		return lang_id;
	}

	public void setLang_id(int lang_id) {
		this.lang_id = lang_id;
	}

	@Override
	public String toString() {
		return "ScheduledDonationSettingsLangKey [customer_id=" + customer_id + ", application_id=" + application_id
				+ ", lang_id=" + lang_id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (application_id ^ (application_id >>> 32));
		result = prime * result + (int) (customer_id ^ (customer_id >>> 32));
		result = prime * result + lang_id;
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
		ScheduledDonationSettingsLangKey other = (ScheduledDonationSettingsLangKey) obj;
		if (application_id != other.application_id)
			return false;
		if (customer_id != other.customer_id)
			return false;
		if (lang_id != other.lang_id)
			return false;
		return true;
	}
	
	

}
