package com.us.weavx.core.model;

public class TransactionMedium {

	public static final long DEFAULT_MEDIUM = 0;
	
	private long id;
	private String name;
	private String key;
	private String description;
	private long customer_id;
	public TransactionMedium() {
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
	public long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}
	@Override
	public String toString() {
		return "TransactionMedium [id=" + id + ", name=" + name + ", key=" + key + ", description=" + description
				+ ", customer_id=" + customer_id + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (customer_id ^ (customer_id >>> 32));
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
		TransactionMedium other = (TransactionMedium) obj;
		if (customer_id != other.customer_id)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	
	
}
