package com.us.weavx.core.services.exception;

import com.us.weavx.core.model.CustomerUserWIthEmail;

public class ClosedByUserAccessTokenException extends Exception {
	
	private CustomerUserWIthEmail tokenOwner;

	public ClosedByUserAccessTokenException() {
		// TODO Auto-generated constructor stub
	}

	public ClosedByUserAccessTokenException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ClosedByUserAccessTokenException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ClosedByUserAccessTokenException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ClosedByUserAccessTokenException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
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
