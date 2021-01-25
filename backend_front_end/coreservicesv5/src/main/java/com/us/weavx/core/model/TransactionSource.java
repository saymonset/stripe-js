package com.us.weavx.core.model;

public class TransactionSource {
	
	public static final int DIRECT_TRAFFIC = 0;
	public static final int SCHEDULED = 4;
	
	private int id;
	private String name;
	private String key;
	private String description;
	private long customerId;
	public TransactionSource(int id, String name, String key, String description, long customerId) {
		super();
		this.id = id;
		this.name = name;
		this.key = key;
		this.description = description;
		this.customerId = customerId;
	}
	public TransactionSource(String name, String key, String description, long customerId) {
		super();
		this.name = name;
		this.key = key;
		this.description = description;
		this.customerId = customerId;
	}
	
	public TransactionSource() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	@Override
	public String toString() {
		return "CustomerSource [id=" + id + ", name=" + name + ", key=" + key + ", description=" + description
				+ ", customerId=" + customerId + "]";
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
		TransactionSource other = (TransactionSource) obj;
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
