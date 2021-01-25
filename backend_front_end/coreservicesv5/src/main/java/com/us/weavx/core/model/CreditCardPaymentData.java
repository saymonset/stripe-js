package com.us.weavx.core.model;

public class CreditCardPaymentData extends PaymentData {

	private String creditCardMasked;
	private int creditCardBrandId;
	
	public CreditCardPaymentData() {
		// TODO Auto-generated constructor stub
	}

	public CreditCardPaymentData(long paymentDataId, String creditCardMasked, int creditCardBrandId) {
		super(paymentDataId);
		this.creditCardMasked = creditCardMasked;
		this.creditCardBrandId = creditCardBrandId;
	}

	public String getCreditCardMasked() {
		return creditCardMasked;
	}

	public void setCreditCardMasked(String creditCardMasked) {
		this.creditCardMasked = creditCardMasked;
	}

	public int getCreditCardBrandId() {
		return creditCardBrandId;
	}

	public void setCreditCardBrandId(int creditCardBrandId) {
		this.creditCardBrandId = creditCardBrandId;
	}

	@Override
	public String toString() {
		return "CreditCardPaymentData [creditCardMasked=" + creditCardMasked + ", creditCardBrandId="
				+ creditCardBrandId + "]";
	}
	
	

}
