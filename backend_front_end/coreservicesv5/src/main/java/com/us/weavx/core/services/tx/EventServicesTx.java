package com.us.weavx.core.services.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.data.EventServicesTxDAO;
import com.us.weavx.core.model.Event;

@Service("eventServicesTx")
public class EventServicesTx {

	@Autowired
	private EventServicesTxDAO dao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Event findEventByIdAndApplicationId(long eventId, long applicationId) {
		return dao.findEventByIdAndApplicationId(eventId, applicationId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Event findEventById(long eventId) {
		return dao.findEventById(eventId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int countTicketsAvalaible(long eventId, long applicationId) {
		return dao.countTicketsAvalaible(eventId, applicationId);
	}
}
