package com.us.weavx.core.model;

public class Property {
	
	private int id;
	private String name;
	public Property(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Property() {
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
	@Override
	public String toString() {
		return "Property [id=" + id + ", name=" + name + "]";
	}
	
	

}
