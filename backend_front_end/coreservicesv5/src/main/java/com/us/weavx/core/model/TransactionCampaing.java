package com.us.weavx.core.model;

import java.sql.Timestamp;

public class TransactionCampaing {
	
	public static final long NO_CAMPAING = 0;
	
	private long id;
	private String name;
	private String key;
	private String description;
	private Timestamp validFrom;
	private Timestamp validTo;
	private long customerId;
	
	public TransactionCampaing(long id, String name, String key, String description, Timestamp validFrom,
			Timestamp validTo, long customerId) {
		super();
		this.id = id;
		this.name = name;
		this.key = key;
		this.description = description;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.customerId = customerId;
	}
	public TransactionCampaing(String name, String key, String description, Timestamp validFrom, Timestamp validTo,
			long customerId) {
		super();
		this.name = name;
		this.key = key;
		this.description = description;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.customerId = customerId;
	}
	public TransactionCampaing() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}
	public Timestamp getValidTo() {
		return validTo;
	}
	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	@Override
	public String toString() {
		return "TransactionCampaing [id=" + id + ", name=" + name + ", key=" + key + ", description=" + description
				+ ", validFrom=" + validFrom + ", validTo=" + validTo + ", customerId=" + customerId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionCampaing other = (TransactionCampaing) obj;
		if (customerId != other.customerId)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	
	
	
}
