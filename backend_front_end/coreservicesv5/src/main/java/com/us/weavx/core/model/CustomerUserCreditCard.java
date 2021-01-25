package com.us.weavx.core.model;

public class CustomerUserCreditCard {
	
	private String cardLast4;
	private String brand;
	private String cardId;
	private String expMonth;
	private String expYear;
	private boolean isDefault;

	public CustomerUserCreditCard() {
		
	}

	public CustomerUserCreditCard(String cardLast4, String brand, String cardId, String expMonth, String expYear,
			boolean isDefault) {
		super();
		this.cardLast4 = cardLast4;
		this.brand = brand;
		this.cardId = cardId;
		this.expMonth = expMonth;
		this.expYear = expYear;
		this.isDefault = isDefault;
	}

	public String getCardLast4() {
		return cardLast4;
	}

	public void setCardLast4(String cardLast4) {
		this.cardLast4 = cardLast4;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "CustomerUserCreditCard [cardLast4=" + cardLast4 + ", brand=" + brand + ", cardId=" + cardId
				+ ", expMonth=" + expMonth + ", expYear=" + expYear + ", isDefault=" + isDefault + "]";
	}

	

	
	

}
