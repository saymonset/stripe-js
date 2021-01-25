package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.AlreadyUsedValidationCodeException;
import com.us.weavx.core.exception.ExpiredValidationCodeException;
import com.us.weavx.core.exception.MaxValidationCodeTriesExceededException;
import com.us.weavx.core.exception.RequiredApplicationParameterNotFoundException;
import com.us.weavx.core.exception.UnsupportedDeviceException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UtilTxServices;
@Component
public class ValidateDeviceCodeMethodImpl implements ServiceMethod {
	@Autowired
	private UtilTxServices utilTxServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			long validationCodeId = ((Number) params.get("validationCodeId")).longValue();
			String deviceCode = (String) params.get("deviceCode");
			if (utilTxServices.validateDeviceCode(validationCodeId, deviceCode, customerId, applicationId)) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				res.setResult(result);
				return res;					
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.INVALID_OTP_CODE);
				res.setReturnMessage(ServiceReturnMessages.INVALID_OTP);
				HashMap<String, Object> result = new HashMap<>();
				res.setResult(result);
				return res;	
			}
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		} catch (UnsupportedDeviceException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.UNSUPPORTED_DEVICE_CODE);
			res.setReturnMessage(ServiceReturnMessages.UNSUPPORTED_DEVICE);
			return res;			
		} catch (ExpiredValidationCodeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.EXPIRED_OTP_CODE);
			res.setReturnMessage(ServiceReturnMessages.EXPIRED_OTP);
			return res;
		} catch (AlreadyUsedValidationCodeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.ALREADY_USED_VALIDATIONCODE_CODE);
			res.setReturnMessage(ServiceReturnMessages.ALREADY_USED_VALIDATIONCODE);
			return res;
		} catch (RequiredApplicationParameterNotFoundException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		} catch (MaxValidationCodeTriesExceededException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.MAXIMUM_OTP_VALIDATION_TRIES_EXCEEDED_CODE);
			res.setReturnMessage(ServiceReturnMessages.MAXIMUM_OTP_VALIDATION_TRIES_EXCEEDED);
			return res;
		}
	}

	
	

}
