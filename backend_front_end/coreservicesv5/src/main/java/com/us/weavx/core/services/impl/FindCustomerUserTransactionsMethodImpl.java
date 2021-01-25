package com.us.weavx.core.services.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.TransactionDetailInfo;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;

@Component	
public class FindCustomerUserTransactionsMethodImpl implements ServiceMethod {

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
			Timestamp from = new Timestamp(((Number) params.get("from")).longValue());
			Timestamp to = new Timestamp(((Number) params.get("to")).longValue());
			List<TransactionDetailInfo> transactions = txServices.findCustomerUserTransactions(uAT.getCustomerUserId(), aInfo.getApplicationId(), from, to);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("transactions", transactions);
			res.setResult(result);
			return res;
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
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
