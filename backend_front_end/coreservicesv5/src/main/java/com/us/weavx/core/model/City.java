package com.us.weavx.core.model;

public class City {
	
	private int id;
	private String name;
	private int stateId;
	
	public static final int UNDEFINED_CITY = 0;

	public City() {
		// TODO Auto-generated constructor stub
	}

	public City(int id, String name, int stateId) {
		super();
		this.id = id;
		this.name = name;
		this.stateId = stateId;
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

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", stateId=" + stateId + "]";
	}
	
	

}
