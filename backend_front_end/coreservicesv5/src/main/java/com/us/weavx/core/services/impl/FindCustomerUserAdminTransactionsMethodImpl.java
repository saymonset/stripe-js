package com.us.weavx.core.services.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.TransactionDetailInfo;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
@Component
public class FindCustomerUserAdminTransactionsMethodImpl implements ServiceMethod {

	@Autowired
	private TransactionTxServices txServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			//Se inicializa el container de spring
			Long customerUserId = ((Number) params.get("customerUserId")).longValue();
			List<TransactionDetailInfo> transactions = txServices.findCustomerUserTransactionsAll(customerUserId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("transactions", transactions);
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
