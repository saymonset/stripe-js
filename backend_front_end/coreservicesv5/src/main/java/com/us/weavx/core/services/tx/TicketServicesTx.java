package com.us.weavx.core.services.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.data.TicketServicesTxDAO;
import com.us.weavx.core.model.Ticket;
import com.us.weavx.core.model.TicketHistory;
import com.us.weavx.core.model.TicketStatus;

@Service("ticketServicesTx")
public class TicketServicesTx {

	@Autowired
	private TicketServicesTxDAO dao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public TicketStatus findTicketStatusByName(String name) {
		return dao.findTicketStatusByName(name);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Ticket addTicket(Ticket ticket) {
		return dao.addTicket(ticket);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public TicketHistory addTicketHistory(TicketHistory ticketHistory) {
		return dao.addTicketHistory(ticketHistory);
	}
}
