package com.us.weavx.core.paymentgw.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.us.weavx.core.exception.PaymentGwHandlerException;
import com.us.weavx.core.model.CreditCardBrand;
import com.us.weavx.core.model.CreditCardPaymentData;
import com.us.weavx.core.model.CustomerUserCreditCard;
import com.us.weavx.core.model.PaymentResult;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.paymentgw.GWTransactionInfo;
import com.us.weavx.core.paymentgw.PaymentGwHandler;
import com.us.weavx.core.paymentgw.PaymentModeSelector;
import com.us.weavx.core.paymentgw.PaymentParameterSelector;

import net.authorize.Environment;
import net.authorize.api.contract.v1.ANetApiResponse;
import net.authorize.api.contract.v1.ARBCreateSubscriptionRequest;
import net.authorize.api.contract.v1.ARBCreateSubscriptionResponse;
import net.authorize.api.contract.v1.ARBSubscriptionType;
import net.authorize.api.contract.v1.ARBSubscriptionUnitEnum;
import net.authorize.api.contract.v1.ArrayOfBatchDetailsType;
import net.authorize.api.contract.v1.ArrayOfSetting;
import net.authorize.api.contract.v1.ArrayOfTransactionSummaryType;
import net.authorize.api.contract.v1.BatchDetailsType;
import net.authorize.api.contract.v1.CreateCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.CreateCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.CreateCustomerProfileRequest;
import net.authorize.api.contract.v1.CreateCustomerProfileResponse;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.CustomerAddressType;
import net.authorize.api.contract.v1.CustomerDataType;
import net.authorize.api.contract.v1.CustomerPaymentProfileExType;
import net.authorize.api.contract.v1.CustomerPaymentProfileMaskedType;
import net.authorize.api.contract.v1.CustomerPaymentProfileType;
import net.authorize.api.contract.v1.CustomerProfileIdType;
import net.authorize.api.contract.v1.CustomerProfilePaymentType;
import net.authorize.api.contract.v1.CustomerProfileType;
import net.authorize.api.contract.v1.CustomerTypeEnum;
import net.authorize.api.contract.v1.DeleteCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.DeleteCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.GetCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.GetCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.GetCustomerProfileRequest;
import net.authorize.api.contract.v1.GetCustomerProfileResponse;
import net.authorize.api.contract.v1.GetSettledBatchListRequest;
import net.authorize.api.contract.v1.GetSettledBatchListResponse;
import net.authorize.api.contract.v1.GetTransactionListRequest;
import net.authorize.api.contract.v1.GetTransactionListResponse;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.OpaqueDataType;
import net.authorize.api.contract.v1.OrderType;
import net.authorize.api.contract.v1.PaymentProfile;
import net.authorize.api.contract.v1.PaymentScheduleType;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.SettingType;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.contract.v1.TransactionResponse;
import net.authorize.api.contract.v1.TransactionSummaryType;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import net.authorize.api.contract.v1.UpdateCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.UpdateCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.ValidationModeEnum;
import net.authorize.api.controller.ARBCreateSubscriptionController;
import net.authorize.api.controller.CreateCustomerPaymentProfileController;
import net.authorize.api.controller.CreateCustomerProfileController;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.DeleteCustomerPaymentProfileController;
import net.authorize.api.controller.GetCustomerPaymentProfileController;
import net.authorize.api.controller.GetCustomerProfileController;
import net.authorize.api.controller.GetSettledBatchListController;
import net.authorize.api.controller.GetTransactionListController;
import net.authorize.api.controller.UpdateCustomerPaymentProfileController;
import net.authorize.api.controller.base.ApiOperationBase;
@Component
public class AuthorizePaymentGwHandlerImpl implements PaymentGwHandler {

	public static final String AUTHORIZE_TOKEN = "token";
	public static final String DATA_DESCRIPTOR = "COMMON.ACCEPT.INAPP.PAYMENT";
	public static final String AUTH_CODE = "authCode";
	public static final String TRANS_ID = "transId";
	public static final String ACCOUNT_ID = "accountId";
	public static final String CARD_BRAND = "accountType";
	public static final Object CREDIT_CARD_CVV = "creditCardCode";
	public static final String SUCCESSFUL_PAYMENT = "1";
	public static final Object DECLINED_PAYMENT = "2";
	public static final Object ERROR_PAYMENT = "3";
	public static final String RESPONSE_CODE = "responseCode";
	private static final String AUTHORIZE_ERROR_DENIED_TX = "2";
	private static final String AUTHORIZE_PARAM_SOURCE = "source";
	private static final String AUTHORIZE_PARAM_DESCRIPTION = "description";
	private static final String AUTHORIZE_PARAM_EMAIL = "email";
	private static final String AUTHORIZE_PARAM_AMOUNT = "amount";
	private static final String AUTHORIZE_PARAM_DATE = "createdAt";

