package com.us.weavx.core.model;

public class FundDescription {
	
	private int fundId;
	private int langId;
	private String fundLabel;
	private String fundDescription;
	public FundDescription(int fundId, int langId, String fundLabel, String fundDescription) {
		super();
		this.fundId = fundId;
		this.langId = langId;
		this.fundLabel = fundLabel;
		this.fundDescription = fundDescription;
	}
	public FundDescription(int langId, String fundLabel, String fundDescription) {
		super();
		this.langId = langId;
		this.fundLabel = fundLabel;
		this.fundDescription = fundDescription;
		this.fundId = -1;
	}
	public FundDescription() {
		super();
	}
	public int getFundId() {
		return fundId;
	}
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public String getFundLabel() {
		return fundLabel;
	}
	public void setFundLabel(String fundLabel) {
		this.fundLabel = fundLabel;
	}
	public String getFundDescription() {
		return fundDescription;
	}
	public void setFundDescription(String fundDescription) {
		this.fundDescription = fundDescription;
	}
	@Override
	public String toString() {
		return "FundDescription [fundId=" + fundId + ", langId=" + langId + ", fundLabel=" + fundLabel
				+ ", fundDescription=" + fundDescription + "]";
	}
	
	
	

}
