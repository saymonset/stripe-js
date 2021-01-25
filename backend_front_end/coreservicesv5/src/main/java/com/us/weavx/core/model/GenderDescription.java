package com.us.weavx.core.model;

public class GenderDescription {
	
	private long customerId;
	private int genderId;
	private int langId;
	private String label;
	private String description;
	public GenderDescription(long customerId, int genderId, int langId, String label, String description) {
		super();
		this.customerId = customerId;
		this.genderId = genderId;
		this.langId = langId;
		this.label = label;
		this.description = description;
	}
	public GenderDescription() {
		super();
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "GenderDescription [customerId=" + customerId + ", genderId=" + genderId + ", langId=" + langId
				+ ", label=" + label + ", description=" + description + "]";
	}
	
	

}