	@Override
	public PaymentResult makePayment(double amount, HashMap<String, Object> parameters) {
		try {
			String customerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			String customerPaymentProfileId = (String) parameters
					.get(PaymentParameterSelector.CUSTOMER_USER_PAYMENT_INSTRUMENT_ID);
			int environmentType = (Integer) parameters.get(PaymentParameterSelector.PAYMENT_MODE);
			switch (environmentType) {
			case PaymentModeSelector.SANDBOX:
				ApiOperationBase.setEnvironment(Environment.SANDBOX);
				break;
			case PaymentModeSelector.PRODUCTION:
				ApiOperationBase.setEnvironment(Environment.PRODUCTION);
				break;
			default:
				return new PaymentResult(PaymentResult.INVALID_PAYMENT_MODE, null);
			}
			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			String authKey1 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_1);
			String authKey2 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
			if (authKey1 == null || authKey2 == null) {
				return new PaymentResult(PaymentResult.AUTH_KEYS_NOT_FOUND, null);
			}
			merchantAuthenticationType.setName(authKey1);
			merchantAuthenticationType.setTransactionKey(authKey2);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
			
			TransactionUserData transactionUserData = (TransactionUserData) parameters
					.get(PaymentParameterSelector.USER_DATA);
			if (transactionUserData == null) {
				return new PaymentResult(PaymentResult.USER_DATA_MISSING, null);
			}
			CustomerAddressType address = new CustomerAddressType();
			address.setFirstName(transactionUserData.getName());
			address.setLastName(transactionUserData.getLastname());
			address.setAddress(transactionUserData.getAddress());
			address.setCity(transactionUserData.getCityText());
			address.setState(transactionUserData.getStateText());
			address.setZip(transactionUserData.getPostcode());
			address.setCountry(transactionUserData.getCountryText());
			
			// Populate the payment data
			PaymentType paymentType = new PaymentType();
			String token = (String) parameters.get(AUTHORIZE_TOKEN);

			// opaque data object replacing the usual credit card object
			OpaqueDataType od = new OpaqueDataType();
			od.setDataDescriptor(DATA_DESCRIPTOR);
			od.setDataValue(token);
			paymentType.setOpaqueData(od);

			TransactionRequestType txnRequest = new TransactionRequestType();
			txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
			txnRequest.setAmount(new BigDecimal(amount));

			if (customerId != null) {
				if (customerPaymentProfileId != null) {

					CustomerProfilePaymentType profile = new CustomerProfilePaymentType();
					PaymentProfile payment = new PaymentProfile();

					profile.setCustomerProfileId(customerId);
					payment.setPaymentProfileId(customerPaymentProfileId);
					profile.setPaymentProfile(payment);

					txnRequest.setProfile(profile);

				} else {
					if (token != null) {
										
						CreateCustomerPaymentProfileRequest apiRequest = new CreateCustomerPaymentProfileRequest();
						apiRequest.setMerchantAuthentication(merchantAuthenticationType);
						apiRequest.setCustomerProfileId(customerId);

						CustomerPaymentProfileType profile = new CustomerPaymentProfileType();
						profile.setBillTo(address);
						profile.setPayment(paymentType);
						profile.setDefaultPaymentProfile(true);
						apiRequest.setPaymentProfile(profile);

						CreateCustomerPaymentProfileController controller = new CreateCustomerPaymentProfileController(apiRequest);
						controller.execute();
						CreateCustomerPaymentProfileResponse response = new CreateCustomerPaymentProfileResponse();
						response = controller.getApiResponse();
						if (response != null) {
							if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

								GetCustomerPaymentProfileRequest apiRequestc = new GetCustomerPaymentProfileRequest();
								apiRequestc.setCustomerProfileId(customerId);
								apiRequestc.setCustomerPaymentProfileId(response.getCustomerPaymentProfileId());
								apiRequestc.setUnmaskExpirationDate(true);
								GetCustomerPaymentProfileController controllerc = new GetCustomerPaymentProfileController(apiRequestc);
								controllerc.execute();

								GetCustomerPaymentProfileResponse responsec = new GetCustomerPaymentProfileResponse();
								responsec = controllerc.getApiResponse();

								if (responsec != null) {

									if (responsec.getMessages().getResultCode() == MessageTypeEnum.OK) {

										customerPaymentProfileId = response.getCustomerPaymentProfileId();
										CustomerProfilePaymentType profile2 = new CustomerProfilePaymentType();
										PaymentProfile payment = new PaymentProfile();

										profile2.setCustomerProfileId(customerId);
										payment.setPaymentProfileId(customerPaymentProfileId);
										profile2.setPaymentProfile(payment);

										txnRequest.setProfile(profile2);
									}
								}
							} else {

								txnRequest.setPayment(paymentType);
								System.out.println("Failed to create customer payment profile, credit card exist."
										+ response.getMessages().getResultCode());
							}
						}

					} else {

						GetCustomerProfileRequest apiRequest = new GetCustomerProfileRequest();
						apiRequest.setCustomerProfileId(customerId);
						apiRequest.setUnmaskExpirationDate(true);

						GetCustomerProfileController controller = new GetCustomerProfileController(apiRequest);
						controller.execute();

						GetCustomerProfileResponse response = new GetCustomerProfileResponse();
						response = controller.getApiResponse();

						if (response != null) {

							if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

								if (!response.getProfile().getPaymentProfiles().isEmpty()) {

									List<CustomerPaymentProfileMaskedType> card = response.getProfile()
											.getPaymentProfiles();
									for (int i = 0; i < card.size(); i++) {
										customerPaymentProfileId = card.get(i).getCustomerPaymentProfileId();
									}
									CustomerProfilePaymentType profile = new CustomerProfilePaymentType();
									PaymentProfile payment = new PaymentProfile();

									profile.setCustomerProfileId(customerId);
									payment.setPaymentProfileId(customerPaymentProfileId);
									profile.setPaymentProfile(payment);

									txnRequest.setProfile(profile);
								}
								if ((response.getSubscriptionIds() != null)
										&& (response.getSubscriptionIds().getSubscriptionId() != null)
										&& (!response.getSubscriptionIds().getSubscriptionId().isEmpty())) {
									System.out.println("List of subscriptions:");
									for (String subscriptionid : response.getSubscriptionIds().getSubscriptionId())
										System.out.println(subscriptionid);
								}

							} else {
								System.out.println(
										"Failed to get customer profile:  " + response.getMessages().getResultCode());
							}
						}
					}
				}
			} else {
				txnRequest.setPayment(paymentType);

				txnRequest.setBillTo(address);

				CustomerDataType customer = new CustomerDataType();
				customer.setType(CustomerTypeEnum.INDIVIDUAL);
				customer.setEmail(transactionUserData.getEmail());

				txnRequest.setCustomer(customer);

			}

			OrderType order = new OrderType();
			String orderDescription = (String) parameters.get(PaymentParameterSelector.ORDER_DESCRIPTION);
			if (orderDescription == null) {
				return new PaymentResult(PaymentResult.PARAMETER_MISSING,
						PaymentParameterSelector.ORDER_DESCRIPTION + " is required.", null);
			}

			order.setDescription(orderDescription);
			txnRequest.setOrder(order);
			ArrayOfSetting settingsArr = new ArrayOfSetting();
			List<SettingType> settings = settingsArr.getSetting();
			SettingType settingAllowPartialAuth = new SettingType();
			settingAllowPartialAuth.setSettingName("allowPartialAuth");
			settingAllowPartialAuth.setSettingValue("false");
			settings.add(settingAllowPartialAuth);
			SettingType emailCustomer = new SettingType();
			emailCustomer.setSettingName("emailCustomer");
			emailCustomer.setSettingValue("true");
			settings.add(emailCustomer);
			SettingType recurringBilling = new SettingType();
			recurringBilling.setSettingName("recurringBilling");
			recurringBilling.setSettingValue("false");
			settings.add(recurringBilling);
			txnRequest.setTransactionSettings(settingsArr);
			// Make the API Request

			CreateTransactionRequest apiRequest = new CreateTransactionRequest();
			apiRequest.setTransactionRequest(txnRequest);
			CreateTransactionController controller = new CreateTransactionController(apiRequest);
			controller.execute();
			CreateTransactionResponse response = controller.getApiResponse();
			if (response != null) {

				// If API Response is ok, go ahead and check the transaction
				// response
				if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

					TransactionResponse result = response.getTransactionResponse();
					if (result.getResponseCode().equals(SUCCESSFUL_PAYMENT)) {
						PaymentResult p = new PaymentResult();
						p.setResult(PaymentResult.APPROVED);
						HashMap<String, Object> authResp = new HashMap<String, Object>();
						authResp.put(RESPONSE_CODE, result.getResponseCode());
						authResp.put(AUTH_CODE, result.getAuthCode());
						authResp.put(TRANS_ID, result.getTransId());
						authResp.put(ACCOUNT_ID, result.getAccountNumber());
						authResp.put(CARD_BRAND, result.getAccountType());
						p.setResultMessage(result.getMessages().getMessage().get(0).getDescription());
						p.setAuthorizationInfo(authResp);
						return p;
					} else if (result.getResponseCode().equals(DECLINED_PAYMENT)) {
						PaymentResult p = new PaymentResult();
						p.setResult(PaymentResult.DENIED);
						p.setResultMessage(result.getErrors().getError().get(0).getErrorText());
						HashMap<String, Object> authResp = new HashMap<String, Object>();
						authResp.put(RESPONSE_CODE, result.getResponseCode());
						authResp.put(AUTH_CODE, result.getResponseCode());
						authResp.put(TRANS_ID, result.getTransId());
						p.setAuthorizationInfo(authResp);
						return p;
					} else if (result.getResponseCode().equals(ERROR_PAYMENT)) {
						// Hay que revisar el error code a ver si se trata de
						// una transaccion negada u otro error
						if (result.getErrors().getError().get(0).getErrorCode().equals(AUTHORIZE_ERROR_DENIED_TX)) {
							// Es una transaccion negada.
							PaymentResult p = new PaymentResult();
							p.setResult(PaymentResult.DENIED);
							p.setResultMessage(result.getErrors().getError().get(0).getErrorText());
							return p;
						} else {
							PaymentResult p = new PaymentResult();
							p.setResult(PaymentResult.ERROR);
							p.setResultMessage(result.getErrors().getError().get(0).getErrorText());
							return p;
						}
					} else {
						PaymentResult p = new PaymentResult();
						p.setResult(PaymentResult.OTHER);
						p.setResultMessage("Unknown payment error.");
						return p;
					}
				} else {
					PaymentResult p = new PaymentResult();
					p.setResult(PaymentResult.NETWORK_ERROR);
					p.setResultMessage(response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
					return p;
				}
			} else {
				PaymentResult p = new PaymentResult();
				p.setResult(PaymentResult.NO_RESPONSE);
				p.setResultMessage(controller.getErrorResponse().getMessages().getMessage().get(0).getText());
				return p;
			}
		} catch (Exception e) {
			PaymentResult p = new PaymentResult();
			p.setResult(PaymentResult.RUNTIME_ERROR);
			p.setResultMessage(e.getMessage());
			return p;
		}
	}

	@Override
	public List<Object> prePayment(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		ArrayList<Object> result = new ArrayList<Object>();
		try {

			CreditCardPaymentData creditCardPaymentData = new CreditCardPaymentData();
			creditCardPaymentData.setCreditCardMasked("Unknown");
			creditCardPaymentData.setCreditCardBrandId(CreditCardBrand.VISA);
			result.add(creditCardPaymentData);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Object> postPayment(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		List<Object> result = new ArrayList<Object>();
		Transaction t = (Transaction) parameters.get(PaymentParameterSelector.TRANSACTION);
		if (t == null) {
			throw new PaymentGwHandlerException("transaction parameter not found.");
		}
		Map<String, Object> authorizationInfo = (Map<String, Object>) parameters
				.get(PaymentParameterSelector.AUTHORIZATION_INFO);
		if (authorizationInfo == null) {
			throw new PaymentGwHandlerException("authorizationInfo parameter not found.");
		}
		String cardLast4 = (String) authorizationInfo.get(ACCOUNT_ID);
		String transId = (String) authorizationInfo.get(TRANS_ID);
		String authCode = (String) authorizationInfo.get(AUTH_CODE);
		String cardBrand = (String) authorizationInfo.get(CARD_BRAND);

		if (transId == null || authCode == null) {
			throw new PaymentGwHandlerException("invalid authorizationInfo transId or authCode");
		}

		CreditCardPaymentData ccpd = (CreditCardPaymentData) parameters
				.get(PaymentParameterSelector.CREDIT_CARD_PAYMENT_DATA);
		if (ccpd == null) {
			throw new PaymentGwHandlerException("creditCardPayData parameter not found");
		}

		ccpd.setCreditCardMasked(cardLast4);
		String creditCardBrand = cardBrand.toLowerCase();

		switch (creditCardBrand) {
		case "visa":
			ccpd.setCreditCardBrandId(CreditCardBrand.VISA);
			break;
		case "discover":
			ccpd.setCreditCardBrandId(CreditCardBrand.DISCOVER);
			break;
		case "mastercard":
			ccpd.setCreditCardBrandId(CreditCardBrand.MASTER);
			break;
		}
		result.add(ccpd);

		t.setAuthGw1(transId);
		t.setAuthGw2(authCode);
		result.add(t);
		return result;
	}

	@Override
	public List<GWTransactionInfo> listGwTransactions(Long fromMillis, Long toMillis,
			HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		try {
			String authName = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_1);
			String authTransKey = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			merchantAuthenticationType.setName(authName);
			merchantAuthenticationType.setTransactionKey(authTransKey);
			int environmentType = (Integer) parameters.get(PaymentParameterSelector.PAYMENT_MODE);
			switch (environmentType) {
			case PaymentModeSelector.SANDBOX:
				ApiOperationBase.setEnvironment(Environment.SANDBOX);
				break;
			case PaymentModeSelector.PRODUCTION:
				ApiOperationBase.setEnvironment(Environment.PRODUCTION);
				break;
			default:
				throw new PaymentGwHandlerException("Invalid Environment: " + environmentType);
			}
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
			GetSettledBatchListRequest request = new GetSettledBatchListRequest();
			GregorianCalendar calFrom = (GregorianCalendar) GregorianCalendar.getInstance();
			if (fromMillis == null) {
				// Se debe tomar las 00:00 del dia anterior
				calFrom.add(Calendar.DAY_OF_MONTH, -1);
				calFrom = (GregorianCalendar) DateUtils.truncate(calFrom, Calendar.DATE);
			} else {
				calFrom.setTimeInMillis(fromMillis);
			}
			GregorianCalendar calTo = (GregorianCalendar) GregorianCalendar.getInstance();
			if (toMillis != null) {
				calTo.setTimeInMillis(toMillis);
			}
			request.setFirstSettlementDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(calFrom));
			request.setLastSettlementDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(calTo));
			GetSettledBatchListController controller = new GetSettledBatchListController(request);
			controller.execute();
			GetSettledBatchListResponse response = controller.getApiResponse();
			ArrayOfBatchDetailsType batchDetails = response.getBatchList();
			List<BatchDetailsType> det = batchDetails.getBatch();
			Iterator<BatchDetailsType> iter = det.iterator();
			ArrayList<String> ids = new ArrayList<String>();
			while (iter.hasNext()) {
				BatchDetailsType tmpBatch = iter.next();
				ids.add(tmpBatch.getBatchId());
			}
			Iterator<String> batchIdsIterator = ids.iterator();
			GetTransactionListRequest transactionListTmp = new GetTransactionListRequest();
			ArrayList<GWTransactionInfo> gwTransactions = new ArrayList<GWTransactionInfo>();
			while (batchIdsIterator.hasNext()) {
				String tmpId = batchIdsIterator.next();
				transactionListTmp.setBatchId(tmpId);
				GetTransactionListController tmpController = new GetTransactionListController(transactionListTmp);
				tmpController.execute();
				GetTransactionListResponse tmpResponse = tmpController.getApiResponse();
				ArrayOfTransactionSummaryType txs = tmpResponse.getTransactions();
				List<TransactionSummaryType> txList = txs.getTransaction();
				Iterator<TransactionSummaryType> txListIterator = txList.iterator();
				while (txListIterator.hasNext()) {
					TransactionSummaryType txTmp = txListIterator.next();
					GWTransactionInfo newTx = new GWTransactionInfo();
					newTx.setGwTransactionId(txTmp.getTransId());
					gwTransactions.add(newTx);
				}
			}
			return gwTransactions;
		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}
	}

	@Override
	public String createCustomerProfile(HashMap<String, Object> parameters) throws PaymentGwHandlerException {

		try {
			String customerId = "";
			String merchantCustomerId = "";
			int maxlength = 20;
			int environmentType = (Integer) parameters.get(PaymentParameterSelector.PAYMENT_MODE);

			switch (environmentType) {
			case PaymentModeSelector.SANDBOX:
				ApiOperationBase.setEnvironment(Environment.SANDBOX);
				break;
			case PaymentModeSelector.PRODUCTION:
				ApiOperationBase.setEnvironment(Environment.PRODUCTION);
				break;
			default:
				throw new PaymentGwHandlerException("Invalid Environment: " + environmentType);
			}

			String authKey1 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_1);
			String authKey2 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);

			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			merchantAuthenticationType.setName(authKey1);
			merchantAuthenticationType.setTransactionKey(authKey2);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

			Map<String, Object> customerParams = new HashMap<>();
			String token = (String) parameters.get(AUTHORIZE_TOKEN);
			TransactionUserData tud = (TransactionUserData) parameters.get(PaymentParameterSelector.USER_DATA);
			customerParams.put(AUTHORIZE_PARAM_SOURCE, token);
			customerParams.put(AUTHORIZE_PARAM_DESCRIPTION, tud.getName() + " " + tud.getLastname());
			customerParams.put(AUTHORIZE_PARAM_EMAIL, tud.getEmail());

			
			if (customerParams.get(AUTHORIZE_PARAM_EMAIL).toString().length() >  maxlength) {
				merchantCustomerId = customerParams.get(AUTHORIZE_PARAM_EMAIL).toString().substring(0,maxlength);
			}
			else
			{
				merchantCustomerId = customerParams.get(AUTHORIZE_PARAM_EMAIL).toString();
			}
			

			TransactionUserData transactionUserData = (TransactionUserData) parameters
					.get(PaymentParameterSelector.USER_DATA);
		
			CustomerAddressType address = new CustomerAddressType();
			address.setFirstName(transactionUserData.getName());
			address.setLastName(transactionUserData.getLastname());
			address.setAddress(transactionUserData.getAddress());
			address.setCity(transactionUserData.getCityText());
			address.setState(transactionUserData.getStateText());
			address.setZip(transactionUserData.getPostcode());
			address.setCountry(transactionUserData.getCountryText());
			
			PaymentType paymentType = new PaymentType();
			// opaque data object replacing the usual credit card object
			OpaqueDataType od = new OpaqueDataType();
			od.setDataDescriptor(DATA_DESCRIPTOR);
			od.setDataValue(token);
			paymentType.setOpaqueData(od);

			// Set payment profile data
			CustomerPaymentProfileType customerPaymentProfileType = new CustomerPaymentProfileType();
			customerPaymentProfileType.setCustomerType(CustomerTypeEnum.INDIVIDUAL);

			customerPaymentProfileType.setPayment(paymentType);
			customerPaymentProfileType.setBillTo(address);
			customerPaymentProfileType.setDefaultPaymentProfile(true);
			// Set customer profile data
			CustomerProfileType customerProfileType = new CustomerProfileType();
			customerProfileType.setMerchantCustomerId(merchantCustomerId);
			customerProfileType.setDescription((String) customerParams.get(AUTHORIZE_PARAM_DESCRIPTION));
			customerProfileType.setEmail((String) customerParams.get(AUTHORIZE_PARAM_EMAIL));
			customerProfileType.getPaymentProfiles().add(customerPaymentProfileType);

			// Create the API request and set the parameters for this specific request
			CreateCustomerProfileRequest apiRequest = new CreateCustomerProfileRequest();
			apiRequest.setProfile(customerProfileType);
			apiRequest.setValidationMode(ValidationModeEnum.TEST_MODE);

			// Call the controller
			CreateCustomerProfileController controller = new CreateCustomerProfileController(apiRequest);
			controller.execute();

			// Get the response
			CreateCustomerProfileResponse response = new CreateCustomerProfileResponse();
			response = controller.getApiResponse();

			// Parse the response to determine results
			if (response != null) {
				if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
					customerId = response.getCustomerProfileId();
				}
			} else {
				ANetApiResponse errorResponse = controller.getErrorResponse();
				System.out.println("Failed to get response");
				if (!errorResponse.getMessages().getMessage().isEmpty()) {
					System.out.println("Error: " + errorResponse.getMessages().getMessage().get(0).getCode() + " \n"
							+ errorResponse.getMessages().getMessage().get(0).getText());
				}
			}
			parameters.remove(AUTHORIZE_TOKEN);
			return customerId;
		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}

	}

	@Override
	public List<CustomerUserCreditCard> listCustomerUserCards(HashMap<String, Object> parameters)
			throws PaymentGwHandlerException {

		try {

			String customerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			List<CustomerUserCreditCard> result = new ArrayList<>();

			int environmentType = (Integer) parameters.get(PaymentParameterSelector.PAYMENT_MODE);

			switch (environmentType) {
			case PaymentModeSelector.SANDBOX:
				ApiOperationBase.setEnvironment(Environment.SANDBOX);
				break;
			case PaymentModeSelector.PRODUCTION:
				ApiOperationBase.setEnvironment(Environment.PRODUCTION);
				break;
			default:
				throw new PaymentGwHandlerException("Invalid Environment: " + environmentType);
			}

			String authKey1 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_1);
			String authKey2 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);

			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			merchantAuthenticationType.setName(authKey1);
			merchantAuthenticationType.setTransactionKey(authKey2);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

			GetCustomerProfileRequest apiRequest = new GetCustomerProfileRequest();
			apiRequest.setCustomerProfileId(customerId);
			apiRequest.setUnmaskExpirationDate(true);

			GetCustomerProfileController controller = new GetCustomerProfileController(apiRequest);
			controller.execute();

			GetCustomerProfileResponse response = new GetCustomerProfileResponse();
			response = controller.getApiResponse();

			if (response != null) {

				if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

					if (!response.getProfile().getPaymentProfiles().isEmpty()) {

						List<CustomerPaymentProfileMaskedType> card = response.getProfile().getPaymentProfiles();
						for (int i = 0; i < card.size(); i++) {
							CustomerUserCreditCard cuCC = new CustomerUserCreditCard();
							cuCC.setCardId(card.get(i).getCustomerPaymentProfileId());
							cuCC.setCardLast4(card.get(i).getPayment().getCreditCard().getCardNumber().substring(4));
							cuCC.setExpMonth(card.get(i).getPayment().getCreditCard().getExpirationDate().substring(5));
							cuCC.setExpYear(card.get(i).getPayment().getCreditCard().getExpirationDate().substring(0, 4));
							cuCC.setBrand(card.get(i).getPayment().getCreditCard().getCardType());
							if(card.size()  == 1) {
								cuCC.setDefault(true);
							}
							else
							{
								cuCC.setDefault((card.get(i).isDefaultPaymentProfile() == null) ? false : true);
							}
							result.add(cuCC);
						}
					}
					if ((response.getSubscriptionIds() != null)
							&& (response.getSubscriptionIds().getSubscriptionId() != null)
							&& (!response.getSubscriptionIds().getSubscriptionId().isEmpty())) {
						System.out.println("List of subscriptions:");
						for (String subscriptionid : response.getSubscriptionIds().getSubscriptionId())
							System.out.println(subscriptionid);
					}

				} else {
					System.out.println("Failed to get customer profile:  " + response.getMessages().getResultCode());
				}
			}
			return result;

		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}

	}

	@Override
	public String createCustomerSubscription(HashMap<String, Object> parameters) throws PaymentGwHandlerException {

		try {

			String customerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			String customerPaymentProfileId = (String) parameters.get(AUTHORIZE_PARAM_SOURCE);

			int environmentType = (Integer) parameters.get(PaymentParameterSelector.PAYMENT_MODE);

			switch (environmentType) {
			case PaymentModeSelector.SANDBOX:
				ApiOperationBase.setEnvironment(Environment.SANDBOX);
				break;
			case PaymentModeSelector.PRODUCTION:
				ApiOperationBase.setEnvironment(Environment.PRODUCTION);
				break;
			default:
				throw new PaymentGwHandlerException("Invalid Environment: " + environmentType);
			}

			String authKey1 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_1);
			String authKey2 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);

			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			merchantAuthenticationType.setName(authKey1);
			merchantAuthenticationType.setTransactionKey(authKey2);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
			// Set up payment schedule
			PaymentScheduleType schedule = new PaymentScheduleType();
			PaymentScheduleType.Interval interval = new PaymentScheduleType.Interval();
			// interval.setLength(intervalLength);
			interval.setUnit(ARBSubscriptionUnitEnum.DAYS);
			schedule.setInterval(interval);

			try {
				XMLGregorianCalendar startDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
				startDate.setDay(Integer.parseInt(parameters.get(AUTHORIZE_PARAM_DATE).toString().substring(8, 10)));
				startDate.setMonth(Integer.parseInt(parameters.get(AUTHORIZE_PARAM_DATE).toString().substring(5, 7)));
				startDate.setYear(Integer.parseInt(parameters.get(AUTHORIZE_PARAM_DATE).toString().substring(0, 4)));
				schedule.setStartDate(startDate); // 2020-08-30
			} catch (Exception e) {
			}

			schedule.setTotalOccurrences((short) 12);
			schedule.setTrialOccurrences((short) 1);

			CustomerProfileIdType profile = new CustomerProfileIdType();
			profile.setCustomerProfileId(customerId);
			profile.setCustomerPaymentProfileId(customerPaymentProfileId);
			// profile.setCustomerAddressId(customerAddressId);

			ARBSubscriptionType arbSubscriptionType = new ARBSubscriptionType();
			arbSubscriptionType.setPaymentSchedule(schedule);
			arbSubscriptionType.setAmount(
					new BigDecimal((String) parameters.get(AUTHORIZE_PARAM_AMOUNT)).setScale(2, RoundingMode.CEILING));
			arbSubscriptionType.setTrialAmount(new BigDecimal(0.0).setScale(2, RoundingMode.CEILING));
			arbSubscriptionType.setProfile(profile);

			// Make the API Request
			ARBCreateSubscriptionRequest apiRequest = new ARBCreateSubscriptionRequest();
			apiRequest.setSubscription(arbSubscriptionType);
			ARBCreateSubscriptionController controller = new ARBCreateSubscriptionController(apiRequest);
			controller.execute();
			ARBCreateSubscriptionResponse response = controller.getApiResponse();
			if (response != null) {

				if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

					System.out.println(response.getSubscriptionId());
					System.out.println(response.getMessages().getMessage().get(0).getCode());
					System.out.println(response.getMessages().getMessage().get(0).getText());
				} else {
					System.out.println("Failed to create Subscription:  " + response.getMessages().getResultCode());
					System.out.println(response.getMessages().getMessage().get(0).getText());
				}
			}

			return "";

		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}
	}

	@Override
	public String cancelCustomerSubscription(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createCustomerPlan(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerUserCreditCard addCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException {

		try {
			String customerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			CustomerUserCreditCard cuCC = new CustomerUserCreditCard();

			int environmentType = (Integer) parameters.get(PaymentParameterSelector.PAYMENT_MODE);
			switch (environmentType) {
			case PaymentModeSelector.SANDBOX:
				ApiOperationBase.setEnvironment(Environment.SANDBOX);
				break;
			case PaymentModeSelector.PRODUCTION:
				ApiOperationBase.setEnvironment(Environment.PRODUCTION);
				break;
			default:
				throw new PaymentGwHandlerException("Invalid Environment: " + environmentType);
			}
			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			String authKey1 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_1);
			String authKey2 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);

			merchantAuthenticationType.setName(authKey1);
			merchantAuthenticationType.setTransactionKey(authKey2);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
			

			TransactionUserData transactionUserData = (TransactionUserData) parameters
					.get(PaymentParameterSelector.USER_DATA);
		
			CustomerAddressType address = new CustomerAddressType();
			address.setFirstName(transactionUserData.getName());
			address.setLastName(transactionUserData.getLastname());
			address.setAddress(transactionUserData.getAddress());
			address.setCity(transactionUserData.getCityText());
			address.setState(transactionUserData.getStateText());
			address.setZip(transactionUserData.getPostcode());
			address.setCountry(transactionUserData.getCountryText());
			
			// Populate the payment data
			PaymentType paymentType = new PaymentType();
			String token = (String) parameters.get(AUTHORIZE_TOKEN);

			// opaque data object replacing the usual credit card object
			OpaqueDataType od = new OpaqueDataType();
			od.setDataDescriptor(DATA_DESCRIPTOR);
			od.setDataValue(token);
			paymentType.setOpaqueData(od);

			CreateCustomerPaymentProfileRequest apiRequest = new CreateCustomerPaymentProfileRequest();
			apiRequest.setMerchantAuthentication(merchantAuthenticationType);
			apiRequest.setCustomerProfileId(customerId);

			CustomerPaymentProfileType profile = new CustomerPaymentProfileType();

			profile.setPayment(paymentType);
			profile.setDefaultPaymentProfile(true);
			profile.setBillTo(address);
			apiRequest.setPaymentProfile(profile);

			CreateCustomerPaymentProfileController controller = new CreateCustomerPaymentProfileController(apiRequest);
			controller.execute();
			CreateCustomerPaymentProfileResponse response = new CreateCustomerPaymentProfileResponse();
			response = controller.getApiResponse();
			if (response != null) {
				if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

					GetCustomerPaymentProfileRequest apiRequestc = new GetCustomerPaymentProfileRequest();
					apiRequestc.setCustomerProfileId(customerId);
					apiRequestc.setCustomerPaymentProfileId(response.getCustomerPaymentProfileId());
					apiRequestc.setUnmaskExpirationDate(true);
					GetCustomerPaymentProfileController controllerc = new GetCustomerPaymentProfileController(
							apiRequestc);
					controllerc.execute();

					GetCustomerPaymentProfileResponse responsec = new GetCustomerPaymentProfileResponse();
					responsec = controllerc.getApiResponse();

					if (responsec != null) {

						if (responsec.getMessages().getResultCode() == MessageTypeEnum.OK) {

							cuCC.setCardId(response.getCustomerPaymentProfileId());
							cuCC.setCardLast4(responsec.getPaymentProfile().getPayment().getCreditCard().getCardNumber());
							cuCC.setExpMonth(responsec.getPaymentProfile().getPayment().getCreditCard().getExpirationDate());
							cuCC.setExpYear(responsec.getPaymentProfile().getPayment().getCreditCard().getExpirationDate());
							cuCC.setBrand(responsec.getPaymentProfile().getPayment().getCreditCard().getCardType());
							cuCC.setDefault((responsec.getPaymentProfile().isDefaultPaymentProfile() == null) ? false : true);

						}
					}
					if (response.getValidationDirectResponse() != null)
						System.out.println(response.getValidationDirectResponse());
				} else {

					System.out.println("Failed to create customer payment profile, credit card exist."
							+ response.getMessages().getResultCode());
				}
			}

			return cuCC;

		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}

	}

	@Override
	public void removeCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException {

		try {
			String customerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			String customerPaymentProfileId = (String) parameters.get(AUTHORIZE_PARAM_SOURCE);

			int environmentType = (Integer) parameters.get(PaymentParameterSelector.PAYMENT_MODE);
			switch (environmentType) {
			case PaymentModeSelector.SANDBOX:
				ApiOperationBase.setEnvironment(Environment.SANDBOX);
				break;
			case PaymentModeSelector.PRODUCTION:
				ApiOperationBase.setEnvironment(Environment.PRODUCTION);
				break;
			default:
				throw new PaymentGwHandlerException("Invalid Environment: " + environmentType);
			}
			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			String authKey1 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_1);
			String authKey2 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);

			merchantAuthenticationType.setName(authKey1);
			merchantAuthenticationType.setTransactionKey(authKey2);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

			DeleteCustomerPaymentProfileRequest apiRequest = new DeleteCustomerPaymentProfileRequest();
			apiRequest.setCustomerProfileId(customerId);
			apiRequest.setCustomerPaymentProfileId(customerPaymentProfileId);

			DeleteCustomerPaymentProfileController controller = new DeleteCustomerPaymentProfileController(apiRequest);
			controller.execute();

			DeleteCustomerPaymentProfileResponse response = new DeleteCustomerPaymentProfileResponse();
			response = controller.getApiResponse();

			if (response != null) {

				if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

					System.out.println(response.getMessages().getMessage().get(0).getCode());
					System.out.println(response.getMessages().getMessage().get(0).getText());
				} else {
					System.out.println(
							"Failed to delete customer payment profile:  " + response.getMessages().getResultCode());
				}
			}

		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}

	}

	@Override
	public void setDefaultCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException {

		try {
			String customerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			String customerPaymentProfileId = (String) parameters.get(AUTHORIZE_PARAM_SOURCE);
			CustomerUserCreditCard cuCC = new CustomerUserCreditCard();

			int environmentType = (Integer) parameters.get(PaymentParameterSelector.PAYMENT_MODE);
			switch (environmentType) {
			case PaymentModeSelector.SANDBOX:
				ApiOperationBase.setEnvironment(Environment.SANDBOX);
				break;
			case PaymentModeSelector.PRODUCTION:
				ApiOperationBase.setEnvironment(Environment.PRODUCTION);
				break;
			default:
				throw new PaymentGwHandlerException("Invalid Environment: " + environmentType);
			}
			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			String authKey1 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_1);
			String authKey2 = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);

			merchantAuthenticationType.setName(authKey1);
			merchantAuthenticationType.setTransactionKey(authKey2);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

			GetCustomerPaymentProfileRequest apiRequestc = new GetCustomerPaymentProfileRequest();
			apiRequestc.setCustomerProfileId(customerId);
			apiRequestc.setCustomerPaymentProfileId(customerPaymentProfileId);
			apiRequestc.setUnmaskExpirationDate(true);
			GetCustomerPaymentProfileController controllerc = new GetCustomerPaymentProfileController(apiRequestc);
			controllerc.execute();

			GetCustomerPaymentProfileResponse responsec = new GetCustomerPaymentProfileResponse();
			responsec = controllerc.getApiResponse();

			if (responsec != null) {

				if (responsec.getMessages().getResultCode() == MessageTypeEnum.OK) {

					// credit card details
					CreditCardType creditCard = new CreditCardType();
					creditCard
							.setCardNumber(responsec.getPaymentProfile().getPayment().getCreditCard().getCardNumber());
					creditCard.setExpirationDate(
							responsec.getPaymentProfile().getPayment().getCreditCard().getExpirationDate());

					PaymentType paymentType = new PaymentType();
					paymentType.setCreditCard(creditCard);

					CustomerPaymentProfileExType customer = new CustomerPaymentProfileExType();
					customer.setPayment(paymentType);
					customer.setDefaultPaymentProfile(true);
					customer.setCustomerPaymentProfileId(customerPaymentProfileId);

					UpdateCustomerPaymentProfileRequest apiRequest = new UpdateCustomerPaymentProfileRequest();
					apiRequest.setCustomerProfileId(customerId);
					apiRequest.setPaymentProfile(customer);
					apiRequest.setValidationMode(ValidationModeEnum.TEST_MODE);

					UpdateCustomerPaymentProfileController controller = new UpdateCustomerPaymentProfileController(apiRequest);
					controller.execute();

					UpdateCustomerPaymentProfileResponse response = new UpdateCustomerPaymentProfileResponse();
					response = controller.getApiResponse();

					if (response != null) {

						if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

							System.out.println(response.getMessages().getMessage().get(0).getCode());
							System.out.println(response.getMessages().getMessage().get(0).getText());
						} else {
							System.out.println("Failed to update customer payment profile:  "
									+ response.getMessages().getResultCode());
						}
					}
				}
			}

		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}

	}

	@Override
	public boolean updateCustomerProfile(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		// TODO Auto-generated method stub
		return false;
	}

}
