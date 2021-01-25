package com.us.weavx.model.dto;

import java.sql.Timestamp;
import java.util.List;

public class TransactionInfoDTO {
	
	private long id;
	private long customerId;
	private long applicationId;
	private Timestamp date;
	private String language;
	private String status;
	private double amount;
	private String source;
	private String campaing;
	private String medium;
	private boolean isScheduled;
	private List<FundInfoDTO> funds;
	private long countryId;
	private String countryName;
	private int continentId;
	private String continentName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCampaing() {
		return campaing;
	}
	public void setCampaing(String campaing) {
		this.campaing = campaing;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public List<FundInfoDTO> getFunds() {
		return funds;
	}
	public void setFunds(List<FundInfoDTO> funds) {
		this.funds = funds;
	}
	
	public boolean isScheduled() {
		return isScheduled;
	}
	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}		
	public long getCountryId() {
		return countryId;
	}
	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}		
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public int getContinentId() {
		return continentId;
	}
	public void setContinentId(int continentId) {
		this.continentId = continentId;
	}	
	public String getContinentName() {
		return continentName;
	}
	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}
	
	@Override
	public String toString() {
		return "TransactionInfoDTO [id=" + id + ", customerId=" + customerId + ", applicationId=" + applicationId
				+ ", date=" + date + ", language=" + language + ", status=" + status + ", amount=" + amount
				+ ", source=" + source + ", campaing=" + campaing + ", medium=" + medium + ", isScheduled="
				+ isScheduled + ", funds=" + funds + ", countryId=" + countryId + ", countryName=" + countryName
				+ ", continentId=" + continentId + ", continentName=" + continentName + "]";
	}
		
}
