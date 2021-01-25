package com.us.weavx.core.model;

public class EventSettings {

	private long id;
	
	private long eventId;
	
	private String name;
	
	private String key;
	
	private String secret;

	public EventSettings() {
		super();
	}

	public EventSettings(long id, long eventId, String name, String key, String secret) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.name = name;
		this.key = key;
		this.secret = secret;
	}
	
	public EventSettings(long eventId, String name, String key, String secret) {
		super();
		this.eventId = eventId;
		this.name = name;
		this.key = key;
		this.secret = secret;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
}
