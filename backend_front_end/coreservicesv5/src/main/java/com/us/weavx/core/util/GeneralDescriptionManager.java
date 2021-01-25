package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.GeneralDescription;
import com.us.weavx.core.model.GeneralDescriptionKey;
import com.us.weavx.core.services.tx.ConfigurationTxServices;

@Component
public class GeneralDescriptionManager {

	@Autowired
	private ConfigurationTxServices configurationServices;
	
	private Map<Long, Map<GeneralDescriptionKey, GeneralDescription>> generalDescriptions;
	
	public GeneralDescriptionManager() {
		generalDescriptions = new HashMap<>();
	}
		
	private void loadgeneralDescriptions(long customerId) {
		List<GeneralDescription> events = configurationServices.getGeneralDescriptions(customerId);
		Map<GeneralDescriptionKey, GeneralDescription> eventsMap = new HashMap<>();
		for (GeneralDescription item : events) {
			eventsMap.put(new GeneralDescriptionKey(item.getCustomerId(), item.getLangId(), item.getTagId()), item);
		}
		generalDescriptions.put(customerId, eventsMap);
	}
	
	public GeneralDescription getGeneralDescriptions(long customerId, GeneralDescriptionKey key) {
		Map<GeneralDescriptionKey, GeneralDescription> eventMap = generalDescriptions.get(customerId);
		if (eventMap == null) {
			loadgeneralDescriptions(customerId);
		}
		eventMap = generalDescriptions.get(customerId);
		if (eventMap == null) {
			return null;
		}
		GeneralDescription myEvent = eventMap.get(key);
		return myEvent;
	}
	
	public List<GeneralDescription> getAllGeneralDescriptions(long customerId) {
		Map<GeneralDescriptionKey, GeneralDescription> eventMap = generalDescriptions.get(customerId);
		if (eventMap == null || eventMap.isEmpty()) {
			loadgeneralDescriptions(customerId);
		}
		return new ArrayList<>(generalDescriptions.get(customerId).values());
	}
	
	public void reloadeneralDescriptions(long customerId) {
		loadgeneralDescriptions(customerId);
	}

}
