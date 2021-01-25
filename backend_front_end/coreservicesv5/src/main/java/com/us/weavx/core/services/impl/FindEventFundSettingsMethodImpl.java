package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.EventFundSettings;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;
@Component
public class FindEventFundSettingsMethodImpl implements ServiceMethod {
	@Autowired
	private EventRestrictionServicesTx eventRestrictionServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			long fundId = ((Number) params.get("fundId")).longValue(); 
			Long eventId =   params.get("eventId") != null ? ((Number) params.get("eventId")).longValue() : null; 
			EventFundSettings eventFundSettings;
			if(eventId == null) {
				eventFundSettings = eventRestrictionServices.findEventFundSettingsByCustomerIdAndApplicationId(fundId, customerId, applicationId);
			} else {	
				eventFundSettings = eventRestrictionServices.findEventFundSettingsByCustomerIdAndApplicationIdAndEventId(fundId, customerId, applicationId, eventId);	
			}
			if (eventFundSettings != null) {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				result.put("eventFundSettings", eventFundSettings);
				resp.setResult(result);
				resp.setReturnDate(LocalDateTime.now().toString());
				return resp;
			} else {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.UNKNOWN_OBJECT_CODE);
				resp.setReturnMessage(ServiceReturnMessages.UNKNOWN_OBJECT);
				return resp;
			}
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());
			return resp;
		}
	}
}
