package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class ResendPurchaseReceiptAdminMethodImpl implements ServiceMethod {
	@Autowired
	private TransactionTxServices txServices;
	@Autowired
	private UserTxServices userServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			Long customerUserId = ((Number) params.get("customerUserId")).longValue();
			Long transactionId = ((Number) params.get("transactionId")).longValue();
			String adminEmail = (String) params.get("adminEmail");
			userServices.closeAllCustomerUserSessions(customerUserId);
			txServices.resendPurchaseReceipt(transactionId, customerUserId, aInfo.getCustomerId(), aInfo.getApplicationId(), 1, adminEmail);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		} 
	}

	
	

}
