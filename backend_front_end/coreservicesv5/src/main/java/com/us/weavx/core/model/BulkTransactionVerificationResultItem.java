package com.us.weavx.core.model;

public class BulkTransactionVerificationResultItem {
	private BulkTransactionItem item;
	private boolean isValid;
	private String message;
	public BulkTransactionItem getItem() {
		return item;
	}
	public void setItem(BulkTransactionItem item) {
		this.item = item;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "BulkTransactionVerificationResultItem [item=" + item + ", isValid=" + isValid + ", message=" + message
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
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
		BulkTransactionVerificationResultItem other = (BulkTransactionVerificationResultItem) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}
	
	

}
