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
import com.us.weavx.core.model.ExternalPaymentData;
import com.us.weavx.core.model.Fund;
import com.us.weavx.core.model.OTPKey;
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
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.util.ApplicationParametersManager;
import com.us.weavx.core.util.TransactionCampaingManager;
import com.us.weavx.core.util.TransactionMediumManager;
import com.us.weavx.core.util.TransactionSourceManager;
import com.us.weavx.core.util.UnknownCustomerCampaingException;

@Component
public class ExternalPurchaseMethodImpl implements ServiceMethod {
	
	@Autowired
	private UserTxServices userTxServices;
	
	@Autowired
	private TransactionTxServices transactionServices;

	@Autowired
	private ConfigurationTxServices configurationServices;
	
	@Autowired
	private ApplicationParametersManager parameterManager;
	
	@Autowired
	private TransactionSourceManager transactionSourceManager;

	@Autowired
	private TransactionCampaingManager transactionCampaingManager;
	
	@Autowired
	private TransactionMediumManager transactionMediumManager;

	@Autowired
	private RegisterUserMethodImpl registerUserMethod;

	@Autowired
	private AuthenticateUserFromAdminMethodImpl generateOTPmethod;
	
	@Autowired
	private AuthenticateCustomerUserByOTPMethodImpl authenticateByOTPMethod;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			ArrayList<HashMap<String, Object>> txDetails = (ArrayList<HashMap<String, Object>>) params.get("tx_details");
			HashMap<String, Object> transactionUserDataParam = (HashMap<String, Object>) params.get("tx_user_data");
			String name = (String) transactionUserDataParam.get("name");
			String lastname = (String) transactionUserDataParam.get("lastname");
			String email = (String) transactionUserDataParam.get("email");
			String phoneNumber = (String) transactionUserDataParam.get("phoneNumber");
 			Integer langId = (Integer) params.get("lang_id");
			langId = (langId==null)?1:langId;
			String sourceKey = (String) params.get("transaction_source_key");
			String campaingKey = (String) params.get("transaction_campaing_key");
			String mediumKey = (String) params.get("transaction_medium_key");
			HashMap<String, Object> extPaymentDataParam = (HashMap<String, Object>) params.get("external_payment_data");
			//Verificacion si el usuario existe
			
