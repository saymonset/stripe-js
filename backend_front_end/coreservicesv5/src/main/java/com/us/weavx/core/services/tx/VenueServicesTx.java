package com.us.weavx.core.services.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.data.VenueServicesTxDAO;
import com.us.weavx.core.model.Venue;

@Service("venueServicesTx")
public class VenueServicesTx {

	@Autowired
	private VenueServicesTxDAO dao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Venue findByEventId(long eventId) {
		return dao.findVenueByEventId(eventId);
	}
}
