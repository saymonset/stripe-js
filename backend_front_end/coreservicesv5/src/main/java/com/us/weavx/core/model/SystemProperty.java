package com.us.weavx.core.model;

public class SystemProperty {

	private long id;
	private String name;
	private String value;
	private String description;
	
	public SystemProperty() {
		
	}

	public SystemProperty(long id, String name, String value, String description) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.description = description;
	}

	public SystemProperty(String name, String value, String description) {
		super();
		this.name = name;
		this.value = value;
		this.description = description;
	}

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
		return "SystemProperty [id=" + id + ", name=" + name + ", value=" + value + ", description=" + description
				+ "]";
	}
	
	

}
