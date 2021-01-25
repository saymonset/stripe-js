package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
@Component
public class FindLastCustomerUserTransactionMethodImpl implements ServiceMethod {
	
	@Autowired
	TransactionTxServices transactionServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			String userEmail = (String) params.get("userEmail");
			CustomerUser cu = transactionServices.getLastApprovedUserTransactionByEmail(userEmail);
			if (cu != null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				result.put("user", cu);
				res.setResult(result);
				return res;
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER_CODE);
				res.setReturnMessage("No previous transactions found for email: "+userEmail);
				return res;
			}
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e.getMessage());
			return res;
		}
	}

	
	

}
