package com.us.weavx.core.model;

public class Continent {
	
	private int id;
	private String name;
	
	public static final int UNDEFINED_CONTINENT = 0;

	public Continent() {
		// TODO Auto-generated constructor stub
	}

	public Continent(int id, String name) {
		super();
		this.id = id;
		this.name = name;
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
		return "City [id=" + id + ", name=" + name + "]";
	}
	
	

}
