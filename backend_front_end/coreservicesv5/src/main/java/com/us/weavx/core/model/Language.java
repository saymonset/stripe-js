package com.us.weavx.core.model;

public class Language {
	
	private int id;
	private String locale;
	private String description;
	private String isDefault;
	public Language(int id, String locale, String description, String isDefault) {
		super();
		this.id = id;
		this.locale = locale;
		this.description = description;
		this.isDefault = isDefault;
	}
	public Language() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	@Override
	public String toString() {
		return "Language [id=" + id + ", locale=" + locale + ", description=" + description + ", isDefault=" + isDefault
				+ "]";
	}
	
	

}
