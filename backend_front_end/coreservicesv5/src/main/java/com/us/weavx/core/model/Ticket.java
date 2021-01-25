package com.us.weavx.core.model;

public class Ticket {

	private long id;
	private String ticketSerial;
	private long assistantId;
	private int statusId;
	
	public Ticket(long id, String ticketSerial, long assistantId, int statusId) {
		super();
		this.id = id;
		this.ticketSerial = ticketSerial;
		this.assistantId = assistantId;
		this.statusId = statusId;
	}
	
	public Ticket(String ticketSerial, long assistantId, int statusId) {
		super();
		this.ticketSerial = ticketSerial;
		this.assistantId = assistantId;
		this.statusId = statusId;
	}
	
	public Ticket() {
		super();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTicketSerial() {
		return ticketSerial;
	}
	public void setTicketSerial(String ticketSerial) {
		this.ticketSerial = ticketSerial;
	}
	public long getAssistantId() {
		return assistantId;
	}
	public void setAssistantId(long assistantId) {
		this.assistantId = assistantId;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Override
	public String toString() {
		return "Ticket [id=" + id + ", ticketSerial=" + ticketSerial + ", assistantId=" + assistantId + ", statusId="
				+ statusId + "]";
	}
}
