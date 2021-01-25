package com.us.weavx.core.services.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.exception.NoPaymentProfileForPaymentGatewayException;
import com.us.weavx.core.exception.PaymentGatewayException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.CustomerUserCreditCard;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class ListCustomerUserCreditCardsByPaymentGatewayMethodImpl implements ServiceMethod {
	@Autowired
	private TransactionTxServices txServices;
	@Autowired
	private UserTxServices userServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			//Se inicializa el container de spring
			String userAccessToken = (String) params.get("userAccessToken");
			int paymentGwId = (Integer) params.get("paymentGatewayId");
			//Se valida el userAccessToken
			UserAccessToken uAT = userServices.validateUserAccessToken(userAccessToken);
			List<CustomerUserCreditCard> cards = txServices.listCustomerUserCardsByPaymentGateway(uAT.getCustomerUserId(), customerId, paymentGwId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerUserCreditCards", cards);
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
			return res;
		} catch (PaymentGatewayException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.PAYMENT_GATEWAY_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.PAYMENT_GATEWAY_ERROR+e.getMessage());
			return res;
		} catch (NoPaymentProfileForPaymentGatewayException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.NO_PAYMENT_PROFILE_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.NO_PAYMENT_PROFILE_ERROR+e.getMessage());
			return res;			
		}  
	}

	
	

}
