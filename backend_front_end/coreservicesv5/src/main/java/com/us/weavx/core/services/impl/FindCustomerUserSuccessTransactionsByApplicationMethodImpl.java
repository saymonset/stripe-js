package com.us.weavx.core.services.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.AlreadyUsedAccessTokenException;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Asset;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.CustomerSignatureGateway;
import com.us.weavx.core.model.CustomerUserEventContractSign;
import com.us.weavx.core.model.EventAsset;
import com.us.weavx.core.model.EventFundSettings;
import com.us.weavx.core.model.Fund;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.exception.ClosedByUserAccessTokenException;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;
import com.us.weavx.core.services.tx.EventSettingsServicesTx;
import com.us.weavx.core.services.tx.SignatureServicesTx;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.util.ApplicationParametersManager;
import com.us.weavx.core.util.GeneralUtilities;
import com.us.weavx.core.util.SignatureGatewaySelector;
@Component
public class FindCustomerUserSuccessTransactionsByApplicationMethodImpl implements ServiceMethod {

	@Autowired
	private UserTxServices userServices;
	@Autowired
	private EventRestrictionServicesTx eventRestrictionServices;
	@Autowired
	private TransactionTxServices txServices;
	@Autowired
	private SignatureServicesTx signatureServices;
	@Autowired
	private EventSettingsServicesTx eventSettingServices;
	@Autowired
	private ApplicationParametersManager appParamManager;
		
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			//Se inicializa el container de spring
			String userAccessToken = (String) params.get("userAccessToken");
			long eventId =   params.get("eventId") != null ? 0L: ((Number) params.get("eventId")).longValue(); 
			AuthenticatedUserInfo userInfo = userServices.authenticateUserWithAccessToken(userAccessToken, customerId);
			List<Transaction> transactions = txServices.findCustomerUserSuccessTransactionsByApplication(customerId, userInfo.getCustomerUser().getCustUser().getId(), aInfo.getApplicationId());
			if (transactions != null && transactions.size() > 0) {
				for (Transaction t : transactions) {
					List<Fund> funds = txServices.listAllTransactionFundsByTransactionId(t.getId());
					//Se sabe que para eventos por cada transaccion hay un solo fund
					if (funds != null && funds.size() > 0) {
						Fund purchasedFund = funds.get(0);
						EventFundSettings settings;
						if(eventId == 0L) {
							settings = eventRestrictionServices.findEventFundSettingsByCustomerIdAndApplicationId(purchasedFund.getId(), customerId, aInfo.getApplicationId());
						}else {
							settings = eventRestrictionServices.findEventFundSettingsByCustomerIdAndApplicationIdAndEventId(purchasedFund.getId(), customerId, aInfo.getApplicationId(), eventId);
						}
						if (settings != null) {
							if (settings.getSignatureRequired()) {
								//Se valida si requiere firma
								CustomerUserEventContractSign contractSign = signatureServices.findCustomerUserEventContractSignByCustomerUserIdAndAppId(userInfo.getCustomerUser().getCustUser().getId(), aInfo.getApplicationId());
								if (contractSign == null) {
									//Se manda a crear el Url de firma
									CustomerSignatureGateway custSignGw = signatureServices.findCustomerSignatureGatewayByCustomerId(aInfo.getCustomerId());
									switch (custSignGw.getSignatureGatewayId()) {
									case SignatureGatewaySelector.SIGNNOW:
										contractSign = signatureServices.generateSignatureUrl(userInfo.getCustomerUser().getCustUser(), aInfo.getApplicationId(), 1, settings.getSignatureDocumentId(),t.getId());
										break;
									case SignatureGatewaySelector.HARVESTFUL:
										contractSign = signatureServices.generateUserContractURL(userInfo.getCustomerUser().getCustUser(), aInfo.getApplicationId(), 1, settings.getSignatureDocumentId(),t.getId());
										break;
									default:
										throw new RuntimeException("Unknown Signature Gateway Id: "+custSignGw.getSignatureGatewayId());
									}
								}
								if (contractSign == null || !contractSign.isSignatureStatus()) {
									Response res = new Response();
									res.setReturnCode(ServiceReturnMessages.SIGNATURE_REQUIRED_CODE);
									res.setReturnMessage(ServiceReturnMessages.SIGNATURE_REQUIRED);
									HashMap<String, Object> result = new HashMap<>();
									if (contractSign != null) {
										result.put("customerUserData", userInfo.getCustomerUser().getCustUser());
										result.put("signature_url", contractSign.getSignatureUrl());
										result.put("signature_template", settings.getSignatureDocumentId());
									}
									res.setResult(result);
									return res;										
								}
							}
							//Se verififca si el acceso ya se encuentra vencido
							Calendar current = Calendar.getInstance();
							if (current.after(settings.getEndDate())) {
								//Ya el evento termino por tanto hay que validar los dias de acceso
								Calendar maxDate = Calendar.getInstance();								
								if (t.getTransactionDate().before(settings.getEndDate())) {
									//Se compro antes de que fuese el evento por tanto los dias de acceso corren desde el fin del evento
									maxDate.setTime(settings.getEndDate());
								} else {
									//Se compro despues del evento por tanto los dias de acceso corren desde la fecha de compra
									maxDate.setTime(t.getTransactionDate());									
								}
								//Se coloca que la hora de vencimiento sea las 11:59:59
								maxDate.set(Calendar.HOUR_OF_DAY, 23);
								maxDate.set(Calendar.MINUTE, 59);
								maxDate.set(Calendar.SECOND, 59);
								maxDate.set(Calendar.MILLISECOND, 99);
								//Se suma a la fecha los dias de acceso permitidos
								maxDate.add(Calendar.DAY_OF_MONTH, settings.getAllowedDaysToAccess());
								if (maxDate.before(current)) {
									//El usuario ya no tiene acceso
									//Se actualiza el estatus de la transaccion a EXPIRED_ACCESS para que pueda comprar nuevamente el evento si asi lo desea
									t.setTransactionStatus(Transaction.USER_EXPIRED_ACCESS);
									try {
										txServices.updateTransactionStatus(t);
									} catch (Exception e) {
										e.printStackTrace();
										//No se compromete la transaccion por fallar este update.
									}
									Response res = new Response();
									res.setReturnCode(ServiceReturnMessages.USER_ACCESS_EXPIRED_CODE);
									res.setReturnMessage(ServiceReturnMessages.USER_ACCESS_EXPIRED);
									HashMap<String, Object> result = new HashMap<>();
									result.put("maxDateAllowed", maxDate);
									result.put("purchasedFund", purchasedFund);
									res.setResult(result);
									return res;
								}
							}
						}
					}
				}
				//Se determina si el evento tiene modalidad sim-live
				List<EventAsset> assets = eventSettingServices.findEventAssetsByEvent(aInfo.getCustomerId(), aInfo.getApplicationId());
				JSONObject playerParams = null;
				if(assets != null && !assets.isEmpty()) {
					Optional<EventAsset> asset = assets.stream().filter(t -> t.getAssetId()==Asset.URL_PLAYER).findFirst();
					if (asset.isPresent()) {
						EventAsset playerAsset = asset.get();
						if (playerAsset.getAssetParams() != null) {
							playerParams = new JSONObject(playerAsset.getAssetParams());
							playerParams = GeneralUtilities.findOffset(playerParams);
						}
					}
				}
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				result.put("customerUserData", userInfo.getCustomerUser().getCustUser());
				result.put("email", userInfo.getCustomerUser().getEmail());
				if (playerParams != null) {
					result.put("sim-live-data", playerParams.toString());
				}
				res.setResult(result);
				return res;
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.NOT_SUCCESS_TRANSACTIONS_IN_APP_CODE);
				res.setReturnMessage(ServiceReturnMessages.NOT_SUCCESS_TRANSACTIONS_IN_APP);
				HashMap<String, Object> result = new HashMap<>();
				result.put("customerUserData", userInfo.getCustomerUser().getCustUser());
				result.put("email", userInfo.getCustomerUser().getEmail());
				res.setResult(result);
				return res;				
			}
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
		} catch (AlreadyUsedAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.ALREADY_USED_ACCESS_TOKEN_CODE);
			res.setReturnMessage(ServiceReturnMessages.ALREADY_USED_ACCESS_TOKEN);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerUserData", e.getTokenOwner().getCustUser());
			result.put("email", e.getTokenOwner().getEmail());
			res.setResult(result);
			return res;			
		} catch (ClosedByUserAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.ALREADY_USED_ACCESS_TOKEN_CODE);
			res.setReturnMessage(ServiceReturnMessages.ALREADY_USED_ACCESS_TOKEN);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerUserData", e.getTokenOwner().getCustUser());
			result.put("email", e.getTokenOwner().getEmail());
			res.setResult(result);
			return res;			
		} catch (MaximumCustomerUserSessionsForAppExceededException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED_CODE);
			res.setReturnMessage(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED);
			HashMap<String, Object> result = new HashMap<>();
			result.put("userSessions", e.getCurrentSessions());
			result.put("customerUserData", e.getTokenOwner().getCustUser());
			result.put("email", e.getTokenOwner().getEmail());
			res.setResult(result);
			return res;
		} 
	}

	
	

}
