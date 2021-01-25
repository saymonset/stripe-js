package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.VenueDescription;
import com.us.weavx.core.model.VenueDescriptionKey;
import com.us.weavx.core.services.tx.ConfigurationTxServices;

@Component
public class VenueDescriptionManager {
	@Autowired
	private ConfigurationTxServices configurationServices;
	
	private Map<Long, Map<VenueDescriptionKey, VenueDescription>> venueDescriptions;
	
	public VenueDescriptionManager() {
		venueDescriptions = new HashMap<>();
	}
		
	private void loadVenueDescriptions(long venueId) {
		List<VenueDescription> events = configurationServices.getVenueDescriptions(venueId);
		Map<VenueDescriptionKey, VenueDescription> eventsMap = new HashMap<>();
		for (VenueDescription item : events) {
			eventsMap.put(new VenueDescriptionKey(item.getVenueId(),item.getLangId(), item.getTagId()), item);
		}
		venueDescriptions.put(venueId, eventsMap);
	}
	
	public VenueDescription getVenueDescriptions(long venueId, VenueDescriptionKey key) {
		Map<VenueDescriptionKey, VenueDescription> eventMap = venueDescriptions.get(venueId);
		if (eventMap == null) {
			loadVenueDescriptions(venueId);
		}
		eventMap = venueDescriptions.get(venueId);
		if (eventMap == null) {
			return null;
		}
		VenueDescription myEvent = eventMap.get(key);
		return myEvent;
	}
	
	public List<VenueDescription> getAllVenueDescriptions(long venueId) {
		Map<VenueDescriptionKey, VenueDescription> eventMap = venueDescriptions.get(venueId);
		if (eventMap == null || eventMap.isEmpty()) {
			loadVenueDescriptions(venueId);
		}
		return new ArrayList<>(venueDescriptions.get(venueId).values());
	}
	
	public void reloadVenueDescriptions(long venueId) {
		loadVenueDescriptions(venueId);
	}
}
