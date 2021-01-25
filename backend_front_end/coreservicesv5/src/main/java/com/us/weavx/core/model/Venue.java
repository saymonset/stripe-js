package com.us.weavx.core.model;

public class Venue {

	private long id;
	private long eventId;
	private String description;
	private String address;
	private int capacity;
	
	public Venue(long id, long eventId, String description, String address, int capacity) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.description = description;
		this.address = address;
		this.capacity = capacity;
	}
	
	public Venue(int eventId, String description, String address, int capacity) {
		super();
		this.eventId = eventId;
		this.description = description;
		this.address = address;
		this.capacity = capacity;
	}
	
	public Venue() {
		super();
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "VenueDescription [id=" + id + ", eventId=" + eventId + ", description=" + description + ", address="
				+ address + ", capacity=" + capacity + "]";
	}

}
