package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.BulkTransactionItem;
import com.us.weavx.core.model.BulkTransactionVerificationResultItem;
import com.us.weavx.core.model.ExternalPaymentType;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UtilTxServices;
import com.us.weavx.core.util.GeneralUtilities;

@Component
public class AddAttendeesMethodImpl implements ServiceMethod {

	@Autowired
	private TransactionTxServices transactionTxServices;
	@Autowired
	private ConfigurationTxServices configurationTxServices;
	@Autowired
	private ExternalPurchaseMethodImpl externalPurchaseMethod;
	@Autowired
	private UtilTxServices txServices;
	
	public Response executeMethod(Request request) {
		String emailCustomerUser = null;
		List<BulkTransactionVerificationResultItem> resultList = new ArrayList<>();
		try {
			
			ArrayList<HashMap<String, Object>> listTransaction = new ArrayList<HashMap<String,Object>>();
			StringBuffer registroTransacciones = new StringBuffer();
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			ArrayList<HashMap<String, Object>> txDetails = (ArrayList<HashMap<String, Object>>) params.get("tx_details");
			Integer langId = (Integer) params.get("lang_id");
			langId = (langId==null)?1:langId;
			String sourceKey = (String) params.get("transaction_source_key");
			String campaingKey = (String) params.get("transaction_campaing_key");
			String mediumKey = (String) params.get("transaction_medium_key");
			emailCustomerUser = (String) params.get("emailCustomerUser");
			ArrayList<HashMap<String, Object>> attendees = (ArrayList<HashMap<String, Object>>) params.get("attendees");

			//crear la coleccion de BulkTransactionItem para las validaciones
			ArrayList<BulkTransactionItem> attendeesList = crearColeccionBulkTransactionItem(attendees);

			//Se valida la lista de attendees
			Response responseErrorListAttendees = validarListAttendees(attendeesList);
			if(responseErrorListAttendees != null) {
				txServices.sendAlertEmailRegisterCustomer(GeneralUtilities.setMessageErrorAlertEmailRegisterCustomer(responseErrorListAttendees), emailCustomerUser);
				return responseErrorListAttendees;
			
			}
			//Al no haber errores se procesa la lista de attendees y se van generando las transacciones correspondientes.
			for (BulkTransactionItem attendee : attendeesList) {
				long startTime = System.currentTimeMillis();
				BulkTransactionVerificationResultItem currentResult = new BulkTransactionVerificationResultItem();
				try {
					ArrayList<HashMap<String, Object>> txDetailsTmp = (ArrayList<HashMap<String, Object>>) txDetails.clone();					
					currentResult.setItem(attendee);
					//Armado de parametros del metodo para el attendee
					HashMap<String, Object> parameters = new HashMap<>();
					parameters.put("lang_id", langId);
					parameters.put("transaction_source_key", sourceKey);
					parameters.put("transaction_campaing_key", campaingKey);
					parameters.put("transaction_medium_key", mediumKey);
					//Construccion del transaction user data
					HashMap<String, Object> transactionUserData = new HashMap<>();
					transactionUserData.put("name", attendee.getName());
					transactionUserData.put("lastname", attendee.getLastname());
					transactionUserData.put("email", attendee.getEmail());
					transactionUserData.put("phoneNumber", attendee.getPhone());
					parameters.put("tx_user_data", transactionUserData);
					//Construccion del ExternalPaymentData
					HashMap<String, Object> externalPaymentDataMap = construccionExternalPaymentData(currentResult, attendee, txDetailsTmp);
					currentResult = (BulkTransactionVerificationResultItem) externalPaymentDataMap.get("currentResult");
					parameters.put("external_payment_data", externalPaymentDataMap.get("externalPaymentData"));
					parameters.put("tx_details", externalPaymentDataMap.get("txDetailsTmp"));
					parameters.put("accessInfo", aInfo);
					Request externalTransactionRequest = new Request();
					externalTransactionRequest.setAccessToken(request.getAccessToken());
					externalTransactionRequest.setApplicationId(request.getApplicationId());
					externalTransactionRequest.setMethodName("externalPurchase");
					externalTransactionRequest.setParameters(parameters);
					Response extPurchaseResponse = externalPurchaseMethod.executeMethod(externalTransactionRequest);
					currentResult.setValid(extPurchaseResponse.getReturnCode() == ServiceReturnMessages.SUCCESS_CODE);
					currentResult.setMessage(extPurchaseResponse.getReturnMessage());
					resultList.add(currentResult);
					registroTransacciones.append(String.format("%s\t%s\t%s\t%s\n", attendee.getEmail(), extPurchaseResponse.getReturnCode(), extPurchaseResponse.getReturnMessage(), extPurchaseResponse.getResult().toString()));
					HashMap<String, Object> resultTransaccion = new HashMap<String, Object>();
					resultTransaccion.put("email", attendee.getEmail());
					resultTransaccion.put("returnCode", ((int)extPurchaseResponse.getReturnCode() == 0 ? 0 : 1 ));
					resultTransaccion.put("returnMessage", extPurchaseResponse.getReturnMessage());
					resultTransaccion.put("paymentResult", extPurchaseResponse.getResult());
					resultTransaccion.put("name", attendee.getName());
					resultTransaccion.put("lastname", attendee.getLastname());
					listTransaction.add(resultTransaccion);
				} catch (Exception e) {
					currentResult.setValid(false);
					currentResult.setMessage("ERROR: "+e.getMessage());
					resultList.add(currentResult);
				}
			}
			
			txServices.sendAlertEmailRegisterCustomer(GeneralUtilities.setMessageAlertEmailRegisterCustomer(listTransaction), emailCustomerUser);
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("verifiedTransactions", resultList);
			resp.setResult(result);
			return resp;
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());
			HashMap<String, Object> result = new HashMap<>();	
			result.put("verifiedTransactions", resultList);
			resp.setResult(result);	
			txServices.sendAlertEmailRegisterCustomer(GeneralUtilities.setMessageErrorAlertEmailRegisterCustomer(resp), emailCustomerUser);
			return resp;
		}
	}
	
	private ArrayList<BulkTransactionItem> crearColeccionBulkTransactionItem(ArrayList<HashMap<String, Object>> attendees) {
		ArrayList<BulkTransactionItem> attendeesList = new ArrayList<>();
		attendees.forEach(t -> {
			BulkTransactionItem newAttendee = new BulkTransactionItem();
			newAttendee.setEmail((String) t.get("email"));
			newAttendee.setName((String) t.get("name"));
			newAttendee.setLastname((String) t.get("lastname"));
			newAttendee.setPhone((String) t.get("phone"));
			newAttendee.setPaymentType((String) t.get("payment_type"));
			newAttendee.setPaymentDetails((String) t.get("payment_details"));
			attendeesList.add(newAttendee);
		});
		return attendeesList;
	}
	
	private Response validarListAttendees(ArrayList<BulkTransactionItem> attendeesList){
		Collection<BulkTransactionVerificationResultItem> verifiedAttendees = transactionTxServices.validateBulkTransactionData(attendeesList);
		boolean hasErrors = false;
		for (BulkTransactionVerificationResultItem verifiedItem : verifiedAttendees) {
			if (!verifiedItem.isValid()) {
				hasErrors = true;
				break;
			}
		}
		if (hasErrors) {
			Response resp = new Response();
			HashMap<String, Object> result = new HashMap<>();
			result.put("verifiedAttendeesResult", verifiedAttendees);
			resp.setReturnCode(ServiceReturnMessages.TRANSACTION_ABORTED_CODE);
			resp.setReturnMessage("Transaction aborted: At least one attendee has errors on his data.");
			resp.setResult(result);
			return resp;
		}
		return null;
	}
	
	private HashMap<String, Object> construccionExternalPaymentData(BulkTransactionVerificationResultItem currentResult, BulkTransactionItem attendee, ArrayList<HashMap<String, Object>> txDetailsTmp) {
		List<ExternalPaymentType> paymentTypes = configurationTxServices.listAllExternalPaymentTypes();
		HashMap<String, Object> result = new HashMap<>();
		HashMap<String, Object> externalPaymentData = new HashMap<>();
		Predicate<ExternalPaymentType> cashPredicate = t -> t.getName().toUpperCase().equals("CASH");
		ExternalPaymentType cashPaymentType = new ExternalPaymentType();
		paymentTypes.stream().filter(cashPredicate).forEach(t->cashPaymentType.setId(t.getId()));
		if (attendee.getPaymentType().toUpperCase().equals("COMPLIMENTARY")) {
			externalPaymentData.put("externalPaymentDataId", cashPaymentType.getId());
			txDetailsTmp.forEach(t -> t.put("amount", 0));
		} else {
			//Se obtiene el metodo de pago asociado 
			ExternalPaymentType currentPaymentType = new ExternalPaymentType();
			currentPaymentType.setId(-1);
			Predicate<ExternalPaymentType> currentPredicate = t -> t.getName().toUpperCase().equals(attendee.getPaymentType().toUpperCase());
			paymentTypes.stream().filter(currentPredicate).forEach(t -> currentPaymentType.setId(t.getId()));
			if (currentPaymentType.getId() == -1) {
				currentResult.setValid(false);
				currentResult.setMessage("Invalid payment type: "+attendee.getPaymentType());
			} else {
				externalPaymentData.put("externalPaymentDataId", currentPaymentType.getId());
				externalPaymentData.put("externalPaymentDataTxt", attendee.getPaymentDetails());
			}
		}
		result.put("externalPaymentData", externalPaymentData);
		result.put("currentResult", currentResult);
		result.put("txDetailsTmp", txDetailsTmp);
		
		return result;
	}
}
