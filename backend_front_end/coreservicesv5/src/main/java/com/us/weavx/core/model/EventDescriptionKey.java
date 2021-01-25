package com.us.weavx.core.model;

public class EventDescriptionKey {

	private long eventId;
	private int langId;
	private int tagId;
	
	private EventDescriptionKey() {
		super();
	}
	
	public EventDescriptionKey(long eventId, int langId, int tagId) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (eventId ^ (eventId >>> 32));
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
		EventDescriptionKey other = (EventDescriptionKey) obj;
		if (eventId != other.eventId)
			return false;
		if (langId != other.langId)
			return false;
		if (tagId != other.tagId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventDescriptionKey [eventId=" + eventId + ", langId=" + langId + ", tagId=" + tagId + "]";
	}
		
}
