package com.us.weavx.core.model;

import java.sql.Timestamp;

public class TransactionDetailInfo {
	
	private long txId;
	private Timestamp txDate;
	private String fund;
	private String cardMasked;
	private String cardBrand;
	private double amount;
	private String status;
	private boolean is_scheduled;
	private String txInternalId;
	private String paymentInfo;

	public TransactionDetailInfo() {
		// TODO Auto-generated constructor stub
	}

	public long getTxId() {
		return txId;
	}

	public void setTxId(long txId) {
		this.txId = txId;
	}

	public Timestamp getTxDate() {
		return txDate;
	}

	public void setTxDate(Timestamp txDate) {
		this.txDate = txDate;
	}

	public String getFund() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public String getCardMasked() {
		return cardMasked;
	}

	public void setCardMasked(String cardMasked) {
		this.cardMasked = cardMasked;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isIs_scheduled() {
		return is_scheduled;
	}

	public void setIs_scheduled(boolean is_scheduled) {
		this.is_scheduled = is_scheduled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	public String getTxInternalId() {
		return txInternalId;
	}

	public void setTxInternalId(String txInternalId) {
		this.txInternalId = txInternalId;
	}
	
	

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	@Override
	public String toString() {
		return "TransactionDetailInfo [txId=" + txId + ", txDate=" + txDate + ", fund=" + fund + ", cardMasked="
				+ cardMasked + ", cardBrand=" + cardBrand + ", amount=" + amount + ", status=" + status
				+ ", is_scheduled=" + is_scheduled + ", txInternalId=" + txInternalId + ", paymentInfo=" + paymentInfo
				+ "]";
	}

	

	


	
	
	

}
