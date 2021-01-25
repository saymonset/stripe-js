package com.us.weavx.core.model;

public class GeneralDescriptionKey {

	private long customerId;
	private int langId;
	private int tagId;
	
	private GeneralDescriptionKey() {
		super();
	}
	
	public GeneralDescriptionKey(long customerId, int langId, int tagId) {
		super();
		this.customerId = customerId;
		this.langId = langId;
		this.tagId = tagId;
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}	

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + langId;
		result = prime * result + tagId;
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
		GeneralDescriptionKey other = (GeneralDescriptionKey) obj;
		if (customerId != other.customerId)
			return false;
		if (langId != other.langId)
			return false;
		if (tagId != other.tagId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeneralDescriptionKey [customerId=" + customerId + ", langId=" + langId + ", tagId=" + tagId + "]";
	}

	
}