			Long customerUserId = userTxServices.findCustomerUserIdByEmail(email, customerId);
			String userAccessToken = null;
			String ipAddress = "10.0.0.1";
			String userAgent = "COREAdminExternalPurchase";
			String couponSerial = (String) params.get("coupon");
			if (customerUserId == null) {
				//Es un usuario nuevo por lo que hay que crearlo llamando al servicio para registrar un usuario
				Request newUserRequest = new Request();
				newUserRequest.setAccessToken(aInfo.getAccessToken().getToken());
				HashMap<String, Object> parameters = new HashMap<>();
				parameters.put("ipAddress", ipAddress);
				parameters.put("userAgent", userAgent);
				parameters.put("accessInfo", aInfo);
				HashMap<String, Object> user = new HashMap<>();
				user.put("email", email);
				user.put("firstName", name);
				user.put("lastName", lastname);
				if (phoneNumber != null) {
					user.put("phoneNumber", phoneNumber);
				}
				parameters.put("user", user);
				newUserRequest.setParameters(parameters);
				Response responseNewUser = registerUserMethod.executeMethod(newUserRequest);
				if (responseNewUser.getReturnCode() == ServiceReturnMessages.SUCCESS_CODE) {
					userAccessToken = ((UserAccessToken) responseNewUser.getResult().get("userAccessToken")).getToken();
					customerUserId = ((UserAccessToken) responseNewUser.getResult().get("userAccessToken")).getCustomerUserId();
				} else {
					return responseNewUser;
				}
			} else {
				//EL usuario ya existe por lo que hay que autenticarlo para obtener un token
				//Se cierran todas las sesiones del usuario
				userTxServices.closeAllCustomerUserSessions(customerUserId);
				Request newOTPRequest = new Request();
				newOTPRequest.setAccessToken(aInfo.getAccessToken().getToken());
				HashMap<String, Object> newOTPParams = new HashMap<>();
				newOTPParams.put("email", email);
				newOTPParams.put("langId", langId);
				newOTPParams.put("accessInfo", aInfo);
				newOTPRequest.setParameters(newOTPParams);
				Response newOTPResponse = generateOTPmethod.executeMethod(newOTPRequest);
				if (newOTPResponse.getReturnCode()==ServiceReturnMessages.SUCCESS_CODE) {
					OTPKey otpKey = (OTPKey) newOTPResponse.getResult().get("otp_key");
					//Ahora se hace el llamado de validateOTP para obtener el token
					Request validateOTPRequest = new Request();
					validateOTPRequest.setAccessToken(aInfo.getAccessToken().getToken());
					HashMap<String, Object> validateOTPParams = new HashMap<>();
					validateOTPParams.put("otpCode", otpKey.getCode());
					validateOTPParams.put("otpId", otpKey.getId());
					validateOTPParams.put("ipAddress", ipAddress);
					validateOTPParams.put("userAgent", userAgent);
					validateOTPParams.put("accessInfo", aInfo);
					validateOTPRequest.setParameters(validateOTPParams);
					Response validateOTPResponse = authenticateByOTPMethod.executeMethod(validateOTPRequest);
					if (validateOTPResponse.getReturnCode()==ServiceReturnMessages.SUCCESS_CODE) {
						userAccessToken = ((UserAccessToken) validateOTPResponse.getResult().get("user_access_token")).getToken();
					} else {
						return validateOTPResponse;
					}
				} else {
					return newOTPResponse;
				}
			}
			//Armado de TransactionDetails
			Iterator<HashMap<String, Object>> iter = txDetails.iterator();
			ArrayList<TransactionDetail> transactionDetails = new ArrayList<>();
			double totalAmount = 0;
			while (iter.hasNext()) {
				HashMap<String, Object> detailTmp = iter.next();
				TransactionDetail tDetailTmp = new TransactionDetail();
				tDetailTmp.setFundId((Integer) detailTmp.get("fundId"));
				Fund f = configurationServices.findFundById((Integer) detailTmp.get("fundId"));
				if (f.getCustomerId() != customerId) {
					throw new Exception("Invalid event for this organizer, this may be due to having several administration tabs open at this time, it is recommended to close all sessions and keep a single tab open.");
				} else {
					String fundAmountStr = parameterManager.getApplicationParameter(applicationId, Integer.valueOf(f.getId()).toString()); 
					double amountParam =  ((Number) detailTmp.get("amount")).doubleValue();
					if (amountParam > 0) {
						tDetailTmp.setAmount((fundAmountStr != null)?Double.valueOf(fundAmountStr):amountParam);
					} else {
						//Caso complimentary
						tDetailTmp.setAmount(0);
					}
					
				}
				transactionDetails.add(tDetailTmp);
				totalAmount += tDetailTmp.getAmount();
			}
			Integer country = (Integer) transactionUserDataParam.get("country");
			Integer state = (Integer) transactionUserDataParam.get("state");
			String stateText = (String) transactionUserDataParam.get("stateText");
			Integer city = (Integer) transactionUserDataParam.get("city");
			String cityText = (String) transactionUserDataParam.get("cityText");
			String address = (String) transactionUserDataParam.get("address");
			String postcode = (String) transactionUserDataParam.get("postcode");
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
			txUserData.setCustomerUserId(customerUserId);
			//Se arman los parametros a enviar al gW de pago segun los parametros recibidos
			//Se arma el objeto transaccion de acuerdo a la info recibida
			//Armado de ExternalPaymentData
			ExternalPaymentData extPaymentData = new ExternalPaymentData();
			extPaymentData.setExternalPaymentDataId((Integer) extPaymentDataParam.get("externalPaymentDataId"));
			extPaymentData.setExternalPaymentDataTxt((String) extPaymentDataParam.get("externalPaymentDataTxt"));
			Transaction transaction = new Transaction();
			transaction.setCustomerId(customerId);
			transaction.setApplicationId(applicationId);
			transaction.setAmount(totalAmount);
			//Se obtiene el language
			langId = (langId==null)?1:langId;
			transaction.setLangId(langId);
			//Se verifica el source, el campaing, el_medium
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
			//Se accede al componente de spring para procesar la transaccion
			PaymentResult paymentResult = transactionServices.proccessExternalTransaction(transaction, transactionDetails, txUserData, userAccessToken, langId, extPaymentData, couponSerial, userAgent, ipAddress);
			Response res = new Response();
			HashMap<String, Object> result = new HashMap<String, Object>();
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
			case PaymentResult.EXTERNAL_PAYMENT_METHOD_NOT_VALID:
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
			HashMap<String, Object> result = new HashMap<>();
			res.setResult(result);
			return res;
		}
	}

	
	

}
