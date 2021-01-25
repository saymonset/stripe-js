package com.us.weavx.core.model;

public class SystemMessage {

	private long id;
	private String name;
	
	
	
	public SystemMessage(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}



	public SystemMessage() {
		
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
		return "SystemMessage [id=" + id + ", name=" + name + "]";
	}
	
	

}
