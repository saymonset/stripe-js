package com.us.weavx.core.model;

public class AuthenticatedUserInfo {
	
	private UserAccessToken userAccessToken;
	private CustomerUserWIthEmail customerUser;

	public AuthenticatedUserInfo() {
		
	}

	public AuthenticatedUserInfo(UserAccessToken userAccessToken, CustomerUserWIthEmail customerUser) {
		super();
		this.userAccessToken = userAccessToken;
		this.customerUser = customerUser;
	}

	public UserAccessToken getUserAccessToken() {
		return userAccessToken;
	}

	public void setUserAccessToken(UserAccessToken userAccessToken) {
		this.userAccessToken = userAccessToken;
	}

	public CustomerUserWIthEmail getCustomerUser() {
		return customerUser;
	}

	public void setCustomerUser(CustomerUserWIthEmail customerUser) {
		this.customerUser = customerUser;
	}

	@Override
	public String toString() {
		return "AuthenticatedUserInfo [userAccessToken=" + userAccessToken + ", customerUser=" + customerUser + "]";
	}
	
	

}
