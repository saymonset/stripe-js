package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.CustomerProperty;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;

@Component
public class CreateCustomerPropertyMethodImpl implements ServiceMethod {

	@Autowired
	private ConfigurationTxServices services;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			HashMap<String, Object> customerPropertyParam = (HashMap<String, Object>) params.get("customerProperty");
			long customerIdParam = (Integer) customerPropertyParam.get("customerId");
			long customerId = aInfo.getCustomerId();
			if (customerIdParam != customerId) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.WRONG_CUSTOMER_ID_ERROR_CODE);
				res.setReturnMessage(ServiceReturnMessages.WRONG_CUSTOMER_ID_ERROR);
				return res;
			} 
			int propertyId = (Integer) customerPropertyParam.get("propertyId");
			int langId = (Integer) customerPropertyParam.get("langId");
			String propertyValue = (String) customerPropertyParam.get("propertyValue");
			CustomerProperty property = new CustomerProperty(customerId,propertyId,langId,propertyValue);
			//Se inicializa el container de spring
			property = services.createCustomerProperty(property);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerProperty", property);
			res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
			return res;
		}
	}

	
	

}
