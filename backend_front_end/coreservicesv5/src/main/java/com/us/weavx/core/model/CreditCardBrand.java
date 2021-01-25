package com.us.weavx.core.model;

public class CreditCardBrand {
	
	public static final int DISCOVER = 3;
	public static final int VISA = 1;
	public static final int MASTER = 2;
	
	private int id;
	private String name;
	private String urlLogo;
	public CreditCardBrand(int id, String name, String urlLogo) {
		super();
		this.id = id;
		this.name = name;
		this.urlLogo = urlLogo;
	}
	public CreditCardBrand() {
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
	@Override
	public String toString() {
		return "CreditCardBrand [id=" + id + ", name=" + name + ", urlLogo=" + urlLogo + "]";
	}
	
	

}
