package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.EventFundSettings;
import com.us.weavx.core.model.Fund;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.RestrictedEventAttendeeValidationResult;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;
@Component
public class RemoveEventFundSettingsMethodImpl implements ServiceMethod {
	@Autowired
	private EventRestrictionServicesTx eventRestrictionServices;
	@Autowired
	private ConfigurationTxServices configurationTx;

	public Response executeMethod(Request request) {
		List<RestrictedEventAttendeeValidationResult> resultList = new ArrayList<>();
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			HashMap<String, Object> eventFundSettingsParam = (HashMap<String, Object>) params.get("event_fund_settings");
			EventFundSettings eventFundSettings = new EventFundSettings();
			eventFundSettings.setCustomerId(customerId);
			eventFundSettings.setApplicationId(applicationId);
			eventFundSettings.setId(((Number) eventFundSettingsParam.get("id")).longValue());
			eventFundSettings.setFundId(((Number) eventFundSettingsParam.get("fund_id")).longValue());
			Fund f = configurationTx.findFundById(new Long(eventFundSettings.getFundId()).intValue());
			if (f == null) {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.UNKNOWN_OBJECT_CODE);
				resp.setReturnMessage(ServiceReturnMessages.UNKNOWN_OBJECT+" fund_id not found.");
				return resp;				
			}
			if (f.getCustomerId() != customerId) {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.NOT_OWNER_CODE);
				resp.setReturnMessage(ServiceReturnMessages.NOT_OWNER+" fund_id does not belong to the client.");
				return resp;
			}
			eventRestrictionServices.removeEventFundSettings(eventFundSettings.getId());
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("eventFundSettings", eventFundSettings);
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
