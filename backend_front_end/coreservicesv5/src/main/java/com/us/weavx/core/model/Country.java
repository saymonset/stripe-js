package com.us.weavx.core.model;

public class Country {

	private int id;
	private String shortName;
	private String name;
	private String phoneCode;
	private int continentId;
	
	public static final int UNDEFINED_COUNTRY = 0;
	
	public Country() {
		// TODO Auto-generated constructor stub
	}

	public Country(int id, String shortName, String name) {
		super();
		this.id = id;
		this.shortName = shortName;
		this.name = name;
	}
	
	

	public Country(int id, String shortName, String name, String phoneCode) {
		super();
		this.id = id;
		this.shortName = shortName;
		this.name = name;
		this.phoneCode = phoneCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	

	public int getContinentId() {
		return continentId;
	}

	public void setContinentId(int continent_id) {
		this.continentId = continent_id;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", shortName=" + shortName + ", name=" + name + ", phoneCode=" + phoneCode + ", continentId=" + continentId + "]";
	}

	
	

}
