package com.us.weavx.core.model;

import java.util.Date;

public class EventRRSS {

	private long id;
	private long eventId;
	private String url;
	private String image;
	private String description;
	private Date createdAt;
	
	public EventRRSS() {
		super();
	}

	public EventRRSS(long id, long eventId, String url, String image, String description, Date createdAt) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.url = url;
		this.image = image;
		this.description = description;
		this.createdAt = createdAt;
	}
	
	public EventRRSS(long eventId, String url, String image, String description, Date createdAt) {
		super();
		this.eventId = eventId;
		this.url = url;
		this.image = image;
		this.description = description;
		this.createdAt = createdAt;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "EventRRSS [id=" + id + ", eventId=" + eventId + ", url=" + url + ", image=" + image + ", description="
				+ description + ", createdAt=" + createdAt + "]";
	}
}
