package com.us.weavx.core.paymentgw.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.net.RequestOptions;
import com.us.weavx.core.exception.PaymentGwHandlerException;
import com.us.weavx.core.model.CreditCardBrand;
import com.us.weavx.core.model.CreditCardPaymentData;
import com.us.weavx.core.model.CustomerUserCreditCard;
import com.us.weavx.core.model.EventCommissionSettings;
import com.us.weavx.core.model.PaymentResult;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.paymentgw.GWTransactionInfo;
import com.us.weavx.core.paymentgw.PaymentGwHandler;
import com.us.weavx.core.paymentgw.PaymentParameterSelector;
import com.us.weavx.core.util.CommissionPayerSelector;
import com.us.weavx.core.util.CommissionTypeSelector;

@Component
public class StripePaymentGwHandlerImpl implements PaymentGwHandler {
	
	class StripeErrors {
		public static final String CARD_DECLINED = "card_declined";
	}

	public static final String STRIPE_TOKEN = "token";
	private static final String STRIPE_PARAM_AMOUNT = "amount";
	private static final String STRIPE_PARAM_CURRENCY = "currency";
	private static final String STRIPE_DEFAULT_CURRENCY = "usd";
	private static final String STRIPE_PARAM_DESCRIPTION = "description";
	private static final String STRIPE_PARAM_SOURCE = "source";
	private static final String STRIPE_PARAM_CUSTOMER = "customer";
	private static final String STRIPE_PARAM_APPLICATION_FEE_AMOUNT = "application_fee_amount";
	private static final String STRIPE_PARAM_DESTINATION = "destination";
	private static final String STRIPE_PARAM_TRANSFER_DATA = "transfer_data";
	//private static final String STRIPE_RECEIPT_EMAIL = "receipt_email";
	public static final String CARD_LAST_4 = "card_last_4";
	public static final String CARD_BRAND = "card_brand";
	public static final String CHARGE_ID = "charge_id";
	public static final String STRIPE_PARAM_CONNECTED_ACCOUNT_ID = "destination";
	private static final String STRIPE_PARAM_EMAIL = "email";
	
