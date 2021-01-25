package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.Venue;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventServicesTx;
import com.us.weavx.core.services.tx.VenueServicesTx;

@Component
public class GetTicketsAvailabilityMethodImpl implements ServiceMethod {

	@Autowired
	private EventServicesTx eventServicesTx;
	
	@Autowired
	private VenueServicesTx venueServicesTx;
	
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
				return resp;
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				List<Object> list = new ArrayList<>();
				
				int countAssistant = eventServicesTx.countTicketsAvalaible(eventId, aInfo.getApplicationId());
				Venue venue = venueServicesTx.findByEventId(eventId);
				if(venue != null) {
					list.add(venue.getCapacity() - countAssistant);
				} else {
					list.add(0);
				}
				
				result.put("ticketsAvailability", list);
				res.setResult(result);
				res.setReturnDate(LocalDateTime.now().toString());
				return res;
			}
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
			return res;
		}
	}

}
