package com.us.weavx.core.model;

public class EventType {

	private int id;
	private String eventTypeName;
	private int langId;
	
	public EventType() {
		super();
	}

	public EventType(String eventTypeName) {
		super();
		this.eventTypeName = eventTypeName;
	}

	public EventType(int id, String eventTypeName) {
		super();
		this.id = id;
		this.eventTypeName = eventTypeName;
	}

	public EventType(int id, String eventTypeName, int langId) {
		super();
		this.id = id;
		this.eventTypeName = eventTypeName;
		this.langId = langId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	@Override
	public String toString() {
		return "EventType [id=" + id + ", eventTypeName=" + eventTypeName + ", langId=" + langId + "]";
	}

	
}
