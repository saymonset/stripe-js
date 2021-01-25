package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.EventRRSS;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventRRSSServicesTx;

@Component
public class FindEventRRSSDescriptionsMethodImpl implements ServiceMethod {

	@Autowired
	private EventRRSSServicesTx service;

	@Override
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			Long eventId =   params.get("eventId") != null ? ((Number) params.get("eventId")).longValue() : null; 
			if(eventId == null) {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.UNKNOWN_OBJECT_CODE);
				resp.setReturnMessage(ServiceReturnMessages.UNKNOWN_OBJECT);
				resp.setReturnDate(LocalDateTime.now().toString());
				return resp;
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				res.setReturnDate(LocalDateTime.now().toString());
				
				HashMap<String, Object> result = new HashMap<>();
				List<EventRRSS> eventRRSSList = service.findByEventIdAndApplicationId(eventId, aInfo.getApplicationId());
				
				result.put("eventRRSSDescriptions", eventRRSSList);
				res.setResult(result);
				return res;
			}
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" e:"+e.getMessage());
			resp.setReturnDate(LocalDateTime.now().toString());
			return resp;
		}
	}
}
