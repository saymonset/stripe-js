package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AdminRole;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.AdminTxServices;

@Component
public class AddAdminUsersMethodImpl implements ServiceMethod {

	@Autowired
	private AdminTxServices adminTxServices;
	@Autowired
	private RegisterUserMethodImpl registerUserMethod;
	
	public Response executeMethod(Request request) {
		try {
			List<HashMap<String, Object>> badUsers = new ArrayList<>();
			List<HashMap<String, Object>> goodUsers = new ArrayList<>();
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			ArrayList<HashMap<String, Object>> adminUsers = (ArrayList<HashMap<String, Object>>) params.get("adminUsers");
			String ipAddress = (String) params.get("ipAddress");
			String userAgent = (String) params.get("userAgent");
			List<AdminRole> adminRoles = adminTxServices.listAllAdminRoles();
			Map<String, AdminRole> adminRolesMap = new HashMap<>(); 
			adminRoles.forEach(t -> {
				adminRolesMap.put(t.getName(), t);
			});
			adminUsers.forEach(t -> {
				try {
					String name = (String) t.get("name");
					String lastname = (String) t.get("lastname");
					String email = (String) t.get("email");
					String password = (String) t.get("password");
					ArrayList<String> roles = (ArrayList<String>) t.get("adminRoles");
					ArrayList<AdminRole> verifiedRoles = new ArrayList<>();
					roles.forEach(r -> {
						if (adminRolesMap.get(r) != null) {
							verifiedRoles.add(adminRolesMap.get(r));
						}
					});
					//Se procede a agregar el usuario
					Request registerUserRequest = new Request();
					registerUserRequest.setAccessToken(aInfo.getAccessToken().getToken());
					registerUserRequest.setMethodName("registerNewUser");
					HashMap<String, Object> newUserParams = new HashMap<String, Object>();
					HashMap<String, Object> user = new HashMap<>();
					String newPassword = (password==null)?RandomStringUtils.random(8,true,true):password;
					user.put("email", email);
					user.put("firstName", name);
					user.put("lastName", lastname);
					user.put("password", newPassword);
					newUserParams.put("user", user);
					newUserParams.put("ipAddress", ipAddress);
					newUserParams.put("userAgent", userAgent);
					newUserParams.put("accessInfo", aInfo);
					registerUserRequest.setParameters(newUserParams);
					Response newUserResp = registerUserMethod.executeMethod(registerUserRequest);
					boolean addRoles = true;
					long customerUserId = -1;
					switch (newUserResp.getReturnCode()) {
					case ServiceReturnMessages.USER_ALREADY_EXISTS_CODE:
						//Se obtiene el customerUserId 
						customerUserId = (Long) newUserResp.getResult().get("customerUserId");
						break;
					case ServiceReturnMessages.SUCCESS_CODE:
						//se procede a agregar los roles al usuario
						UserAccessToken uAT = (UserAccessToken) newUserResp.getResult().get("userAccessToken");
						customerUserId = uAT.getCustomerUserId();
						break;
					default:
						addRoles = false;
						t.put("error", newUserResp.getReturnMessage());
						badUsers.add(t);					
					}
					if (addRoles) {
						adminTxServices.associateAdminRolesToCustomerUser(verifiedRoles, customerUserId);
						goodUsers.add(t);
					}
				} catch (Exception e) {
					t.put("error", e.getMessage());
					badUsers.add(t);
				}
			});
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("goodUsers", goodUsers);
			result.put("badUsers", badUsers);
			res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" e: "+e.getMessage());
			return res;
		}
	}

	
	

}
