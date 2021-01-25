package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.exception.UnknownCustomerUserException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class UpdateCustomerUserPasswordMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			String ipAddress = (String) params.get("ipAddress");
			if (ipAddress == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.IP_ADDRESS_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.IP_ADDRESS_REQUIRED);
				return res;
			}
			String userAgent = (String) params.get("userAgent");
			if (userAgent == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.USER_AGENT_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.USER_AGENT_REQUIRED);
				return res;				
			}
			String userPassword = (String) params.get("password");
			String userAccessToken = (String) params.get("userAccessToken");
			if (userAccessToken == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.USER_ACCESS_TOKEN_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.USER_ACCESS_TOKEN_REQUIRED);
				return res;								
			}
			UserAccessToken uAT = userServices.validateUserAccessToken(userAccessToken);
			CustomerUser customerUser = userServices.findCustomerUserById(uAT.getCustomerUserId());
			customerUser.setPassword(org.apache.commons.codec.digest.DigestUtils.sha256Hex(userPassword));
			AuthenticatedUserInfo uInfo = userServices.updateCustomerUserPassword(customerUser, uAT.getApplicationId(), ipAddress, userAgent);
			
			
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			if (uInfo.getUserAccessToken() != null) {
				result.put("userAccessToken", uInfo.getUserAccessToken());
			}
			res.setResult(result);
			return res;
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		} catch (MaximumCustomerUserSessionsForAppExceededException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED_CODE);
			res.setReturnMessage(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED);
			return res;
		} catch (UnknownCustomerUserException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER_CODE);
			res.setReturnMessage(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER);
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
