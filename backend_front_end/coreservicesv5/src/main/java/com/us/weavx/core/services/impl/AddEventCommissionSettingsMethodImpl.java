package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.EventCommissionSettings;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;

@Component
public class AddEventCommissionSettingsMethodImpl implements ServiceMethod {
	@Autowired
	private TransactionTxServices txServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			HashMap<String, Object> eventCommissionSettingsParam = (HashMap<String, Object>) params.get("event_commission_settings");
			EventCommissionSettings eventCommissionSettings = new EventCommissionSettings();
			eventCommissionSettings.setCustomerId(customerId);
			eventCommissionSettings.setApplicationid(applicationId);
			eventCommissionSettings.setCommissionTypeId(((Number) eventCommissionSettingsParam.get("commission_type_id")).intValue());
			eventCommissionSettings.setCommissionPayerId(((Number) eventCommissionSettingsParam.get("commission_payer_id")).intValue());			
			eventCommissionSettings.setMinimumCommission(((Number) eventCommissionSettingsParam.get("minimum_commission")).doubleValue());
			eventCommissionSettings.setMaximumCommission(((Number) eventCommissionSettingsParam.get("maximum_commission")).doubleValue());
			eventCommissionSettings.setCommissionValue(((Number) eventCommissionSettingsParam.get("commission_value")).doubleValue());
			eventCommissionSettings.setFreeCommissionValue(((Number) eventCommissionSettingsParam.get("free_commission_value")).doubleValue());
			eventCommissionSettings = txServices.addEventCommisssionSettings(eventCommissionSettings);
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("eventCommissionSettings", eventCommissionSettings);
			resp.setResult(result);
			return resp;
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());	
			return resp;
		}
	}
}
