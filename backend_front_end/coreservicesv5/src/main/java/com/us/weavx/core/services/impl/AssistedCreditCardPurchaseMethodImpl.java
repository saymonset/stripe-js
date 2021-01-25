package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.OTPKey;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;

@Component
public class AssistedCreditCardPurchaseMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userTxServices; 
	@Autowired
	private AuthenticateUserFromAdminMethodImpl authenticateUserFromAdminMethod;
	@Autowired
	private AuthenticateCustomerUserByOTPMethodImpl authenticateCustomerUserByOTPMethod;
	@Autowired
	private CreditCardPurchaseMethodImpl creditCardPurchaseMethod;
	@Autowired
	private RegisterUserMethodImpl registerUserMethod;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
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
			String comments = (String) params.get("comments");
			comments=(comments==null)?"":comments;
			Number operatorIdParam = (Number) params.get("operator_id");
			Long operatorId = (operatorIdParam==null)?null:operatorIdParam.longValue();
			HashMap<String, Object> paymentGwInfoParam = (HashMap<String, Object>) params.get("payment_gw_info");
			//Verificacion si el usuario existe
			Long customerUserId = userTxServices.findCustomerUserIdByEmail(email, customerId);
			String userAccessToken = null;
			String ipAddress = "10.0.0.1";
			String userAgent = "COREAdminExternalPurchase";
			String coupon = (String) params.get("coupon");
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
				} else {
					return responseNewUser;
				}
			} else {
				//EL usuario ya existe por lo que hay que autenticarlo para obtener un token
				//Cerrar todas las sesiones del usuario
				userTxServices.closeAllCustomerUserSessions(customerUserId);
				Request newOTPRequest = new Request();
				newOTPRequest.setAccessToken(aInfo.getAccessToken().getToken());
				HashMap<String, Object> newOTPParams = new HashMap<>();
				newOTPParams.put("email", email);
				newOTPParams.put("langId", langId);
				newOTPParams.put("accessInfo", aInfo);
				newOTPRequest.setParameters(newOTPParams);
				Response newOTPResponse = authenticateUserFromAdminMethod.executeMethod(newOTPRequest);
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
					Response validateOTPResponse = authenticateCustomerUserByOTPMethod.executeMethod(validateOTPRequest);
					if (validateOTPResponse.getReturnCode()==ServiceReturnMessages.SUCCESS_CODE) {
						userAccessToken = ((UserAccessToken) validateOTPResponse.getResult().get("user_access_token")).getToken();
					} else {
						return validateOTPResponse;
					}
				} else {
					return newOTPResponse;
				}
			}
			//Ahora se procede a llamar al metodo de creditCardPurchase para registrar la venta
			Request creditCardPurchase = new Request();
			creditCardPurchase.setAccessToken(aInfo.getAccessToken().getToken());
			HashMap<String, Object> creditCardPurchaseParams = new HashMap<>();
			creditCardPurchaseParams.put("tx_details", txDetails);
			transactionUserDataParam.put("userAccessToken", userAccessToken);
			creditCardPurchaseParams.put("tx_user_data", transactionUserDataParam);
			creditCardPurchaseParams.put("lang_id", langId);
			creditCardPurchaseParams.put("transaction_source_key", sourceKey);
			creditCardPurchaseParams.put("transaction_campaing_key", campaingKey);
			creditCardPurchaseParams.put("transaction_medium_key", mediumKey);
			creditCardPurchaseParams.put("accessInfo", aInfo);
			creditCardPurchaseParams.put("payment_gw_info", paymentGwInfoParam);
			creditCardPurchaseParams.put("userAgent", userAgent);
			creditCardPurchaseParams.put("ipAddress", ipAddress);
			creditCardPurchaseParams.put("coupon", coupon);
			creditCardPurchaseParams.put("comments", comments);
			creditCardPurchaseParams.put("operator_id", operatorId);	
			creditCardPurchase.setParameters(creditCardPurchaseParams);
			Response ccPurchaseResponse = creditCardPurchaseMethod.executeMethod(creditCardPurchase);
			return ccPurchaseResponse;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" e: "+e.getMessage());
			return res;
		}
	}

	
	

}
