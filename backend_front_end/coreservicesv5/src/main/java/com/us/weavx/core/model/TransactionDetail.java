package com.us.weavx.core.model;

public class TransactionDetail {
	
	private long id;
	private long transactionId;
	private int fundId;
	private double amount;
	public TransactionDetail(long id, long transactionId, int fundId, double amount) {
		super();
		this.id = id;
		this.transactionId = transactionId;
		this.fundId = fundId;
		this.amount = amount;
	}
	public TransactionDetail(long transactionId, int fundId, double amount) {
		super();
		this.transactionId = transactionId;
		this.fundId = fundId;
		this.amount = amount;
	}
	public TransactionDetail() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public int getFundId() {
		return fundId;
	}
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "TransactionDetail [id=" + id + ", transactionId=" + transactionId + ", fundId=" + fundId + ", amount="
				+ amount + "]";
	}
	
	

}
