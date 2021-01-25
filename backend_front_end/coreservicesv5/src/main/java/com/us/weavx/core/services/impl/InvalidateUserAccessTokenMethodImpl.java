package com.us.weavx.core.services.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class InvalidateUserAccessTokenMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			List<String> userAccessToken = (List<String>) params.get("invalidUserAccessTokens");
			if (userAccessToken == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.USER_ACCESS_TOKEN_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.USER_ACCESS_TOKEN_REQUIRED);
				return res;
			}
			int invalidatedTokens = userServices.invalidateUserAccessToken(userAccessToken, aInfo.getApplicationId());
			if (invalidatedTokens > 0) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				result.put("invalidatedUserAccessTokensCount", invalidatedTokens);
				res.setResult(result);
				return res;				
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
				res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+"Can't invalidate userAccessTokens.");
				return res;				
			}
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		}
	}

	
	

}
