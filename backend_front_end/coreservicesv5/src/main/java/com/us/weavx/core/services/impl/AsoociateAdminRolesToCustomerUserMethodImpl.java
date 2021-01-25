package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.model.AdminRole;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.AdminTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class AsoociateAdminRolesToCustomerUserMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;
	@Autowired
	private AdminTxServices adminServices;

	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			//Se inicializa el container de spring
			String userAccessToken = (String) params.get("userAccessToken");
			ArrayList<Number> paramAdminRoleList = (ArrayList<Number>) params.get("adminRoles");
			//Se valida el userAccessToken
			UserAccessToken uAT = userServices.validateUserAccessToken(userAccessToken);
			List<AdminRole> adminRoleList = new ArrayList<>();
			paramAdminRoleList.forEach(t -> {
				AdminRole tmp = new AdminRole();
				tmp.setId(t.intValue());
				adminRoleList.add(tmp);
			});
			adminServices.associateAdminRolesToCustomerUser(adminRoleList, uAT.getCustomerUserId());
			Response res = new Response();
			HashMap<String, Object> result = new HashMap<>();
			res.setResult(result);
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			return res;
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+": "+e.getLocalizedMessage());
			return res;
		} catch (InvalidUserAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN_CODE);
			res.setReturnMessage(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN);
			return res;
		} catch (ExpiredUserAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.EXPIRED_USER_ACCESS_TOKEN_CODE);
			res.setReturnMessage(ServiceReturnMessages.EXPIRED_USER_ACCESS_TOKEN);
			return res;
		} 
	}

	
	

}
