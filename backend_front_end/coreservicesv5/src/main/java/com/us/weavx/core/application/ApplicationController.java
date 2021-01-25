package com.us.weavx.core.application;

import java.util.Map;

import com.us.weavx.core.exception.ApplicationControllerGeneralException;
import com.us.weavx.core.model.ApplicationControllerResult;

public interface ApplicationController {
	
	public ApplicationControllerResult onInitBuy(Map<String, Object> parameters) throws ApplicationControllerGeneralException; 
	public ApplicationControllerResult onFinishBuy(Map<String, Object> parameters) throws ApplicationControllerGeneralException; 
	public void sendPurchaseReceipt(Map<String, Object> parameters) throws ApplicationControllerGeneralException;	

}
