package com.us.weavx.core.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserEventContractSign;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.SignatureServicesTx;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class SetSignedDocumentAdminMethodImpl implements ServiceMethod {
	@Autowired
	private SignatureServicesTx signatureServices;
	@Autowired
	private UserTxServices userServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			//Se inicializa el container de spring
			Long customerUserId = ((Number) params.get("customerUserId")).longValue();
			Long transactionId = ((Number) params.get("transactionId")).longValue();			
			String operatorComments = (String) params.get("adminComments");
			String operatorEmail = (String) params.get("adminEmail");
			String ipAddress = (String) params.get("ipAddress");
			if (customerUserId == null || operatorComments == null || operatorComments.trim().length() == 0 || operatorEmail == null || ipAddress == null) {
				throw new Exception("Mandatory parameters not found.");
			}
			CustomerUserEventContractSign contractToSign = signatureServices.findCustomerUserEventContractSignByCustomerUserIdAndAppId(customerUserId, aInfo.getApplicationId());
			JSONObject signatureData = new JSONObject();
			signatureData.put("operator", operatorEmail);
			signatureData.put("comments", operatorComments);
			signatureData.put("ipAddress", ipAddress);
			if (contractToSign == null) {
				CustomerUser cu = userServices.findCustomerUserById(customerUserId);
				contractToSign = new CustomerUserEventContractSign();
				contractToSign.setCustomerUserId(cu.getId());
				contractToSign.setApplicationId(aInfo.getApplicationId());
				contractToSign.setSignatureUrl("http://sos-signature");
				contractToSign.setTransactionId(transactionId);
				contractToSign.setSignatureStatus(true);
				contractToSign.setSignatureData(signatureData.toString());
				contractToSign.setSignedAt(Timestamp.valueOf(LocalDateTime.now()));
				signatureServices.addCustomerUserEventContractSign(contractToSign);
			}
			if (!contractToSign.isSignatureStatus()) {
				contractToSign.setSignatureStatus(true);
				contractToSign.setSignatureData(signatureData.toString());
				contractToSign.setSignedAt(Timestamp.valueOf(LocalDateTime.now()));		
				signatureServices.updateCustomerUserEventContractSign(contractToSign);
			}
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("customerUserEventContractSign", contractToSign);
			res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		} 
	}

	
	

}
