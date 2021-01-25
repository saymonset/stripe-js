package com.us.weavx.core.model;

public class ScheduledDonationFrequency {
	
	private int id;
	private String name;
	private String description;
	private String frequency_handler_class;
	public ScheduledDonationFrequency() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String getFrequency_handler_class() {
		return frequency_handler_class;
	}
	public void setFrequency_handler_class(String frequency_handler_class) {
		this.frequency_handler_class = frequency_handler_class;
	}
	@Override
	public String toString() {
		return "ScheduledDonationFrequency [id=" + id + ", name=" + name + ", description=" + description
				+ ", frequency_handler_class=" + frequency_handler_class + "]";
	}
	
	

}
