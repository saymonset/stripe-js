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
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.User;
import com.us.weavx.core.model.ValidationCode;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.services.tx.UtilTxServices;
import com.us.weavx.core.util.SystemSettingsManager;
@Component
public class UpdateCustomerUserEmailMethodImpl implements ServiceMethod {
	@Autowired
	private UtilTxServices utilTxServices;
	@Autowired
	private UserTxServices userTxServices;
	@Autowired
	private SystemSettingsManager systemSettingsManager;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			long customerUserId = ((Number) params.get("customerUserId")).longValue();
			//Validar que el deviceCode es correcto
			boolean checkOtp = Boolean.parseBoolean(systemSettingsManager.getSystemProperty("EMAIL_UPDATE_OTP_ENABLED"));
			String newEmail = null;
			if (checkOtp) {
				long validationCodeId = ((Number) params.get("validationCodeId")).longValue();
				String deviceCode = (String) params.get("deviceCode");
				if (utilTxServices.validateDeviceCode(validationCodeId, deviceCode, customerId, applicationId)) {
					ValidationCode validationCode = userTxServices.findValidationCodeById(validationCodeId);
					newEmail = validationCode.getDevice();					
				} else {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.INVALID_OTP_CODE);
					res.setReturnMessage(ServiceReturnMessages.INVALID_OTP);
					HashMap<String, Object> result = new HashMap<>();
					res.setResult(result);
					return res;	
				}
			} else {
				newEmail = (String) params.get("newEmail");
				if (newEmail == null) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.INVALID_EMAIL_ADDRRESS_CODE);
					res.setReturnMessage(ServiceReturnMessages.INVALID_EMAIL_ADDRESS+": newEmail is required when EMAIL_UPDATE_OTP_ENABLED is false.");
					HashMap<String, Object> result = new HashMap<>();
					res.setResult(result);
					return res;						
				}
			}
			boolean existsNewUser = false;
			User u = null;
			try {
				u = userTxServices.findUserByEmail(newEmail);
				CustomerUser cu = userTxServices.findCustomerUserByUserId(u.getId(), customerId);
				if (cu != null) {
					existsNewUser = true;
				}
			} catch (Exception e) {
				//No existe el usuario
			}
			if (!existsNewUser) {
				if (u == null) {
 					//Se debe verificar si existe el USER de lo contrario se debe crear antes de poder asociarlo al customer user
					//No existe el usuario por lo cual hay que crearlo
					u = new User(newEmail);
					userTxServices.registerNewUser(u);
				}
				//Se debe buscar el CUstomer User asociado que se desea modificar
				CustomerUser currentCustomerUser = userTxServices.findCustomerUserById(customerUserId);
				currentCustomerUser.setUserId(u.getId());
				userTxServices.updateCustomerUserEmail(currentCustomerUser);
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				return res;
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.USER_ALREADY_EXISTS_CODE);
				res.setReturnMessage(ServiceReturnMessages.USER_ALREADY_EXISTS);
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
