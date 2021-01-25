package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.exception.UnknownCustomerUserException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class UpdateRegisteredCustomerUserMethodImpl implements ServiceMethod {
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
			CustomerUser updatedUser = new CustomerUser();
			updatedUser.setId(((Number) userParam.get("id")).longValue());
			updatedUser.setCustomerId(customerId);
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
			updatedUser.setFirstName(fieldTmp);
			fieldTmp = (String) userParam.get("lastName");
			if (fieldTmp == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.LAST_NAME_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.LAST_NAME_REQUIRED);
				return res;
			}
			fieldTmp = WordUtils.capitalizeFully(fieldTmp.toLowerCase());
			updatedUser.setLastName(fieldTmp);
			fieldTmp = (String) userParam.get("profileImage");
			updatedUser.setProfileImage(fieldTmp);
			fieldIntTmp = (Integer) userParam.get("countryId");
			if (fieldIntTmp != null)
				updatedUser.setCountryId(fieldIntTmp);
			fieldIntTmp = (Integer) userParam.get("stateId");
			if (fieldIntTmp != null)
				updatedUser.setStatedId(fieldIntTmp);
			fieldIntTmp = (Integer) userParam.get("cityId");
			if (fieldIntTmp != null) 
				updatedUser.setCityId(fieldIntTmp);
			fieldTmp = (String) userParam.get("stateText");
			updatedUser.setStateText(fieldTmp);
			fieldTmp = (String) userParam.get("cityText");
			updatedUser.setCityText(fieldTmp);
			fieldTmp = (String) userParam.get("address");
			updatedUser.setAddress(fieldTmp);
			fieldTmp = (String) userParam.get("postalCode");
			updatedUser.setPostalCode(fieldTmp);
			AuthenticatedUserInfo uInfo = userServices.updateCustomerUser(updatedUser, aInfo.getApplicationId(), ipAddress, userAgent);
			updatedUser = uInfo.getCustomerUser().getCustUser();
			if (updatedUser == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER_CODE);
				res.setReturnMessage(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER);
				return res;
			}
			updatedUser.setPassword("****");
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("customer_user", updatedUser);
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
		}
	}

	
	

}
