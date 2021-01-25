package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.AlreadyUsedAccessTokenException;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.exception.ClosedByUserAccessTokenException;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class InvalidateUserSessionsMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			String userAccessToken = (String) params.get("invalidUserAccessToken");
			if (userAccessToken == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.USER_ACCESS_TOKEN_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.USER_ACCESS_TOKEN_REQUIRED);
				return res;
			}
			Long customerUserId = null;
			try {
				AuthenticatedUserInfo cu = userServices.authenticateUserWithAccessToken(userAccessToken, aInfo.getCustomerId());
			} catch (InvalidUserAccessTokenException e) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN_CODE);
				res.setReturnMessage(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN);
				return res;
			} catch (ExpiredUserAccessTokenException e) {
				customerUserId = e.getTokenOwner().getCustUser().getId();
			} catch (AlreadyUsedAccessTokenException e) {
				customerUserId = e.getTokenOwner().getCustUser().getId();
			} catch (ClosedByUserAccessTokenException e) {
				customerUserId = e.getTokenOwner().getCustUser().getId();
			} catch (MaximumCustomerUserSessionsForAppExceededException e) {
				customerUserId = e.getCurrentSessions().get(0).getCustomerUserId();
			}
			userServices.closeAllCustomerUserSessions(customerUserId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			res.setResult(result);
			return res;				
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		}
	}

	
	

}
