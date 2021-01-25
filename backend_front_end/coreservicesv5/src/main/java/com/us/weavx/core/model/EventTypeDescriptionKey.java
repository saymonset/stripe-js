package com.us.weavx.core.model;

public class EventTypeDescriptionKey {
	
	private int fundId;
	private int langId;
	public EventTypeDescriptionKey() {
		super();
	}
	public EventTypeDescriptionKey(int fundId, int langId) {
		super();
		this.fundId = fundId;
		this.langId = langId;
	}
	public int getFundId() {
		return fundId;
	}
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	@Override
	public String toString() {
		return "FundDescriptionKey [fundId=" + fundId + ", langId=" + langId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fundId;
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
		EventTypeDescriptionKey other = (EventTypeDescriptionKey) obj;
		if (fundId != other.fundId)
			return false;
		if (langId != other.langId)
			return false;
		return true;
	}
	
	

}
