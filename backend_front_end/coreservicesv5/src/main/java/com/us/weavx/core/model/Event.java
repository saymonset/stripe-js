package com.us.weavx.core.model;

import java.util.Date;

public class Event {

	private long id;	
	private long applicationId;	
	private String name;	
	private String speaker;	
	private Date startDate;	
	private Date endDate;
	
	public Event() {
		super();
	}	
	
	public Event(long id, long applicationId, String name, String speaker, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.applicationId = applicationId;
		this.name = name;
		this.speaker = speaker;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Event(int applicationId, String name, String speaker, Date startDate, Date endDate) {
		super();
		this.applicationId = applicationId;
		this.name = name;
		this.speaker = speaker;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpeaker() {
		return speaker;
	}
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", applicationId=" + applicationId + ", name=" + name + ", speaker=" + speaker
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
}
