package com.us.weavx.core.application.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.application.ApplicationController;
import com.us.weavx.core.exception.ApplicationControllerGeneralException;
import com.us.weavx.core.model.ApplicationControllerParameterSelector;
import com.us.weavx.core.model.ApplicationControllerResult;
import com.us.weavx.core.model.EmailRecipient;
import com.us.weavx.core.model.EmailType;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.TransactionDetailInfo;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UtilTxServices;
import com.us.weavx.core.util.CustomerPropertyManager;
import com.us.weavx.core.util.GeneralUtilities;

@Component
public class DonationApplicationControllerImpl implements ApplicationController {
	@Autowired
	private TransactionTxServices transactionServices;
	@Autowired
	private UtilTxServices utilServices;
	@Autowired
	private CustomerPropertyManager custPropManager;
	
	public DonationApplicationControllerImpl() {
		//default constructor
	}

	@Override
	public ApplicationControllerResult onInitBuy(Map<String, Object> parameters) throws ApplicationControllerGeneralException {		
		ApplicationControllerResult result = new ApplicationControllerResult();
		result.setResult(ApplicationControllerResult.CONTINUE_TRANSACTION);
		result.setResultMessage("OK");
		return result;
	}

	@Override
	public ApplicationControllerResult onFinishBuy(Map<String, Object> parameters) throws ApplicationControllerGeneralException {
		//proposito de este metodo es enviar un correo al usuario para que pueda autenticarse.
		TransactionUserData transactionUserData = (TransactionUserData) parameters.get(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA);
		Transaction transaction = (Transaction) parameters.get(ApplicationControllerParameterSelector.TRANSACTION);
		Integer langId = (Integer) parameters.get(ApplicationControllerParameterSelector.LANG);
		try {		
			HashMap<String, Object> parametersTmp = new HashMap<>();
			parametersTmp.put(ApplicationControllerParameterSelector.APPLICATION_ID, transaction.getApplicationId());
			parametersTmp.put(ApplicationControllerParameterSelector.CUSTOMER_ID, transaction.getCustomerId());
			parametersTmp.put(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA, transactionUserData);
			parametersTmp.put(ApplicationControllerParameterSelector.TRANSACTION_ID, transaction.getId());
			parametersTmp.put(ApplicationControllerParameterSelector.LANG, langId);
			this.sendPurchaseReceipt(parametersTmp);
			ApplicationControllerResult result = new ApplicationControllerResult();
			result.setResult(ApplicationControllerResult.OK);
			result.setResultMessage("Success");
			return result;
		} catch (Exception e) {
				ApplicationControllerResult result = new ApplicationControllerResult();
				result.setResult(ApplicationControllerResult.ERROR);
				result.setResultMessage("Email sending error: "+e);
				return result;
		}
	}

	@Override
	public void sendPurchaseReceipt(Map<String, Object> parameters) throws ApplicationControllerGeneralException {
		Long applicationId = (Long) parameters.get(ApplicationControllerParameterSelector.APPLICATION_ID);
		TransactionUserData userData = (TransactionUserData) parameters.get(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA);
		Long customerId = (Long) parameters.get(ApplicationControllerParameterSelector.CUSTOMER_ID);
		Long transactionId = (Long) parameters.get(ApplicationControllerParameterSelector.TRANSACTION_ID);
		Integer langId = (Integer) parameters.get(ApplicationControllerParameterSelector.LANG);
		List<TransactionDetailInfo> transactions = transactionServices.findCustomerUserTransactionInfo(transactionId);
		try {		
			String subject = custPropManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_SUBJECT");
			String template = custPropManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_TEMPLATE");
			String rowTemplate = custPropManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_ROW_TEMPLATE");
			String fromName = custPropManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_FROM_NAME");
			String from = custPropManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_FROM");
			String dateFormat = custPropManager.findCustomerProperty(customerId, langId, "DATE_FORMAT");
			String amountFormat = custPropManager.findCustomerProperty(customerId, langId, "AMOUNT_FORMAT");
			DecimalFormat dF = (DecimalFormat) DecimalFormat.getInstance();
			dF.applyPattern(amountFormat);
			SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
			formatter.applyPattern(dateFormat);
			HashMap<String, String> params = new HashMap<String, String>();
			ArrayList<EmailRecipient> dests = new ArrayList<>();
			EmailRecipient recipient = new EmailRecipient();
			recipient.setEmail(userData.getEmail());
			recipient.setName(userData.getName());
			recipient.setRecipientInfo(new HashMap<>());
			dests.add(recipient);
			//Construir el HTML de los items del correo
			double total = 0;
			String itemsHtml = "";
			for (TransactionDetailInfo item : transactions) {
				HashMap<String, String> contentToFill = new HashMap<>();
				contentToFill.put("OrderDescription", item.getFund());
				contentToFill.put("Amount", dF.format(item.getAmount()));
				String rowContent = GeneralUtilities.geTemplateFileContentFilled(rowTemplate, contentToFill);
				itemsHtml+=rowContent;
				total+=item.getAmount();
			}
			TransactionDetailInfo transactionTmp = transactions.get(0);
			params.put("Name", userData.getName());
			String cardLast4 = "****-"+transactionTmp.getCardMasked().substring(transactionTmp.getCardMasked().lastIndexOf("X")+1);
			params.put("CardLast4", cardLast4);
			String cardBrand = transactionTmp.getCardBrand().toLowerCase();
			String cardBrandUrl = null;
			switch (cardBrand) {
			case "visa":
				cardBrandUrl = "https://s3.amazonaws.com/secure.kingjesus.org/email/img/visa+-+email.png";
				break;
			case "discover":
				cardBrandUrl = "https://s3.amazonaws.com/harvestful-cdn/discover.png";
				break;
			case "mastercard":
				cardBrandUrl = "https://s3.amazonaws.com/harvestful-cdn/mastercard.png";
				break;
			default:
				cardBrandUrl = "unknown";
			}
			params.put("CardBrandUrl", cardBrandUrl);
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTimeInMillis(transactionTmp.getTxDate().getTime());
//			Calendar localCalendar = new GregorianCalendar(TimeZone.getTimeZone("ESTEDT"));
//			localCalendar.setTimeInMillis(cal.getTimeInMillis());
			cal.add(Calendar.HOUR_OF_DAY, -4);
			params.put("purchaseDate", formatter.format(cal.getTime()));
			subject = new StringBuilder(subject).append(" ").append(GeneralUtilities.currentDateTime()).toString();
			params.put("Amount", dF.format(total)); 
			params.put("Items", itemsHtml);
			utilServices.sendEmailWithManager(from, fromName, subject, template, dests, new ArrayList<>(), params, EmailType.PURCHASE_EMAIL);			
		} catch (Exception e) {
			throw new ApplicationControllerGeneralException(e);
		}
	}
}
