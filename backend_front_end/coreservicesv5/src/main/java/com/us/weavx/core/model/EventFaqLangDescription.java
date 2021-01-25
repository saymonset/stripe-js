package com.us.weavx.core.model;

public class EventFaqLangDescription {

	private long eventId;
	private int langId;
	private int tagId;
	private String description;
	private String label;
	private String tagDescription;
	
	public EventFaqLangDescription() {
		super();
	}

	public EventFaqLangDescription(long eventId, int langId, int tagId,  String description,
			String label, String tagDescription) {
		super();
		this.eventId = eventId;
		this.langId = langId;
		this.tagId = tagId;
		this.description = description;
		this.label = label;
		this.tagDescription = tagDescription;
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

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTagDescription() {
		return tagDescription;
	}

	public void setTagDescription(String tagDescription) {
		this.tagDescription = tagDescription;
	}	
	
}
