package com.us.weavx.core.model;

public class EventFaqLangDescriptionKey {

	private long eventId;
	private int langId;
	private int tagId;
	
	public EventFaqLangDescriptionKey() {
		super();
	}

	public EventFaqLangDescriptionKey(long eventId, int langId, int tagId) {
		super();
		this.eventId = eventId;
		this.langId = langId;
		this.tagId = tagId;
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
}
