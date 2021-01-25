package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Event;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventServicesTx;

@Component
public class FindEventApplicationMethodImpl implements ServiceMethod{

	@Autowired
	private EventServicesTx eventServicesTx;
	
	@Override
	public Response executeMethod(Request request) {
		Response resp = new Response();
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long eventId = ((Number) params.get("eventId")).longValue();
			long applicationId = aInfo.getApplicationId();
			Event event = eventServicesTx.findEventByIdAndApplicationId(eventId, applicationId);
			
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("events", Arrays.asList(new Object[]{event}));
			resp.setReturnDate(LocalDateTime.now().toString());
			resp.setResult(result);
		} catch (Exception e) {
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" e:"+e.getMessage());
		}
		return resp;
	}

}
