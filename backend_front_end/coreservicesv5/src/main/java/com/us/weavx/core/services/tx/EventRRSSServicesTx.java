package com.us.weavx.core.services.tx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.data.EventRRSSServicesTxDAO;
import com.us.weavx.core.model.EventRRSS;

@Service("eventRRSSServicesTx")
public class EventRRSSServicesTx {

	@Autowired
	private EventRRSSServicesTxDAO dao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<EventRRSS> findByEventIdAndApplicationId(long eventId, long applicationId) {
		return dao.findByEventByIdAndApplicationId(eventId, applicationId);
	}
}
