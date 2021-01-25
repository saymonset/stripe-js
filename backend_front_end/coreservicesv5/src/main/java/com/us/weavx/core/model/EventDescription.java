package com.us.weavx.core.model;

public class EventDescription {

	private long eventId;
	private int langId;
	private String eventLabel;
	private String eventDescription;
	private int tagId;
	private String tagDescription;
	
	public EventDescription(long eventId, int langId, String eventLabel, String eventDescription, int tagId, String tagDescription) {
		super();
		this.eventId = eventId;
		this.langId = langId;
		this.eventLabel = eventLabel;
		this.eventDescription = eventDescription;
		this.tagId = tagId;
		this.tagDescription = tagDescription;
	}
	
	public EventDescription(int langId, String eventLabel, String eventDescription, int tagId, String tagDescription) {
		super();
		this.langId = langId;
		this.eventLabel = eventLabel;
		this.eventDescription = eventDescription;
		this.tagId = tagId;
		this.tagDescription = tagDescription;
	}
	
	public EventDescription() {
		super();
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getEventLabel() {
		return eventLabel;
	}

	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
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
		return "EventDescription [eventId=" + eventId + ", langId=" + langId + ", eventLabel=" + eventLabel
				+ ", eventDescription=" + eventDescription + ", tagId=" + tagId + ", tagDescription=" + tagDescription
				+ "]";
	}


}
