package com.us.weavx.core.model;

import java.util.HashMap;

public class PaymentResult {
	
	public static final int APPROVED = 1;
	public static final int DENIED = 2;
	public static final int ERROR = 3;
	public static final int INVALID_PAYMENT_MODE = 4;
	public static final int AUTH_KEYS_NOT_FOUND = 5;
	public static final int PARAMETER_MISSING = 6;
	public static final int OTHER = 7;
	public static final int NETWORK_ERROR = 8;
	public static final int NO_RESPONSE = 9;
	public static final int RUNTIME_ERROR = 10;
	public static final int USER_DATA_MISSING = 11;
	public static final int INVALID_PAYMENT_GATEWAY = 12;
	public static final int DISABLED_PAYMENT_GATEWAY = 13;
	public static final int PREPAYMENT_ERROR = 14;
	public static final int POSTPAYMENT_ERROR = 15;
	public static final int ABORTED_TRANSACTION = 16;
	public static final int NOT_PROVISIONED = 17;
	public static final int COUPON_NOT_VALID = 18;
	public static final int NOT_FREE_TRANSACTION = 19;
	public static final int EXTERNAL_PAYMENT_METHOD_NOT_VALID = 20;
	public static final int FRAUD_TRANSACTION = 21;
	
	private int result;
	private String resultMessage;
	private HashMap<String, Object> authorizationInfo;
	
	
	public PaymentResult() {
		super();
	}
	public PaymentResult(int result, HashMap<String, Object> authorizationInfo) {
		super();
		this.result = result;
		this.authorizationInfo = authorizationInfo;
	}
	
	public PaymentResult(int result, String resultMessage, HashMap<String, Object> authorizationInfo) {
		super();
		this.result = result;
		this.resultMessage = resultMessage;
		this.authorizationInfo = authorizationInfo;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public HashMap<String, Object> getAuthorizationInfo() {
		return authorizationInfo;
	}
	public void setAuthorizationInfo(HashMap<String, Object> authorizationInfo) {
		this.authorizationInfo = authorizationInfo;
	}
	
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	@Override
	public String toString() {
		return "PaymentResult [result=" + result + ", resultMessage=" + resultMessage + ", authorizationInfo="
				+ authorizationInfo + "]";
	}
	
	
	

}
