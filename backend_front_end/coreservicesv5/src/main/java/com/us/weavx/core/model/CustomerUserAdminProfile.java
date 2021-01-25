package com.us.weavx.core.model;

import java.util.Collection;

public class CustomerUserAdminProfile {

	private CustomerUser customerUser;
	private Collection<AdminRoleFunctions> adminRoleFunctions;
	public CustomerUser getCustomerUser() {
		return customerUser;
	}
	public void setCustomerUser(CustomerUser customerUser) {
		this.customerUser = customerUser;
	}
	public Collection<AdminRoleFunctions> getAdminRoleFunctions() {
		return adminRoleFunctions;
	}
	public void setAdminRoleFunctions(Collection<AdminRoleFunctions> adminRoleFunctions) {
		this.adminRoleFunctions = adminRoleFunctions;
	}
	@Override
	public String toString() {
		return "CustomerUserAdminProfile [customerUser=" + customerUser + ", adminRoleFunctions=" + adminRoleFunctions
				+ "]";
	}
	
	

}
