package com.us.weavx.core.model;

public class EventAsset {

	private long id;
	private int assetId;
	private long customerId;
	private long applicationid;
	private int langId;
	private String assetValue;
	private String assetParams;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getAssetId() {
		return assetId;
	}
	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getApplicationid() {
		return applicationid;
	}
	public void setApplicationid(long applicationid) {
		this.applicationid = applicationid;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public String getAssetValue() {
		return assetValue;
	}
	public void setAssetValue(String assetValue) {
		this.assetValue = assetValue;
	}
	public String getAssetParams() {
		return assetParams;
	}
	public void setAssetParams(String assetParams) {
		this.assetParams = assetParams;
	}
	@Override
	public String toString() {
		return "EventAsset [id=" + id + ", assetId=" + assetId + ", customerId=" + customerId + ", applicationid="
				+ applicationid + ", langId=" + langId + ", assetValue=" + assetValue + ", assetParams=" + assetParams
				+ "]";
	}
	
	
	
	
	

}
