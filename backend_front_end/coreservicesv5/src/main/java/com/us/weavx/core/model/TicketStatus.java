package com.us.weavx.core.model;

public class TicketStatus {

	private int id;
	private String statusName;
	
	public TicketStatus(int id, String statusName) {
		super();
		this.id = id;
		this.statusName = statusName;
	}
	
	public TicketStatus(String statusName) {
		super();
		this.statusName = statusName;
	}
	
	public TicketStatus() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Override
	public String toString() {
		return "TicketStatus [id=" + id + ", statusName=" + statusName + "]";
	}
}
