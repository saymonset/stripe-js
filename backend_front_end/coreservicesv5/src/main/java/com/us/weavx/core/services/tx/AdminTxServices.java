package com.us.weavx.core.services.tx;

import com.us.weavx.core.data.CoreServicesAdminTxDAO;
import com.us.weavx.core.model.AdminFunction;
import com.us.weavx.core.model.AdminRole;
import com.us.weavx.core.model.AdminRoleFunctions;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserAdminProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("adminTxServices")
public class AdminTxServices {

	@Autowired
	private CoreServicesAdminTxDAO dao;

	@Autowired
	private UserTxServices userServices;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AdminRole addAdminRole(AdminRole adminRole) {
		return dao.addAdminRole(adminRole);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public AdminRole findAdminRoleById(int id) {
		return dao.findAdminRoleById(id);				
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<AdminFunction> findRoleAdminFunctions(int adminRoleId) {
		return dao.findRoleAdminFunctions(adminRoleId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<AdminRole> findCustomerUserAdminRoles(long customerUserId) {
		return dao.findCustomerUserAdminRoles(customerUserId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AdminFunction addAdminFunction(final AdminFunction item) {
		return dao.addAdminFunction(item);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public AdminFunction findAdminFunctionById(int id) {
		return dao.findAdminFunctionById(id);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void associateAdminFunctionsToRole(final List<AdminFunction> adminFunctions, final int adminRoleId) {
		dao.associateAdminFunctionsToRole(adminFunctions, adminRoleId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void associateAdminRolesToCustomerUser(final List<AdminRole> adminRoles, final long customerUserId) {
		List<AdminRole> roles = dao.findCustomerUserAdminRoles(customerUserId);
		List<AdminRole> filteredRoles = new ArrayList<>();
		adminRoles.forEach(t -> {
			if (!roles.contains(t)) {
				filteredRoles.add(t);
			}
		});
		if (filteredRoles.size() > 0) {
			dao.associateAdminRolesToCustomerUser(filteredRoles, customerUserId);
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerUserAdminProfile findCustomerUserAdminProfile(long customerUserId) throws Exception {
		CustomerUser cu = userServices.findCustomerUserById(customerUserId);	
		if (cu == null) {
			throw new Exception("Unknown customer user.");
		} 
		CustomerUserAdminProfile profile = new CustomerUserAdminProfile();
		profile.setCustomerUser(cu);
		List<AdminRole> adminRoles = dao.findCustomerUserAdminRoles(customerUserId);
		List<AdminRoleFunctions> adminRolesFunctions = new ArrayList<>();
		adminRoles.forEach(t -> {
			AdminRoleFunctions tmp = new AdminRoleFunctions();
			tmp.setAdminRole(t);
			List<AdminFunction> roleFunctionsTmp = dao.findRoleAdminFunctions(t.getId());
			tmp.setAdminFunctions(roleFunctionsTmp);
			adminRolesFunctions.add(tmp);
		});
		profile.setAdminRoleFunctions(adminRolesFunctions);
		return profile;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<AdminRole> listAllAdminRoles() {
		return dao.listAllAdminRoles();
	}

}
