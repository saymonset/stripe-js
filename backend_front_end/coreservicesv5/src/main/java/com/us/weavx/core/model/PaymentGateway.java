package com.us.weavx.core.model;

public class PaymentGateway {
	
	public static final int AUTHORIZE = 1;
	public static final int STRIPE = 2;
	
	private int id;
	private String name;
	private boolean enabled;
	private String implementorClass;
	public PaymentGateway(int id, String name, boolean enabled, String implementorClass) {
		super();
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.implementorClass = implementorClass;
	}
	public PaymentGateway(String name, boolean enabled, String implementorClass) {
		super();
		this.name = name;
		this.enabled = enabled;
		this.implementorClass = implementorClass;
	}
	public PaymentGateway() {
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
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImplementorClass() {
		return implementorClass;
	}
	public void setImplementorClass(String implementorClass) {
		this.implementorClass = implementorClass;
	}
	@Override
	public String toString() {
		return "PaymentGateway [id=" + id + ", name=" + name + ", enabled=" + enabled + ", implementorClass="
				+ implementorClass + "]";
	}
	
	

}
