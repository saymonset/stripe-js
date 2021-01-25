package com.us.weavx.core.model;

import java.util.Date;

public class TicketHistory {

	private long id;
	private long ticketId;
	private Date historyDate;
	private int statusId;
	private String statusName;	
	private Date createdAt;
	
	public TicketHistory(long id, long ticketId, Date historyDate, int statusId, String statusName, Date createdAt) {
		super();
		this.id = id;
		this.ticketId = ticketId;
		this.historyDate = historyDate;
		this.statusId = statusId;
		this.statusName = statusName;
		this.createdAt = createdAt;
	}
	
	public TicketHistory(long ticketId, Date historyDate, int statusId, String statusName) {
		super();
		this.ticketId = ticketId;
		this.historyDate = historyDate;
		this.statusId = statusId;
		this.statusName = statusName;
	}

	public TicketHistory(long ticketId, Date historyDate, int statusId, String statusName, Date createdAt) {
		super();
		this.ticketId = ticketId;
		this.historyDate = historyDate;
		this.statusId = statusId;
		this.statusName = statusName;
		this.createdAt = createdAt;
	}

	public TicketHistory() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTicketId() {
		return ticketId;
	}

	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "TicketHistory [id=" + id + ", ticketId=" + ticketId + ", historyDate=" + historyDate + ", statusId="
				+ statusId + ", statusName=" + statusName + ", createdAt=" + createdAt + "]";
	}
	
	
}
