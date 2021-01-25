package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;

@Component
public class GetPricesEventMethodImpl implements ServiceMethod {

	@Autowired
	private EventRestrictionServicesTx eventRestrictionServices;
	
	@Override
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long applicationId = aInfo.getApplicationId();
			Long eventId = params.get("eventId") != null ? ((Number) params.get("eventId")).longValue() : null;
			if(eventId != null) { 
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
				resp.setReturnDate(LocalDateTime.now().toString());
				HashMap<String, Object> result = new HashMap<>();
				List<Object> list = new ArrayList<>();
				List<Map<String, Object>> eventFundSettings = eventRestrictionServices.getPriceByEventAndApplication(eventId, applicationId);
				if(eventFundSettings != null) {
					eventFundSettings.forEach(eventFundSetting -> {
						list.add(eventFundSetting);
					});
				}
				result.put("pricesEvent", list);
				resp.setResult(result);
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
