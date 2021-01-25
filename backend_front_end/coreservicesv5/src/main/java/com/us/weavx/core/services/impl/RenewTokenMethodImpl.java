package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ObjectDoesNotExistsException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
@Component
public class RenewTokenMethodImpl implements ServiceMethod {

	private final long ALLOWED_CUSTOMER = 0;
	private final long ALLOWED_APPLICATION = 205;
	
	@Autowired
	private ConfigurationTxServices configurationTxServices;
	
	public Response executeMethod(Request request) {
		Response res = new Response();
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			if (aInfo.getCustomerId() !=  ALLOWED_CUSTOMER || aInfo.getApplicationId() != ALLOWED_APPLICATION) {
				res.setReturnCode(ServiceReturnMessages.ACCESS_DENIED_CODE);
				res.setReturnMessage("Access token not authorized to perform this operation.");
				HashMap<String, Object> result = new HashMap<>();
				res.setResult(result);
				return res;
			}
			String accessTokenToBeRenewed = (String) params.get("access_token");
			int minutesToExtend = ((Number) params.get("minutes")).intValue();
			configurationTxServices.renewAccessToken(accessTokenToBeRenewed, minutesToExtend);
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			res.setResult(result);
		} catch (RuntimeException e) {
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
		} catch (ObjectDoesNotExistsException e) {
			res.setReturnCode(ServiceReturnMessages.UNKNOWN_OBJECT_CODE);
			res.setReturnMessage("Token to be renewed does not exists or is not in EXPIRED state: "+e);
		} 
		return res;
	}

	
	

}
