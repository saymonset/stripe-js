package com.us.weavx.core.model;

public class PaymentTypeDescription {

	private int	paymentTypeId;
	private int langId;
	private String label;
	public PaymentTypeDescription(int paymentTypeId, int langId, String label) {
		super();
		this.paymentTypeId = paymentTypeId;
		this.langId = langId;
		this.label = label;
	}
	public PaymentTypeDescription() {
		super();
	}
	public int getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "PaymentTypeDescription [paymentTypeId=" + paymentTypeId + ", langId=" + langId + ", label=" + label
				+ "]";
	}
	
	

}
