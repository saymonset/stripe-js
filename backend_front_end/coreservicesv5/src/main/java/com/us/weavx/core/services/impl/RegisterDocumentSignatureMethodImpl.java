package com.us.weavx.core.services.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserEventContractSign;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.User;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.SignatureServicesTx;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class RegisterDocumentSignatureMethodImpl implements ServiceMethod {

	@Autowired
	private SignatureServicesTx signatureServices;
	@Autowired
	private UserTxServices userServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			HashMap<String, Object> signatureData = (HashMap<String, Object>) params.get("signature-data");
			String documentName = (String) signatureData.get("document_name");
			StringTokenizer sT = new StringTokenizer(documentName, "-");
			if (sT.countTokens() < 5) {
				throw new Exception("Invalid document name format:"+documentName);
			}
			sT.nextToken();
			long customerId = Long.parseLong(sT.nextToken());
			long applicationId = Long.parseLong(sT.nextToken());
			long customerUserId = Long.parseLong(sT.nextToken());
			CustomerUserEventContractSign contractSign = signatureServices.findCustomerUserEventContractSignByCustomerUserIdAndAppId(customerUserId, applicationId);
			if (contractSign != null) {
				contractSign.setSignatureStatus(true);
				contractSign.setSignedAt(new Timestamp(System.currentTimeMillis()));
				contractSign.setSignatureData(new JSONObject(signatureData).toString());
				signatureServices.updateCustomerUserEventContractSign(contractSign);
				//Se debe enviar el magic link de acceso al haberse concluido la firma
				try {
					CustomerUser cu = userServices.findCustomerUserById(contractSign.getCustomerUserId());
					User u = userServices.findUserById(cu.getUserId());
					userServices.authenticateCustomerUserByEmailV2(u.getEmail(), customerId, applicationId, "10.0.0.1", "SignatureCompleted", 1);
				} catch (Exception e) {
					System.out.println("*******ERROR EN ENVIO DE MAGIC LINK POST FIRMA**********: "+e);
				}
			} 
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			resp.setResult(result);
			return resp;
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());
			return resp;
		}
	}
}
