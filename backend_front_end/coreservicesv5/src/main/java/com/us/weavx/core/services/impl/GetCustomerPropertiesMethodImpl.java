package com.us.weavx.core.services.impl;

import java.util.HashMap;
import java.util.List;

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
public class GetCustomerPropertiesMethodImpl implements ServiceMethod {
	@Autowired
	private ConfigurationTxServices configurationTxServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			//Inicializacion context
			List<CustomerProperty> resultTmp = configurationTxServices.getCustomerProperties(customerId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerProperties", resultTmp);
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
