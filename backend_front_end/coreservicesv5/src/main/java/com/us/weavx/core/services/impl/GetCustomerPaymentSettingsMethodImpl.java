package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.CustomerDefaultPaymentGateway;
import com.us.weavx.core.model.CustomerPaymentGateway;
import com.us.weavx.core.model.CustomerSupportedPaymentType;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
@Component
public class GetCustomerPaymentSettingsMethodImpl implements ServiceMethod {
	@Autowired
	private ConfigurationTxServices services;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			List<CustomerSupportedPaymentType> custSupportedPT = services.getCustomerSupportedPaymentTypes(customerId);
			List<CustomerDefaultPaymentGateway> custDefaultPGw = services.getCustomerDefaultPaymentGateways(customerId);
			List<CustomerPaymentGateway> custPGw = services.getCustomerPaymentGatewayInfo(customerId);
			ArrayList<CustomerPaymentGateway> cleanCustPGw = new ArrayList<>();
			for (CustomerPaymentGateway tmp: custPGw) {
				tmp.cleanPrivateInfo();
				cleanCustPGw.add(tmp);
			}
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerSupportedPaymentTypes", custSupportedPT);
			result.put("customerDefaultPaymentGateways",custDefaultPGw);
			result.put("customerPaymentGatewayInfo", cleanCustPGw);
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
