package com.us.weavx.core.paymentgw;

import java.util.HashMap;

public class GWTransactionInfo {
	private String gwTransactionId;
	private HashMap<String, Object> transactionData;
	public GWTransactionInfo(String gwTransactionId, HashMap<String, Object> transactionData) {
		super();
		this.gwTransactionId = gwTransactionId;
		this.transactionData = transactionData;
	}
	public GWTransactionInfo() {
		// TODO Auto-generated constructor stub
	}
	public String getGwTransactionId() {
		return gwTransactionId;
	}
	public void setGwTransactionId(String gwTransactionId) {
		this.gwTransactionId = gwTransactionId;
	}
	public HashMap<String, Object> getTransactionData() {
		return transactionData;
	}
	public void setTransactionData(HashMap<String, Object> transactionData) {
		this.transactionData = transactionData;
	}
	@Override
	public String toString() {
		return "GWTransactionInfo [gwTransactionId=" + gwTransactionId + ", transactionData=" + transactionData + "]";
	}
	
	

}
