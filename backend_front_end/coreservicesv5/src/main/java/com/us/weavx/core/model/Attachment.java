package com.us.weavx.core.model;

public class Attachment {

	private byte[] data;
    private String name;
    private String contentType;
    
	public Attachment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Attachment(byte[] data, String name, String contentType) {
		super();
		this.data = data;
		this.name = name;
		this.contentType = contentType;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}    
}
