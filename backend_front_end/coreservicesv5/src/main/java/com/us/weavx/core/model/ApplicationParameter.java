package com.us.weavx.core.model;

public class ApplicationParameter {

	private long id;
	private long applicationId;
	private String name;
	private String value;
	private String description;
	
	
	
	public ApplicationParameter(long id, long applicationId, String name, String value, String description) {
		super();
		this.id = id;
		this.applicationId = applicationId;
		this.name = name;
		this.value = value;
		this.description = description;
	}

	public ApplicationParameter() {
		super();
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ApplicationParameter [id=" + id + ", applicationId=" + applicationId + ", name=" + name + ", value="
				+ value + ", description=" + description + "]";
	}
	
	

}
