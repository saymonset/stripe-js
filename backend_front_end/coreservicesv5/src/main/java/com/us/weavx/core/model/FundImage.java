package com.us.weavx.core.model;

public class FundImage {
	
	private int imageId;
	private int fundId;
	private String imageUrl;
	private int langId;
	public FundImage() {
		super();
	}
	public FundImage(int imageId, int fundId, String imageUrl, int langId) {
		super();
		this.imageId = imageId;
		this.fundId = fundId;
		this.imageUrl = imageUrl;
		this.langId = langId;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public int getFundId() {
		return fundId;
	}
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	@Override
	public String toString() {
		return "FundImage [imageId=" + imageId + ", fundId=" + fundId + ", imageUrl=" + imageUrl + ", langId=" + langId
				+ "]";
	}
	
	
	
	

}
