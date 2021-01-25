package com.us.weavx.core.model;


public class FundTicketAvailabilityDescription {
	private long fundId;
	private int capacity;
	
	public FundTicketAvailabilityDescription() {
		super();
	}
	public long getFundId() {
		return fundId;
	}

	public void setFundId(long fundId) {
		this.fundId = fundId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int i) {
		this.capacity = i;
	}
}	