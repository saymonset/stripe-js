package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.CustomerSignatureGateway;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.SignatureServicesTx;
@Component
public class FindCustomerSignatureGatewayMethodImpl implements ServiceMethod {

	@Autowired
	private SignatureServicesTx signatureServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			int id = ((Number) params.get("signature_gateway_id")).intValue(); 
			CustomerSignatureGateway signatureGateway = signatureServices.findCustomerSignatureGatewayByCustomerIdAndGatewayId(aInfo.getCustomerId(), id);
			if (signatureGateway != null) {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				result.put("customerSignatureGateway", signatureGateway);
				resp.setResult(result);
				return resp;
			} else {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.UNKNOWN_OBJECT_CODE);
				resp.setReturnMessage(ServiceReturnMessages.UNKNOWN_OBJECT);
				return resp;
			}
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());
			return resp;
		}
	}
}
