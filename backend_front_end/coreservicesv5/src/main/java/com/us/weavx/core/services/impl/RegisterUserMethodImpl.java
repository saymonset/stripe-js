package com.us.weavx.core.services.impl;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.User;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.util.CoreServicesSecurity;
import com.us.weavx.core.util.GeneralUtilities;
@Component
public class RegisterUserMethodImpl implements ServiceMethod {
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
			HashMap<String, Object> userParam = (HashMap<String, Object>) params.get("user");
			User user = null;
			String email = ((String) userParam.get("email")).toLowerCase(); 
			if (!EmailValidator.getInstance().isValid(email)) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.INVALID_EMAIL_ADDRRESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.INVALID_EMAIL_ADDRESS);
				return res;					
			}
			user = userServices.findUserByEmail(email);
			if (user == null) {
				//Usuario no existe y hay que crearlo
				user = new User(email);
				user = userServices.registerNewUser(user);
				if (user == null) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
					res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
					return res;
				}
			}
			//Ahora se verifica si existe el usuario para el cliente asociado al token
			Long customerUserIdTmp = userServices.existsCustomerUser(user.getId(), aInfo.getCustomerId());
			if (customerUserIdTmp != null){
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.USER_ALREADY_EXISTS_CODE);
				res.setReturnMessage(ServiceReturnMessages.USER_ALREADY_EXISTS);
				HashMap<String, Object> result = new HashMap<>();
				result.put("customerUserId", customerUserIdTmp);
				res.setResult(result);
				return res;
			}
			//Se trata definitivamente de un nuevo usuario para el cliente
			CustomerUser newUser = new CustomerUser();
			newUser.setUserId(user.getId());
			newUser.setCustomerId(customerId);
			String fieldTmp = null;
			Integer fieldIntTmp = null;
			fieldTmp = (String) userParam.get("firstName");
			if (fieldTmp == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.FIRST_NAME_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.FIRST_NAME_REQUIRED);
				return res;
			}
			fieldTmp = WordUtils.capitalizeFully(fieldTmp.toLowerCase());
			newUser.setFirstName(fieldTmp);
			fieldTmp = (String) userParam.get("lastName");
			if (fieldTmp == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.LAST_NAME_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.LAST_NAME_REQUIRED);
				return res;
			}
			fieldTmp = WordUtils.capitalizeFully(fieldTmp.toLowerCase());
			newUser.setLastName(fieldTmp);
			fieldTmp = (String) userParam.get("password");
			if (fieldTmp == null) {
				//Generar password aleatorio
				fieldTmp = CoreServicesSecurity.generateRandomPassword();
			}
			fieldTmp = org.apache.commons.codec.digest.DigestUtils.sha256Hex(fieldTmp);
			newUser.setPassword(fieldTmp);
			//Se obtienen el resto de los campos pues no son obligatorios
			fieldIntTmp = (Integer) userParam.get("genderId");
			if (fieldIntTmp != null) 
				newUser.setGenderId(fieldIntTmp);
			fieldTmp = (String) userParam.get("birthDate");
			//Se valida que sea una fecha valida
			Date date = GeneralUtilities.parseDate(fieldTmp);
			newUser.setBirthDate(date);
			fieldTmp = (String) userParam.get("profileImage");
			newUser.setProfileImage(fieldTmp);
			fieldIntTmp = (Integer) userParam.get("countryId");
			if (fieldIntTmp != null)
				newUser.setCountryId(fieldIntTmp);
			fieldIntTmp = (Integer) userParam.get("stateId");
			if (fieldIntTmp != null)
				newUser.setStatedId(fieldIntTmp);
			fieldIntTmp = (Integer) userParam.get("cityId");
			if (fieldIntTmp != null) 
				newUser.setCityId(fieldIntTmp);
			fieldTmp = (String) userParam.get("stateText");
			newUser.setStateText(fieldTmp);
			fieldTmp = (String) userParam.get("cityText");
			newUser.setCityText(fieldTmp);
			fieldTmp = (String) userParam.get("address");
			newUser.setAddress(fieldTmp);
			fieldTmp = (String) userParam.get("postalCode");
			newUser.setPostalCode(fieldTmp);
			fieldTmp = (String) userParam.get("phoneNumber");
			newUser.setPhoneNumber(fieldTmp);
			AuthenticatedUserInfo userInfo = userServices.registerNewCustomerUser(newUser, aInfo.getApplicationId(), ipAddress, userAgent);
			newUser = userInfo.getCustomerUser().getCustUser();
			newUser.setPassword("****");
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customer_user", newUser);
			if (userInfo.getUserAccessToken() != null) {
				result.put("userAccessToken", userInfo.getUserAccessToken());
			}
			res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
			return res;
		}
	}

	
	

}
