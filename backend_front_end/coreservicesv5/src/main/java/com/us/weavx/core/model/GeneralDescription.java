package com.us.weavx.core.model;

public class GeneralDescription {

	private long customerId;
	private int langId;
	private String generalLabel;
	private String generalDescription;
	private int tagId;
	private String tagDescription;
	
	public GeneralDescription(long customerId, int langId, String generalLabel, String generalDescription, int tagId, String tagDescription) {
		super();
		this.customerId = customerId;
		this.langId = langId;
		this.generalLabel = generalLabel;
		this.generalDescription = generalDescription;
		this.tagId = tagId;
		this.tagDescription = tagDescription;
	}
	
	public GeneralDescription(int langId, String generalLabel, String generalDescription, int tagId, String tagDescription) {
		super();
		this.langId = langId;
		this.generalLabel = generalLabel;
		this.generalDescription = generalDescription;
		this.tagId = tagId;
		this.tagDescription = tagDescription;
	}

	public GeneralDescription() {
		super();
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getGeneralLabel() {
		return generalLabel;
	}

	public void setGeneralLabel(String generalLabel) {
		this.generalLabel = generalLabel;
	}

	public String getGeneralDescription() {
		return generalDescription;
	}

	public void setGeneralDescription(String generalDescription) {
		this.generalDescription = generalDescription;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getTagDescription() {
		return tagDescription;
	}

	public void setTagDescription(String tagDescription) {
		this.tagDescription = tagDescription;
	}

	@Override
	public String toString() {
		return "GeneralDescription [customerId=" + customerId + ", langId=" + langId + ", generalLabel=" + generalLabel
				+ ", generalDescription=" + generalDescription + ", tagId=" + tagId + ", tagDescription="
				+ tagDescription + "]";
	}

	
}
