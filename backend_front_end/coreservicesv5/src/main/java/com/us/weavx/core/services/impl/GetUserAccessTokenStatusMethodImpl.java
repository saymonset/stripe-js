package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;

@Component
public class GetUserAccessTokenStatusMethodImpl implements ServiceMethod {

	@Autowired
	private UserTxServices service;
	
	@Override
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			String uat = params.get("userAccessToken") != null ? params.get("userAccessToken").toString() : null;
			
			if(uat != null) {
				UserAccessToken userAccessToken = service.getUserTokenInfo(uat);
				if(userAccessToken == null) {
					throw new Exception("Invalid User Access Token");
				}
				HashMap<String, Object> result = new HashMap<>();
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				res.setReturnDate(LocalDateTime.now().toString());
				result.put("userAccessToken", userAccessToken);
				res.setResult(result);
				return res;
			} else {
				Response resp = new Response();
				resp.setReturnCode(ServiceReturnMessages.UNKNOWN_OBJECT_CODE);
				resp.setReturnMessage(ServiceReturnMessages.UNKNOWN_OBJECT);
				return resp;
			}
			
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e.getMessage());
			return res;
		}
	}

}
