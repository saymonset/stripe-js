package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.EventDescription;
import com.us.weavx.core.model.EventDescriptionKey;
import com.us.weavx.core.services.tx.ConfigurationTxServices;

@Component
public class EventDescriptionManager {
	@Autowired
	private ConfigurationTxServices configurationServices;
	
	private Map<Long, Map<EventDescriptionKey, EventDescription>> eventDescriptions;
	
	public EventDescriptionManager() {
		eventDescriptions = new HashMap<>();
	}
		
	private void loadeventDescriptions(long eventId) {
		List<EventDescription> events = configurationServices.getEventDescriptions(eventId);
		Map<EventDescriptionKey, EventDescription> eventsMap = new HashMap<>();
		for (EventDescription item : events) {
			eventsMap.put(new EventDescriptionKey(item.getEventId(),item.getLangId(),item.getTagId()), item);
		}
		eventDescriptions.put(eventId, eventsMap);
	}
	
	public EventDescription getEventDescriptions(long eventId, EventDescriptionKey key) {
		Map<EventDescriptionKey, EventDescription> eventMap = eventDescriptions.get(eventId);
		if (eventMap == null) {
			loadeventDescriptions(eventId);
		}
		eventMap = eventDescriptions.get(eventId);
		if (eventMap == null) {
			return null;
		}
		EventDescription myEvent = eventMap.get(key);
		return myEvent;
	}
	
	public List<EventDescription> getAllEventDescriptions(long eventId) {
		Map<EventDescriptionKey, EventDescription> eventMap = eventDescriptions.get(eventId);
		if (eventMap == null || eventMap.isEmpty()) {
			loadeventDescriptions(eventId);
		}
		return new ArrayList<>(eventDescriptions.get(eventId).values());
	}
	
	public void reloadeventDescriptions(long eventId) {
		loadeventDescriptions(eventId);
	}
}
