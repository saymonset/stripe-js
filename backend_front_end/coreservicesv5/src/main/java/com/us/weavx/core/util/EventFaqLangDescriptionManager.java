package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.EventFaqLangDescription;
import com.us.weavx.core.model.EventFaqLangDescriptionKey;
import com.us.weavx.core.services.tx.ConfigurationTxServices;

@Component
public class EventFaqLangDescriptionManager {
	@Autowired
	private ConfigurationTxServices configurationServices;
	
	private Map<Long, Map<EventFaqLangDescriptionKey, EventFaqLangDescription>> EventFaqLangDescriptions;
	
	public EventFaqLangDescriptionManager() {
		EventFaqLangDescriptions = new HashMap<>();
	}
		
	private void loadEventFaqLangDescriptions(long eventId) {
		List<EventFaqLangDescription> events = configurationServices.getEventFaqLanfDescriptions(eventId);
		Map<EventFaqLangDescriptionKey, EventFaqLangDescription> eventsMap = new HashMap<>();
		for (EventFaqLangDescription item : events) {
			eventsMap.put(new EventFaqLangDescriptionKey(item.getEventId(),item.getLangId(),item.getTagId()), item);
		}
		EventFaqLangDescriptions.put(eventId, eventsMap);
	}
	
	public EventFaqLangDescription getEventFaqLangDescriptions(long eventId, EventFaqLangDescriptionKey key) {
		Map<EventFaqLangDescriptionKey, EventFaqLangDescription> eventMap = EventFaqLangDescriptions.get(eventId);
		if (eventMap == null) {
			loadEventFaqLangDescriptions(eventId);
		}
		eventMap = EventFaqLangDescriptions.get(eventId);
		if (eventMap == null) {
			return null;
		}
		EventFaqLangDescription myEvent = eventMap.get(key);
		return myEvent;
	}
	
	public List<EventFaqLangDescription> getAllEventFaqLangDescriptions(long eventId) {
		Map<EventFaqLangDescriptionKey, EventFaqLangDescription> eventMap = EventFaqLangDescriptions.get(eventId);
		if (eventMap == null || eventMap.isEmpty()) {
			loadEventFaqLangDescriptions(eventId);
		}
		return new ArrayList<>(EventFaqLangDescriptions.get(eventId).values());
	}
	
	public void reloadEventFaqLangDescriptions(long eventId) {
		loadEventFaqLangDescriptions(eventId);
	}
}
