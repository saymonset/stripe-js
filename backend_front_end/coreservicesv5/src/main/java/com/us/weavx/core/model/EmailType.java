package com.us.weavx.core.model;

public class EmailType {
	
	public static final int HEALTH_CHECK = 1;
	public static final int PURCHASE_EMAIL = 2;
	public static final int AUTHENTICATION_EMAIL = 3;
	public static final int INCOMPLETE_PURCHASE_EMAIL = 4;
	public static final int CAMPAING_EMAIL = 5;
	public static final int ALERT_EMAIL = 6;
	public static final int NOTIFICATION_EMAIL = 7;
	public static final int SCHEDULED_PURCHASE_EMAIL = 8;

	private int id;
	private String name;
	public EmailType() {
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
		return "EmailType [id=" + id + ", name=" + name + "]";
	}
	
	
	
}