	@Override
	public PaymentResult makePayment(double amount, HashMap<String, Object> parameters) {
		try {

			Stripe.apiKey = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
			String oneClic = (String) parameters.get(PaymentParameterSelector.ONE_CLIC);
			String connectedAccountId = (String) parameters.get(PaymentParameterSelector.CONNECTED_ACCOUNT_ID);
			Double commission = (Double) parameters.get(PaymentParameterSelector.EVENT_COMMISSION_SETTINGS);
			Integer commissionPayerType = (Integer) parameters.get(PaymentParameterSelector.EVENT_COMMISSION_PAYER);
			commission = (commission == null)?0d:commission;
			String paymentInstrumentId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_USER_PAYMENT_INSTRUMENT_ID);
			String token = (String) parameters.get(STRIPE_TOKEN);
			String stripeCustomerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			TransactionUserData transactionUserData = (TransactionUserData) parameters.get(PaymentParameterSelector.USER_DATA);
	        if (transactionUserData == null) {
	        	return new PaymentResult(PaymentResult.USER_DATA_MISSING, null);
	        }
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(STRIPE_PARAM_CURRENCY, STRIPE_DEFAULT_CURRENCY);
			params.put(STRIPE_PARAM_DESCRIPTION, parameters.get(PaymentParameterSelector.ORDER_DESCRIPTION));
			String source = null;
			if (stripeCustomerId != null) {
				//CLIENTE PREVIAMENTE REGISTRADO
				Customer stripeCustomer = null;
				try {
					stripeCustomer = Customer.retrieve(stripeCustomerId);
				} catch (Exception e) {
					return new PaymentResult(PaymentResult.ERROR, null);
				}
				params.put(STRIPE_PARAM_CUSTOMER, stripeCustomerId);
				if (token != null) {
					//Hay que agregar la nueva tdc al cliente
					Map<String, Object> createCardParam = new HashMap<String, Object>();
					createCardParam.put("source", token);
					Card newCard = (Card) stripeCustomer.getSources().create(createCardParam);
					source = newCard.getId();
				} else if (paymentInstrumentId != null) {
					source = paymentInstrumentId;
				} else if (oneClic != null) {
					source = stripeCustomer.getDefaultSource();
				} else {
					return new PaymentResult(PaymentResult.ERROR, null);
				}
			} else {
				//USUARIO NO REGISTRADO
				if (token != null) {
					source = token;
				} else {
					return new PaymentResult(PaymentResult.ERROR, null);
				}
			}
			params.put(STRIPE_PARAM_SOURCE, source);
			Charge charge = null;
			if (commissionPayerType == CommissionPayerSelector.USER) {
				amount = amount + commission;
			}
			params.put(STRIPE_PARAM_AMOUNT, new Double((amount)*100).intValue());
			if (connectedAccountId != null) {
				//Se procesara la transacción usando StripeConnect
				params.put(STRIPE_PARAM_APPLICATION_FEE_AMOUNT, new Double(commission*100).intValue());
				RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(connectedAccountId).build();
				charge = Charge.create(params, requestOptions);
			} else {
				//Se procesará la transacción sin StripeConnect
				charge = Charge.create(params);
			}
			HashMap<String, Object> authResp = new HashMap<String, Object>();
			authResp.put(CARD_LAST_4, ((Card) charge.getSource()).getLast4());
			authResp.put(CARD_BRAND, ((Card) charge.getSource()).getBrand());
			authResp.put(CHARGE_ID, charge.getId());
			PaymentResult result = new PaymentResult();
			result.setResult(PaymentResult.APPROVED);
			result.setResultMessage("OK");
			result.setAuthorizationInfo(authResp);
			return result;
		} catch (CardException e) {
			String errorCode = e.getCode();
			String errorMessage = e.getMessage();
			if (errorCode.equals(StripeErrors.CARD_DECLINED)) {
				//Tarjeta Negada
				PaymentResult result = new PaymentResult();
				result.setResult(PaymentResult.DENIED);
				result.setResultMessage(errorMessage);
				return result;
			} else {
				PaymentResult result = new PaymentResult();
				result.setResult(PaymentResult.ERROR);
				result.setResultMessage(errorMessage);
				return result;
			}
		} catch (Exception e) {
			PaymentResult result = new PaymentResult();
			result.setResult(PaymentResult.OTHER);
			result.setResultMessage("Unknown error: "+e.getMessage());
			return result;
		}
	}


	@Override
	public List<Object> prePayment(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		try {
			CreditCardPaymentData creditCardPaymentData = new CreditCardPaymentData();
			creditCardPaymentData.setCreditCardMasked("Unknown");
			creditCardPaymentData.setCreditCardBrandId(CreditCardBrand.VISA);
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(creditCardPaymentData);
		return result;
		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}
	}


	@Override
	public List<Object> postPayment(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		List<Object> result = new ArrayList<Object>();
		Transaction t = (Transaction) parameters.get(PaymentParameterSelector.TRANSACTION);
		if (t == null) {
			throw new PaymentGwHandlerException("transaction parameter not found.");
		}
		Map<String, Object> authorizationInfo = (Map<String,Object>) parameters.get(PaymentParameterSelector.AUTHORIZATION_INFO);
		if (authorizationInfo == null) {
			throw new PaymentGwHandlerException("authorizationInfo parameter not found.");
		}
		CreditCardPaymentData ccpd = (CreditCardPaymentData) parameters.get(PaymentParameterSelector.CREDIT_CARD_PAYMENT_DATA);
		if (ccpd == null) {
			throw new PaymentGwHandlerException("creditCardPayData parameter not found");
		}
		String cardLast4 = (String) authorizationInfo.get(CARD_LAST_4);
		String cardBrand = (String) authorizationInfo.get(CARD_BRAND);
		String chargeId = (String) authorizationInfo.get(CHARGE_ID);
		if (cardLast4 == null || cardBrand == null || chargeId == null) {
			throw new PaymentGwHandlerException("authorizationInfo cardLast4, cardBrand or chargeId not found");
		}
		ccpd.setCreditCardMasked("XXXX"+cardLast4);
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
		t.setAuthGw1(chargeId);
		t.setAuthGw2("");
		result.add(t);
		return result;
	}


	@Override
	public List<GWTransactionInfo> listGwTransactions(Long fromMillis, Long toMillis, HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		// TODO Auto-generated method stub
		List<GWTransactionInfo> result = new ArrayList<GWTransactionInfo>();
		return result;
	}


	@Override
	public String createCustomerProfile(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		try {
			Map<String, Object> customerParams = new HashMap<>();
			String token = (String) parameters.get(STRIPE_TOKEN);
			TransactionUserData tud = (TransactionUserData) parameters.get(PaymentParameterSelector.USER_DATA);
			customerParams.put(STRIPE_PARAM_SOURCE, token);
			customerParams.put(STRIPE_PARAM_DESCRIPTION, "Customer:"+tud.getName()+" "+tud.getLastname());	
			customerParams.put(STRIPE_PARAM_EMAIL, tud.getEmail());
			Stripe.apiKey = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
			Customer c = Customer.create(customerParams);
			parameters.remove(STRIPE_TOKEN);
			return c.getId();
		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}
		
	}


	@Override
	public List<CustomerUserCreditCard> listCustomerUserCards(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		try {
			List<CustomerUserCreditCard> result = new ArrayList<>();
			Map<String, Object> ccParameters = new HashMap<>();
			ccParameters.put("object", "card");
			//Se intenta validar que sea valido el cliente
			String clientId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			Stripe.apiKey = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
			Customer c = Customer.retrieve(clientId);
			String defaultCard = c.getDefaultSource();
			List<PaymentSource> cards = c.getSources().list(ccParameters).getData();
			for (PaymentSource extAcc : cards) {
				Card cardTmp = (Card) extAcc;
				CustomerUserCreditCard cuCC = new CustomerUserCreditCard();
				cuCC.setCardId(cardTmp.getId());
				cuCC.setCardLast4(cardTmp.getLast4());
				cuCC.setExpMonth(cardTmp.getExpMonth().toString());
				cuCC.setExpYear(cardTmp.getExpYear().toString());
				cuCC.setBrand(cardTmp.getBrand());
				cuCC.setDefault(cardTmp.getId().equals(defaultCard));
				result.add(cuCC);
			}
			return result;
		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}
	}
	@Override
	public String createCustomerPlan(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		// TODO Auto-generated method stub
		Stripe.apiKey = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
		
		Map<String, Object> planParams = new HashMap<String, Object>();
		planParams.put("amount", 10);
		planParams.put("interval", "month");
		planParams.put("name", "Emerald basic");
		planParams.put("currency", "usd");
		planParams.put("id", "emerald-basic");

		try {
			Plan.create(planParams);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String createCustomerSubscription(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		try {
			Subscription.create(parameters);
			return null;
			
		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		}
	}


	@Override
	public String cancelCustomerSubscription(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		Subscription sub = null;

			try {
				sub = Subscription.retrieve((String)parameters.get("idSubscription"));
			} catch (StripeException e) {
				e.printStackTrace();
			}

		try {
			sub.cancel(parameters);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public CustomerUserCreditCard addCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		try {
			Stripe.apiKey = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
			String stripeCustomerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			String newCardToken = (String) parameters.get(STRIPE_TOKEN);
			Customer c = Customer.retrieve(stripeCustomerId);
			//Hay que agregar la nueva tdc al cliente
			Map<String, Object> createCardParam = new HashMap<String, Object>();
			createCardParam.put("source", newCardToken);
			Card cc = (Card) c.getSources().create(createCardParam);
			c.setDefaultSource(cc.getId());
			CustomerUserCreditCard cuCC = new CustomerUserCreditCard();
			cuCC.setCardId(cc.getId());
			cuCC.setCardLast4(cc.getLast4());
			cuCC.setExpMonth(cc.getExpMonth().toString());
			cuCC.setExpYear(cc.getExpYear().toString());
			cuCC.setBrand(cc.getBrand());
			cuCC.setDefault(true);
			return cuCC;
		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		} 	
	}

	@Override
	public void removeCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		try {
			Stripe.apiKey = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
			String stripeCustomerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			String cardToDelete = (String) parameters.get(STRIPE_PARAM_SOURCE);
			Customer c = Customer.retrieve(stripeCustomerId);
			c.getSources().retrieve(cardToDelete);
		} catch (Exception e) {
			throw new PaymentGwHandlerException(e);
		} 	
		
	}


	@Override
	public void setDefaultCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException {
		try {
			Stripe.apiKey = (String) parameters.get(PaymentParameterSelector.AUTH_KEY_2);
			String stripeCustomerId = (String) parameters.get(PaymentParameterSelector.CUSTOMER_PROFILE);
			String newDefaultCard = (String) parameters.get(STRIPE_PARAM_SOURCE);
			Customer c = Customer.retrieve(stripeCustomerId);
			String currentDefaultCard = c.getDefaultSource();
			if (currentDefaultCard != null && !currentDefaultCard.equals(newDefaultCard)) {
				Map<String, Object> updateParams = new HashMap<String, Object>();
				updateParams.put("default_source", newDefaultCard);
				c.update(updateParams);
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
