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
import com.us.weavx.core.model.Venue;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventServicesTx;
import com.us.weavx.core.services.tx.VenueServicesTx;

@Component
public class FindVenueEventMethodImpl implements ServiceMethod{

	@Autowired
	private EventServicesTx eventServicesTx;
	
	@Autowired
	private VenueServicesTx venueServicesTx;
	
	@Override
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long eventId = ((Number) params.get("eventId")).longValue();
			Event event = eventServicesTx.findEventByIdAndApplicationId(eventId, aInfo.getApplicationId());
			Venue venue = event == null ? null : venueServicesTx.findByEventId(event.getId());
			if (venue != null) {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				result.put("eventVenue", Arrays.asList(new Object[]{event, venue}));
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
