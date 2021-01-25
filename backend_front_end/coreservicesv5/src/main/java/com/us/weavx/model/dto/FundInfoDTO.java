package com.us.weavx.model.dto;

public class FundInfoDTO {
	
	private String fundDescription;
	private double amount;
	public String getFundDescription() {
		return fundDescription;
	}
	public void setFundDescription(String fundDescription) {
		this.fundDescription = fundDescription;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "FundInfoDTO [fundDescription=" + fundDescription + ", amount=" + amount + "]";
	}
	
	

}
