package com.us.weavx.core.model;

import java.sql.Timestamp;

public class Developer {
	
	private long id;
	private String name;
	private String email;
	private String password;
	private Timestamp createdAt;
	public Developer(long id, String name, String email, String password, Timestamp createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
	}
	public Developer(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}
	public Developer() {
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "Developer [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", createdAt="
				+ createdAt + "]";
	}
	
	

}
