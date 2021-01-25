package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.InvalidEmailAddressException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.RestrictedEventAttendee;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;
@Component
public class UpdateRestrictedEventAttendeeMethodImpl implements ServiceMethod {
	@Autowired
	private EventRestrictionServicesTx eventRestrictionServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			HashMap<String, Object> attendeeParam = (HashMap<String, Object>) params.get("attendee");
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			RestrictedEventAttendee attendee = new RestrictedEventAttendee();
			attendee.setId(((Number) attendeeParam.get("id")).longValue());
			attendee.setEmail((String) attendeeParam.get("email"));
			attendee.setCustomerId(aInfo.getCustomerId());
			attendee.setApplicationId(aInfo.getApplicationId());
			attendee.setActive(true);
			//Se inicializa el container de spring
			attendee = eventRestrictionServices.updateRestrictedEventAttendee(attendee);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			attendee.setCustomerId(aInfo.getCustomerId());
			attendee.setApplicationId(aInfo.getApplicationId());
			result.put("attendee", attendee);
			res.setResult(result);
			return res;
		} catch (InvalidEmailAddressException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.INVALID_EMAIL_ADDRRESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.INVALID_EMAIL_ADDRESS);
			return res;
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		}
	}

	
	

}
