package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class ResendPurchaseReceiptMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;
	@Autowired
	private TransactionTxServices txServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			//Se inicializa el container de spring
			String userAccessToken = (String) params.get("userAccessToken");
			UserAccessToken uAT = userServices.validateUserAccessToken(userAccessToken);
			Long transactionId = ((Number) params.get("transactionId")).longValue();
			Integer langId = ((Number) params.get("langId")).intValue(); 
			txServices.resendPurchaseReceipt(transactionId, uAT.getCustomerUserId(), aInfo.getCustomerId(), aInfo.getApplicationId(), langId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			return res;
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		} catch (InvalidUserAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN_CODE);
			res.setReturnMessage(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN);
			return res;
		} catch (ExpiredUserAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.EXPIRED_USER_ACCESS_TOKEN_CODE);
			res.setReturnMessage(ServiceReturnMessages.EXPIRED_USER_ACCESS_TOKEN);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerUserData", e.getTokenOwner().getCustUser());
			result.put("email", e.getTokenOwner().getEmail());
			res.setResult(result);
			return res;
		} 
	}

	
	

}
