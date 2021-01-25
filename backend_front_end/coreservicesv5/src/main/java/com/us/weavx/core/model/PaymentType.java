package com.us.weavx.core.model;

public class PaymentType {
	
	public static final int CREDIT_CARD = 1;
	public static final int PAYPAL = 2;
	public static final int FREE = 3;
	
	private int id;
	private String name;
	private String urlLogo;
	private boolean enabled;
	public PaymentType(int id, String name, String urlLogo, boolean enabled) {
		super();
		this.id = id;
		this.name = name;
		this.urlLogo = urlLogo;
		this.enabled = enabled;
	}
	public PaymentType(String name, String urlLogo) {
		super();
		this.name = name;
		this.urlLogo = urlLogo;
	}
	public PaymentType(String name, String urlLogo, boolean enabled) {
		super();
		this.name = name;
		this.urlLogo = urlLogo;
		this.enabled = enabled;
	}
	public PaymentType() {
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
	public String getUrlLogo() {
		return urlLogo;
	}
	public void setUrlLogo(String urlLogo) {
		this.urlLogo = urlLogo;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		return "PaymentType [id=" + id + ", name=" + name + ", urlLogo=" + urlLogo + ", enabled=" + enabled + "]";
	}
	
	

}
