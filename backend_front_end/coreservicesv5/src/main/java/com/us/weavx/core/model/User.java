package com.us.weavx.core.model;

import java.sql.Timestamp;

public class User implements Cloneable {
	private long id;
	private String email;
	private Timestamp createdAt;
	public User(long id, String email, Timestamp createdAt) {
		super();
		this.id = id;
		this.email = email;
		this.createdAt = createdAt;
	}
	public User(String email) {
		super();
		this.id = -1;
		this.email = email;
	}
	public User() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", createdAt=" + createdAt + "]";
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	
}
