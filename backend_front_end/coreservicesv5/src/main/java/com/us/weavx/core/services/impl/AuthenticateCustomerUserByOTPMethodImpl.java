package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredOTPException;
import com.us.weavx.core.exception.InvalidOTPCodeException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.exception.MaximumOTPValidationTriesExceededException;
import com.us.weavx.core.exception.OTPIllegalStatusException;
import com.us.weavx.core.exception.RequiredApplicationParameterNotFoundException;
import com.us.weavx.core.exception.UnknownApplicationException;
import com.us.weavx.core.exception.UnknownCustomerUserException;
import com.us.weavx.core.exception.UnknownOTPException;
import com.us.weavx.core.exception.UserAccessTokenGenerationException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;

@Component
public class AuthenticateCustomerUserByOTPMethodImpl implements ServiceMethod {

	@Autowired
	private UserTxServices userServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			String otpCode = (String) params.get("otpCode");
			long otpId = ((Number) params.get("otpId")).longValue();
			String ipAddress = (String) params.get("ipAddress");
			String userAgent = (String) params.get("userAgent");
			AuthenticatedUserInfo userInfo = userServices.authenticateCustomerUserByOTP(otpId, otpCode, ipAddress, userAgent);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customer_user", userInfo.getCustomerUser());
			result.put("user_access_token", userInfo.getUserAccessToken());
			res.setResult(result);
			return res;
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		} catch (InvalidOTPCodeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.INVALID_OTP_CODE);
			res.setReturnMessage(ServiceReturnMessages.INVALID_OTP);
			return res;			
		} catch (UnknownApplicationException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.UNKNOWN_APPLICATION_CODE);
			res.setReturnMessage(ServiceReturnMessages.UNKNOWN_APPLICATION);
			return res;			
		} catch (UnknownCustomerUserException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER_CODE);
			res.setReturnMessage(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER);
			return res;			
		} catch (UserAccessTokenGenerationException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.ACCESS_TOKEN_GENERATION_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.ACCESS_TOKEN_GENERATION_ERROR+":"+e);
			return res;			
		} catch (MaximumCustomerUserSessionsForAppExceededException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED_CODE);
			res.setReturnMessage(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED);
			HashMap<String, Object> result = new HashMap<>();
			result.put("userSessions", e.getCurrentSessions());
			res.setResult(result);
			return res;		
		} catch (MaximumOTPValidationTriesExceededException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.MAXIMUM_OTP_VALIDATION_TRIES_EXCEEDED_CODE);
			res.setReturnMessage(ServiceReturnMessages.MAXIMUM_OTP_VALIDATION_TRIES_EXCEEDED+":"+e);
			return res;						
		} catch (UnknownOTPException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.UNKNOWN_OTP_CODE);
			res.setReturnMessage(ServiceReturnMessages.UNKNOWN_OTP+":"+e);
			return res;			
		} catch (ExpiredOTPException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.EXPIRED_OTP_CODE);
			res.setReturnMessage(ServiceReturnMessages.EXPIRED_OTP+":"+e);
			return res;			
		} catch (OTPIllegalStatusException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.OTP_ILLEGALSTATUS_CODE);
			res.setReturnMessage(ServiceReturnMessages.OTP_ILLEGAL_STATUS+":"+e);
			return res;			
		} catch (RequiredApplicationParameterNotFoundException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;			
		}
	}

	
	

}
