package com.us.weavx.core.model;

import java.util.HashMap;

public class ApplicationControllerResult {

	public static final int CONTINUE_TRANSACTION = 0;
	public static final int ABORT_TRANSACTION = 1;
	public static final int OK = 2;
	public static final int ERROR = 3;
	
	private int result;
	private String resultMessage;
	private HashMap<String, Object> resultParameters;
	
	public ApplicationControllerResult() {
	}

	public ApplicationControllerResult(int result, String resultMessage, HashMap<String, Object> resultParameters) {
		super();
		this.result = result;
		this.resultMessage = resultMessage;
		this.resultParameters = resultParameters;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public HashMap<String, Object> getResultParameters() {
		return resultParameters;
	}

	public void setResultParameters(HashMap<String, Object> resultParameters) {
		this.resultParameters = resultParameters;
	}

	@Override
	public String toString() {
		return "ApplicationControllerResult [result=" + result + ", resultMessage=" + resultMessage
				+ ", resultParameters=" + resultParameters + "]";
	}
	
	

}
