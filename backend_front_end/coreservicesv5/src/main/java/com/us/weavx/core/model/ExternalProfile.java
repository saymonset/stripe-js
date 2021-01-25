package com.us.weavx.core.model;

public class ExternalProfile {
	
	private long id;
	private int identityProviderId;
	private long userId;
	private String accesstoken;
	public ExternalProfile(long id, int identityProviderId, long userId, String accesstoken) {
		super();
		this.id = id;
		this.identityProviderId = identityProviderId;
		this.userId = userId;
		this.accesstoken = accesstoken;
	}
	public ExternalProfile(int identityProviderId, long userId, String accesstoken) {
		super();
		this.identityProviderId = identityProviderId;
		this.userId = userId;
		this.accesstoken = accesstoken;
	}
	public ExternalProfile() {
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getIdentityProviderId() {
		return identityProviderId;
	}
	public void setIdentityProviderId(int identityProviderId) {
		this.identityProviderId = identityProviderId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	@Override
	public String toString() {
		return "ExternalProfile [id=" + id + ", identityProviderId=" + identityProviderId + ", userId=" + userId
				+ ", accesstoken=" + accesstoken + "]";
	}
	
	

}
