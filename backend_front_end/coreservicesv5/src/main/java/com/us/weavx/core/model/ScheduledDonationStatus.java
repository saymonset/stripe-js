package com.us.weavx.core.model;

public class ScheduledDonationStatus {
	
	public static final int PENDING = 0;
	public static final int ACTIVE = 1;
	public static final int FAILED = 2;
	public static final int SUSPENDED = 3;
	public static final int DELETED = 4;
	
	private long id;
	private String name;
	public ScheduledDonationStatus() {
		super();
	}
	private String description;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "ScheduledDonationStatus [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
	
	

}
