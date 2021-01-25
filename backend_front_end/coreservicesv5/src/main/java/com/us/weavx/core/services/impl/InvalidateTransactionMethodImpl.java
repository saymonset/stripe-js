package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
@Component
public class InvalidateTransactionMethodImpl implements ServiceMethod {
	@Autowired
	private TransactionTxServices txServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			//Se inicializa el container de spring
			Long transactionId = ((Number) params.get("transactionId")).longValue();
			Transaction t = txServices.findTransactionById(transactionId);
			if (t.getCustomerId() == aInfo.getCustomerId() && t.getApplicationId() == aInfo.getApplicationId()) {
				if (t.getTransactionStatus() != Transaction.SUCCESS_TX) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.NOT_SUCCESS_TRANSACTIONS_IN_APP_CODE);
					res.setReturnMessage("Not approved transaction.");
					return res;									
				} else {
					t.setTransactionStatus(Transaction.ABORTED);
					txServices.updateTransactionStatus(t);
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
					res.setReturnMessage(ServiceReturnMessages.SUCCESS);
					return res;
				}
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.NOT_OWNER_CODE);
				res.setReturnMessage(ServiceReturnMessages.NOT_OWNER);
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
