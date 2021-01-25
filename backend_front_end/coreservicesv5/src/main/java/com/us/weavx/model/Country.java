package com.us.weavx.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="countries")
public class Country {
	
	@Id
	private int id;
	private String shortname;
	private String name;
	private String phone_code;
	@ManyToOne
	@JoinColumn(name="continent_id")
	private Continent continent;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone_code() {
		return phone_code;
	}
	public void setPhone_code(String phone_code) {
		this.phone_code = phone_code;
	}	
	
	public Continent getContinent() {
		return continent;
	}
	public void setContinent(Continent continent) {
		this.continent = continent;
	}
	@Override
	public String toString() {
		return "Country [id=" + id + ", shortname=" + shortname + ", name=" + name + ", phone_code=" + phone_code + ", continent=" + continent + "]";
	}
	
	

}
