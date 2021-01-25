package com.us.weavx.core.model;

public class VenueDescriptionKey {

	private int venueId;
	private int langId;
	private int tagId;
	
	public VenueDescriptionKey() {
		super();
	}
	public VenueDescriptionKey(int venueId, int langId, int tagId) {
		super();
		this.venueId = venueId;
		this.langId = langId;
		this.tagId = tagId;
	}
	public int getVenueId() {
		return venueId;
	}
	public void setVenueId(int venueId) {
		this.venueId = venueId;
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
		result = prime * result + langId;
		result = prime * result + tagId;
		result = prime * result + venueId;
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
		VenueDescriptionKey other = (VenueDescriptionKey) obj;
		if (langId != other.langId)
			return false;
		if (tagId != other.tagId)
			return false;
		if (venueId != other.venueId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "VenueDescriptionKey [venueId=" + venueId + ", langId=" + langId + ", tagId=" + tagId + "]";
	}
	
	
}
