package com.us.weavx.core.model;

public class EventTypeDescription {
	
	private int eventId;
	private int langId;
	private String eventLabel;
	private String eventDescription;
	
	
	public EventTypeDescription() {
		super();
	}

	public EventTypeDescription(int eventId, int langId, String eventLabel, String eventDescription) {
		super();
		this.eventId = eventId;
		this.langId = langId;
		this.eventLabel = eventLabel;
		this.eventDescription = eventDescription;
	}

	public EventTypeDescription(int langId, String eventLabel, String eventDescription) {
		super();
		this.langId = langId;
		this.eventLabel = eventLabel;
		this.eventDescription = eventDescription;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
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

	@Override
	public String toString() {
		return "EventTypeDescription [eventId=" + eventId + ", langId=" + langId + ", eventLabel=" + eventLabel
				+ ", eventDescription=" + eventDescription + "]";
	}
	
	

}
