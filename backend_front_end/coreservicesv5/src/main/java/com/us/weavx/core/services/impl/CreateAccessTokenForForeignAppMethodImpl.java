package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.exception.UserAccessTokenGenerationException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class CreateAccessTokenForForeignAppMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userTxServices;

	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			String foreignAppUserAccessToken = (String) params.get("foreignUserAccessToken");
			UserAccessToken newToken = userTxServices.createAccessTokenForForeignApp(foreignAppUserAccessToken,  aInfo.getApplicationId(), aInfo.getCustomerId());
			Response res = new Response();
			HashMap<String, Object> result = new HashMap<>();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			result.put("newUserAccessToken", newToken.getToken());
			res.setResult(result);
			return res; 
		} catch (RuntimeException | UserAccessTokenGenerationException e) {
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
			return res;
		} catch (MaximumCustomerUserSessionsForAppExceededException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED_CODE);
			res.setReturnMessage(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED);
			HashMap<String, Object> result = new HashMap<>();
			result.put("userSessions", e.getCurrentSessions());
			return res;
		}
	}

	
	

}
