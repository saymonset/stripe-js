package com.us.weavx.core.model;

import java.util.Collection;

public class AdminRoleFunctions {

	private AdminRole adminRole;
	private Collection<AdminFunction> adminFunctions;
	public AdminRole getAdminRole() {
		return adminRole;
	}
	public void setAdminRole(AdminRole adminRole) {
		this.adminRole = adminRole;
	}
	public Collection<AdminFunction> getAdminFunctions() {
		return adminFunctions;
	}
	public void setAdminFunctions(Collection<AdminFunction> adminFunctions) {
		this.adminFunctions = adminFunctions;
	}
	@Override
	public String toString() {
		return "AdminRoleFunctions [adminRole=" + adminRole + ", adminFunctions=" + adminFunctions + "]";
	}
	
	

}
