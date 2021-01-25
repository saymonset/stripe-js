package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.RestrictedEventAttendee;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;
import com.us.weavx.core.services.tx.UserTxServices;
import org.apache.commons.validator.routines.EmailValidator;

@Component
public class ExistsUserMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userTxServices;
	@Autowired
	private EventRestrictionServicesTx eventRestrictionTxServices;

	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			String email = (String) params.get("email");
			if (!EmailValidator.getInstance().isValid(email)) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.INVALID_EMAIL_ADDRRESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.INVALID_EMAIL_ADDRESS);
				return res;					
			}
			if (eventRestrictionTxServices.isRestrictedEvent(customerId, aInfo.getApplicationId())) {
				//Se verifica si el usuario tiene acceso al evento
				RestrictedEventAttendee attendee = eventRestrictionTxServices.findEventAttendeeByCustomerIdApplicationIdAndEmail(customerId, aInfo.getApplicationId(), email);
				if (attendee == null) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.RESTRICTED_EVENT_CODE);
					res.setReturnMessage(ServiceReturnMessages.RESTRICTED_EVENT);
					return res;
				}
			}
			Long customerUserId = userTxServices.findCustomerUserIdByEmail(email, customerId);
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				if (customerUserId != null) {
					result.put("existsUser", true);
					result.put("customerUserId", customerUserId);
				} else {
					result.put("existsUser", false);
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
