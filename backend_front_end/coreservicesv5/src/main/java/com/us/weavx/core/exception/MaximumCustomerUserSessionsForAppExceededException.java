package com.us.weavx.core.exception;

import java.util.List;

import com.us.weavx.core.model.CustomerUserWIthEmail;
import com.us.weavx.core.model.UserAccessToken;

public class MaximumCustomerUserSessionsForAppExceededException extends Exception {
	
	private List<UserAccessToken> currentSessions;
	private CustomerUserWIthEmail tokenOwner;

	public MaximumCustomerUserSessionsForAppExceededException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MaximumCustomerUserSessionsForAppExceededException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public MaximumCustomerUserSessionsForAppExceededException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public MaximumCustomerUserSessionsForAppExceededException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public MaximumCustomerUserSessionsForAppExceededException(String arg0, List<UserAccessToken> list) {
		super(arg0);
		this.currentSessions = list;
		// TODO Auto-generated constructor stub
	}

	public MaximumCustomerUserSessionsForAppExceededException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public List<UserAccessToken> getCurrentSessions() {
		return currentSessions;
	}

	public void setCurrentSessions(List<UserAccessToken> currentSessions) {
		this.currentSessions = currentSessions;
	}

	public CustomerUserWIthEmail getTokenOwner() {
		return tokenOwner;
	}

	public void setTokenOwner(CustomerUserWIthEmail tokenOwner) {
		this.tokenOwner = tokenOwner;
	}
	
	
	
	

}
