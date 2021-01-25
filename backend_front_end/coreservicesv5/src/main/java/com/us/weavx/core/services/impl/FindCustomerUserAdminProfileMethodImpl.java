package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.CustomerUserAdminProfile;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.AdminTxServices;
import com.us.weavx.core.services.tx.UserTxServices;

@Component
public class FindCustomerUserAdminProfileMethodImpl implements ServiceMethod {

	@Autowired
	private UserTxServices userServices;
	@Autowired
	private AdminTxServices adminTxServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			//Se inicializa el container de spring
			String userAccessToken = (String) params.get("userAccessToken");
			UserAccessToken uAT = userServices.validateUserAccessToken(userAccessToken);
			CustomerUserAdminProfile profile = adminTxServices.findCustomerUserAdminProfile(uAT.getCustomerUserId());
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerUserAdminProfile", profile);
			res.setResult(result);
			return res;
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
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
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerUserData", e.getTokenOwner().getCustUser());
			result.put("email", e.getTokenOwner().getEmail());
			res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());
			return res;			
		} 
	}

	
	

}
