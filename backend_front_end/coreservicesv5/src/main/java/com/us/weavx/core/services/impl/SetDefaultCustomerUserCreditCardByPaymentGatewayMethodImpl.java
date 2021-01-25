package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.exception.NoPaymentProfileForPaymentGatewayException;
import com.us.weavx.core.exception.PaymentGatewayException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class SetDefaultCustomerUserCreditCardByPaymentGatewayMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;
	@Autowired
	private TransactionTxServices txServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			//Se inicializa el container de spring
			String userAccessToken = (String) params.get("userAccessToken");
			int paymentGwId = (Integer) params.get("paymentGatewayId");
			String cardId = (String) params.get("cardId");
			//Se valida el userAccessToken
			UserAccessToken uAT = userServices.validateUserAccessToken(userAccessToken);
			HashMap<String, Object> parameters = new HashMap<>();
			parameters.put("source", cardId);
			txServices.setDefaultCustomerUserCreditCardInPaymentGateway(uAT.getCustomerUserId(), customerId, paymentGwId, parameters);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
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
			return res;
		} catch (PaymentGatewayException | NoPaymentProfileForPaymentGatewayException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.PAYMENT_GATEWAY_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.PAYMENT_GATEWAY_ERROR+e.getMessage());
			return res;
		} 
	}

	
	

}
