package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.InvalidUserCredentialsException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.exception.UnknownApplicationException;
import com.us.weavx.core.exception.UserAccessTokenGenerationException;
import com.us.weavx.core.model.AuthenticatedUserAdmin;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;

@Component
public class AuthenticateUserAdminMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
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
			HashMap<String, Object> userCredentials = (HashMap<String, Object>) params.get("user_credentials");
			String email = ((String) userCredentials.get("email")).toLowerCase();
			String password = (String) userCredentials.get("password");
			List<AuthenticatedUserAdmin> lstCustomerApplication;
			try {
				lstCustomerApplication = userServices.authenticateCustomerUserAdmin(email, password, ipAddress, userAgent);

				//userInfo = userServices.authenticateCustomerUser(email, password,ipAddress, userAgent);
			} catch (InvalidUserCredentialsException e1) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.INVALID_USER_CREDENTIALS_CODE);
				res.setReturnMessage(ServiceReturnMessages.INVALID_USER_CREDENTIALS);
				return res;
			} catch (UnknownApplicationException e1) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.UNKNOWN_APPLICATION_CODE);
				res.setReturnMessage(ServiceReturnMessages.UNKNOWN_APPLICATION);
				return res;
			} catch (UserAccessTokenGenerationException e1) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.ERROR_GENERATING_USER_TOKEN_CODE);
				res.setReturnMessage(ServiceReturnMessages.ERROR_GENERATING_USER_TOKEN);
				return res;
			} catch (MaximumCustomerUserSessionsForAppExceededException e) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED_CODE);
				res.setReturnMessage(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED);
				HashMap<String, Object> result = new HashMap<>();
				result.put("userSessions", e.getCurrentSessions());
				res.setResult(result);
				return res;				
			}
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			res.setReturnDate(LocalDateTime.now().toString());
			
			 HashMap<String, Object> result = new HashMap<>(); 
			 result.put("customerApplication",lstCustomerApplication); 
			 res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		}
	}

	
	

}
