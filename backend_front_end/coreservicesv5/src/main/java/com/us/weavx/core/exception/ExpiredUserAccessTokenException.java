package com.us.weavx.core.exception;

import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserWIthEmail;

public class ExpiredUserAccessTokenException extends Exception {

	private CustomerUserWIthEmail tokenOwner;
	
	public ExpiredUserAccessTokenException() {
		// TODO Auto-generated constructor stub
	}

	public ExpiredUserAccessTokenException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ExpiredUserAccessTokenException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ExpiredUserAccessTokenException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ExpiredUserAccessTokenException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public CustomerUserWIthEmail getTokenOwner() {
		return tokenOwner;
	}

	public void setTokenOwner(CustomerUserWIthEmail tokenOwner) {
		this.tokenOwner = tokenOwner;
	}
	
	

}
