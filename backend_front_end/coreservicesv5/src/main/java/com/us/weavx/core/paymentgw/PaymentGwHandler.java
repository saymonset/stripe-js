package com.us.weavx.core.paymentgw;

import java.util.HashMap;
import java.util.List;

import com.us.weavx.core.exception.PaymentGwHandlerException;
import com.us.weavx.core.model.CustomerUserCreditCard;
import com.us.weavx.core.model.PaymentResult;

public interface PaymentGwHandler {
	public List<Object> prePayment(HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public PaymentResult makePayment(double amount, HashMap<String, Object> parameters); 
	public List<Object> postPayment(HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public List<GWTransactionInfo> listGwTransactions(Long fromMillis, Long toMillis, HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public String createCustomerProfile(HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public List<CustomerUserCreditCard> listCustomerUserCards(HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public String createCustomerPlan (HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public boolean updateCustomerProfile(HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public String createCustomerSubscription (HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public String cancelCustomerSubscription (HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public CustomerUserCreditCard addCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public void removeCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException;
	public void setDefaultCreditCard(HashMap<String, Object> parameters) throws PaymentGwHandlerException;
}
