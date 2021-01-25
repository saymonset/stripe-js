package com.us.weavx.core.model;

public class State {
	
	private int id;
	private String name;
	private int countryId;
	
	public static final int UNDEFINED_STATE = 0;

	public State() {
		// TODO Auto-generated constructor stub
	}

	public State(int id, String name, int countryId) {
		super();
		this.id = id;
		this.name = name;
		this.countryId = countryId;
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

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + ", countryId=" + countryId + "]";
	}
	
	

}
