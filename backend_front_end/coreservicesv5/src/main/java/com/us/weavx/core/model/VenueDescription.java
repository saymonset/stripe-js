package com.us.weavx.core.model;

public class VenueDescription {
	
	private int venueId;
	private int langId;
	private String venueLabel;
	private String venueDescription;
	private int tagId;
	private String tagDescription;
	
	
	public VenueDescription(int venueId, int langId, String venueLabel, String venueDescription, int tagId, String tagDescription) {
		super();
		this.venueId = venueId;
		this.langId = langId;
		this.venueLabel = venueLabel;
		this.venueDescription = venueDescription;
		this.tagId = tagId;
		this.tagDescription = tagDescription;
	}
	
	public VenueDescription(int langId, String venueLabel, String venueDescription, int tagId, String tagDescription) {
		super();
		this.langId = langId;
		this.venueLabel = venueLabel;
		this.venueDescription = venueDescription;
		this.tagId = tagId;
		this.tagDescription = tagDescription;
	}
	
	public VenueDescription() {
		super();
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

	public String getVenueLabel() {
		return venueLabel;
	}

	public void setVenueLabel(String venueLabel) {
		this.venueLabel = venueLabel;
	}

	public String getVenueDescription() {
		return venueDescription;
	}

	public void setVenueDescription(String venueDescription) {
		this.venueDescription = venueDescription;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getTagDescription() {
		return tagDescription;
	}

	public void setTagDescription(String tagDescription) {
		this.tagDescription = tagDescription;
	}

	@Override
	public String toString() {
		return "VenueDescription [venueId=" + venueId + ", langId=" + langId + ", venueLabel=" + venueLabel
				+ ", venueDescription=" + venueDescription + ", tagId=" + tagId + ", tagDescription=" + tagDescription
				+ "]";
	}

}
