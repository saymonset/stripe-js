package com.us.weavx.core.model;

public class CustomerUserWIthEmail {
	
	private CustomerUser custUser;
	private String email;
	
	

	public CustomerUserWIthEmail(CustomerUser custUser, String email) {
		super();
		this.custUser = custUser;
		this.email = email;
	}



	public CustomerUserWIthEmail() {
		super();
	}



	public CustomerUser getCustUser() {
		return custUser;
	}



	public void setCustUser(CustomerUser custUser) {
		this.custUser = custUser;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	@Override
	public String toString() {
		return "CustomerUserWIthEmail [custUser=" + custUser + ", email=" + email + "]";
	}
	
	

}
