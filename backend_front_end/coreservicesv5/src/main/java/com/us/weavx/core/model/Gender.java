package com.us.weavx.core.model;

public class Gender {
	
	private long id;
	private String name;
	
	public static final long UNDEFINED_GENDER = 0;
	
	public Gender(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Gender(String name) {
		super();
		this.name = name;
	}
	public Gender() {
		super();
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
	@Override
	public String toString() {
		return "Gender [id=" + id + ", name=" + name + "]";
	}
	
	

}
