package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.EventDescription;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.Venue;
import com.us.weavx.core.model.VenueDescription;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.VenueServicesTx;
import com.us.weavx.core.util.EventDescriptionManager;
import com.us.weavx.core.util.VenueDescriptionManager;

@Component
public class GetEventVenueDescriptionMethodImpl implements ServiceMethod {
	
	@Autowired
	private EventDescriptionManager managerEvents;
	
	@Autowired
	private VenueDescriptionManager managerVenues;
	
	@Autowired
	private VenueServicesTx venueServicesTx;
	
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
				List<EventDescription> resultTmp = managerEvents.getAllEventDescriptions(eventId);
				
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				res.setReturnDate(LocalDateTime.now().toString());
				
				HashMap<String, Object> result = new HashMap<>();
				List<Object> list = new ArrayList<>();
				list.add(resultTmp);
				
				Venue venue = venueServicesTx.findByEventId(eventId); 
				if(venue != null) {
					List<VenueDescription> resultDescription = managerVenues.getAllVenueDescriptions(venue.getId());
					list.add(resultDescription == null ? new ArrayList<>() : resultDescription);
				}
				
				result.put("eventDescriptions", list);
				res.setResult(result);
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
