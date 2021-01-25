package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.UnknownCustomerMediumException;
import com.us.weavx.core.exception.UnknownCustomerSourceException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.PaymentResult;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.TransactionCampaing;
import com.us.weavx.core.model.TransactionDetail;
import com.us.weavx.core.model.TransactionMedium;
import com.us.weavx.core.model.TransactionSource;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.model.User;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.util.ApplicationParametersManager;
import com.us.weavx.core.util.TransactionCampaingManager;
import com.us.weavx.core.util.TransactionMediumManager;
import com.us.weavx.core.util.TransactionSourceManager;
import com.us.weavx.core.util.UnknownCustomerCampaingException;
@Component
public class FreePurchaseMethodImpl implements ServiceMethod {

	@Autowired
	private UserTxServices userTxServices;
	@Autowired
	private TransactionTxServices transactionServices;
	@Autowired
	private ApplicationParametersManager appParamManager;
	@Autowired
	private TransactionSourceManager transactionSourceManager;
	@Autowired
	private TransactionCampaingManager transactionCampaingManager;
	@Autowired
	private TransactionMediumManager transactionMediumManager;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			String userAgent = (String) params.get("userAgent");
			userAgent = (userAgent==null)?"INTERNAL":userAgent;
			String ipAddress = (String) params.get("ipAddress");
			ipAddress = (ipAddress==null)?"N/A":ipAddress;
			String couponSerial = (String) params.get("coupon");
			//Armado de TransactionDetails
			ArrayList<HashMap<String, Object>> txDetails = (ArrayList<HashMap<String, Object>>) params.get("tx_details");
			Iterator<HashMap<String, Object>> iter = txDetails.iterator();
			ArrayList<TransactionDetail> transactionDetails = new ArrayList<>();
			double totalAmount = 0;
			while (iter.hasNext()) {
				HashMap<String, Object> detailTmp = iter.next();
				TransactionDetail tDetailTmp = new TransactionDetail();
				tDetailTmp.setFundId((Integer) detailTmp.get("fundId"));
				tDetailTmp.setAmount(((Number) detailTmp.get("amount")).doubleValue());
				transactionDetails.add(tDetailTmp);
				totalAmount += tDetailTmp.getAmount();
			}
			//Armado de TransactionUserData
			HashMap<String, Object> transactionUserDataParam = (HashMap<String, Object>) params.get("tx_user_data");
			String name = (String) transactionUserDataParam.get("name");
			String lastname = (String) transactionUserDataParam.get("lastname");
			Integer country = (Integer) transactionUserDataParam.get("country");
			Integer state = (Integer) transactionUserDataParam.get("state");
			String stateText = (String) transactionUserDataParam.get("stateText");
			Integer city = (Integer) transactionUserDataParam.get("city");
			String cityText = (String) transactionUserDataParam.get("cityText");
			String address = (String) transactionUserDataParam.get("address");
			String postcode = (String) transactionUserDataParam.get("postcode");
			String email = (String) transactionUserDataParam.get("email");
			String userToken = (String) transactionUserDataParam.get("userAccessToken");
			TransactionUserData txUserData = new TransactionUserData();
			txUserData.setName(name);
			txUserData.setLastname(lastname);
			txUserData.setCountry(country);
			txUserData.setState(state);
			txUserData.setStateText(stateText);
			txUserData.setCity(city);
			txUserData.setCityText(cityText);
			txUserData.setAddress(address);
			txUserData.setPostcode(postcode);
			txUserData.setEmail(email);
 			UserAccessToken uAT = null;
 			Long customerUserId = null;
 			if (userToken != null) {
	 			try {
					uAT = userTxServices.validateUserAccessToken(userToken);
					customerUserId = uAT.getCustomerUserId();
				} catch (Exception e1) {
					Response res = new Response();
					HashMap<String, Object> result = new HashMap<>();
					res.setReturnCode(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN_CODE);
					res.setReturnMessage(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN);
					res.setResult(result);
					return res;
				}
 			}
			txUserData.setCustomerUserId(customerUserId);
			//Se arman los parametros a enviar al gW de pago segun los parametros recibidos
			//Se arma el objeto transaccion de acuerdo a la info recibida
			Transaction transaction = new Transaction();
			transaction.setCustomerId(customerId);
			transaction.setApplicationId(applicationId);
			transaction.setAmount(totalAmount);
			//Se obtiene el language
			Integer langId = (Integer) params.get("lang_id");
			langId = (langId==null)?1:langId;
			transaction.setLangId(langId);
			//Se verifica el source, el campaing, el_medium
			String sourceKey = (String) params.get("transaction_source_key");
			Integer sourceId = TransactionSource.DIRECT_TRAFFIC;
			if (sourceKey != null) {
				TransactionSource tSource;
				try {
					tSource = transactionSourceManager.findCustomerSource(customerId, sourceKey);
				} catch (UnknownCustomerSourceException e1) {
					tSource = null;
				}
				if (tSource != null) {
					sourceId = tSource.getId();
				} else {
					//Se crea el nuevo sourceId
					tSource = new TransactionSource();
					tSource.setCustomerId(customerId);
					tSource.setKey(sourceKey);
					tSource.setName(sourceKey);
					
					try {
						tSource = transactionSourceManager.addCustomerSource(tSource);
						sourceId = tSource.getId();
					} catch (Exception e) {
						Response res = new Response();
						res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
						res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+"->Can't create a new Transaction Source: "+e);
						return res;
					}
				}
			}
			transaction.setSourceId(sourceId);
			Long campaingId = TransactionCampaing.NO_CAMPAING;
			String campaingKey = (String) params.get("transaction_campaing_key");
			if (campaingKey != null) {
				TransactionCampaing tCampaing;
				try {
					tCampaing = transactionCampaingManager.findCustomerCampaing(customerId, campaingKey);
				} catch (UnknownCustomerCampaingException e1) {
					tCampaing = null;
				}
				if (tCampaing != null) {
					campaingId = tCampaing.getId();
				} else {
					//Se crea el nuevo sourceId
					tCampaing = new TransactionCampaing();
					tCampaing.setCustomerId(customerId);
					tCampaing.setKey(campaingKey);
					tCampaing.setName(campaingKey);
					try {
						tCampaing = transactionCampaingManager.addCustomerCampaing(tCampaing);
						campaingId = tCampaing.getId();
					} catch (Exception e) {
						Response res = new Response();
						res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
						res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+"->Can't create a new Transaction Campaing: "+e);
						return res;
					}
				}
			}
			transaction.setCampaingId(campaingId);
			Long mediumId = TransactionMedium.DEFAULT_MEDIUM;
			String mediumKey = (String) params.get("transaction_medium_key");
			if (mediumKey != null) {
				TransactionMedium tMedium;
				try {
					tMedium = transactionMediumManager.findCustomerMedium(customerId, mediumKey);
				} catch (UnknownCustomerMediumException e1) {
					tMedium = null;
				}
				if (tMedium != null) {
					mediumId = tMedium.getId();
				} else {
					//Se crea el nuevo sourceId
					tMedium = new TransactionMedium();
					tMedium.setCustomer_id(customerId);
					tMedium.setKey(mediumKey);
					tMedium.setName(mediumKey);
					try {
						tMedium = transactionMediumManager.addCustomerMedium(tMedium);
						mediumId = tMedium.getId();
					} catch (Exception e) {
						Response res = new Response();
						res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
						res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+"->Can't create a new Transaction Medium: "+e);
						return res;
					}
				}
			}
			transaction.setMediumId(mediumId);
			String comments = (String) params.get("comments");
			comments=(comments==null)?"":comments;
			transaction.setComments(comments);
			Number operatorIdParam = (Number) params.get("operator_id");
			Long operatorId = (operatorIdParam==null)?null:operatorIdParam.longValue();
			transaction.setOperatorId(operatorId);
			if (operatorId != null) {
				//Se busca el email del operador				
				User u = userTxServices.findUserByCustomerUserId(operatorId);
				if (u != null) {
					transaction.setOperatorEmail(u.getEmail());
				}
			}
			PaymentResult paymentResult = transactionServices.proccessFreeTransaction(transaction, transactionDetails, txUserData, userToken, langId, couponSerial, userAgent, ipAddress);
			Response res = new Response();
			HashMap<String, Object> result = new HashMap<>();
			switch (paymentResult.getResult()) {	
			case PaymentResult.APPROVED:
				res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				result.put("paymentResult", paymentResult);
				res.setResult(result);
				return res;
			case PaymentResult.DENIED:
				res = new Response();
				res.setReturnCode(ServiceReturnMessages.DENIED_TRANSACTION_CODE);
				res.setReturnMessage(ServiceReturnMessages.DENIED_TRANSACTION+paymentResult.getResultMessage());
				result.put("paymentResult", paymentResult);
				res.setResult(result);
				return res;
			case PaymentResult.ERROR:
				res = new Response();
				res.setReturnCode(ServiceReturnMessages.TRANSACTION_ERROR_CODE);
				res.setReturnMessage(ServiceReturnMessages.TRANSACTION_ERROR+paymentResult.getResultMessage());
				result.put("paymentResult", paymentResult);
				res.setResult(result);
				return res;		
			case PaymentResult.ABORTED_TRANSACTION:
				res = new Response();
				res.setReturnCode(ServiceReturnMessages.TRANSACTION_ABORTED_CODE);
				res.setReturnMessage(ServiceReturnMessages.TRANSACTION_ABORTED+paymentResult.getResultMessage());
				result.put("paymentResult", paymentResult);
				res.setResult(result);
				return res;					
			default:
				res = new Response();
				res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
				res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+paymentResult.getResultMessage());
				result.put("paymentResult", paymentResult);
				res.setResult(result);
				return res;
			}
			
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" e: "+e.getMessage());
			return res;
		}
	}

	
	

}
