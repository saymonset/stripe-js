package com.us.weavx.core.model;

public class ExternalPaymentData {
	
	private int externalPaymentDataId;
	private String externalPaymentDataTxt;
	public int getExternalPaymentDataId() {
		return externalPaymentDataId;
	}
	public void setExternalPaymentDataId(int externalPaymentDataId) {
		this.externalPaymentDataId = externalPaymentDataId;
	}
	public String getExternalPaymentDataTxt() {
		return externalPaymentDataTxt;
	}
	public void setExternalPaymentDataTxt(String externalPaymentDataTxt) {
		this.externalPaymentDataTxt = externalPaymentDataTxt;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + externalPaymentDataId;
		result = prime * result + ((externalPaymentDataTxt == null) ? 0 : externalPaymentDataTxt.hashCode());
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
		ExternalPaymentData other = (ExternalPaymentData) obj;
		if (externalPaymentDataId != other.externalPaymentDataId)
			return false;
		if (externalPaymentDataTxt == null) {
			if (other.externalPaymentDataTxt != null)
				return false;
		} else if (!externalPaymentDataTxt.equals(other.externalPaymentDataTxt))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ExternalPaymentData [externalPaymentDataId=" + externalPaymentDataId + ", externalPaymentDataTxt="
				+ externalPaymentDataTxt + "]";
	}
	
	

}
