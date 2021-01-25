package com.us.weavx.core.services.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.constants.TokenStatus;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.CustomerSignatureGateway;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserEventContractSign;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.SignatureGateway;
import com.us.weavx.core.model.User;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.SignatureServicesTx;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.util.SSEManager;
import com.us.weavx.core.util.SignatureGatewaySelector;
@Component
public class SignUserEventContractMethodImpl implements ServiceMethod {
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
			String template = (String) signatureData.get("template");
			String base64Image	= (String) signatureData.get("base_64_signature");
			String signerName = (String) signatureData.get("signer_name");
			HashMap<String, Object> signatureMetadata = (HashMap<String, Object>) params.get("signature-metadata");
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
				//Validar que se tienen todos los parametros requeridos en signature-data
				if (signatureData.get("ip_address") == null || signatureData.get("document_name") == null || signatureData.get("template") == null || signatureData.get("user_access_token") == null) {
					Response resp = new Response();
					resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
					resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+"ip_address, document_name, user_access_token, template are required signature_data parameters.");
					return resp;				
				}
				//Se valida que el customerId, applicationId del documento correspondan con los del access token
				if (customerId != aInfo.getCustomerId() || applicationId != aInfo.getApplicationId()) {
					Response resp = new Response();
					resp.setReturnCode(ServiceReturnMessages.NOT_OWNER_CODE);
					resp.setReturnMessage(ServiceReturnMessages.NOT_OWNER+":access token customer_id or application_id does not match with customer or application in document name.");
					return resp;									
				}
				//Se valida que el userAccessToken sea valido
				String userAccessToken = (String) signatureData.get("user_access_token");
				UserAccessToken uAT = userServices.getUserTokenInfo(userAccessToken);
				if (uAT == null || uAT.getApplicationId() != applicationId || uAT.getCustomerUserId() != customerUserId || uAT.getStatus() != TokenStatus.ACTIVE) {
					Response resp = new Response();
					resp.setReturnCode(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN_CODE);
					resp.setReturnMessage(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN);
					return resp;									
				}
				//Proceder a crear el documento firmado
				CustomerSignatureGateway customerGateway = signatureServices.findCustomerSignatureGatewayByCustomerId(customerId);
				if (customerGateway.getSignatureGatewayId() != SignatureGatewaySelector.HARVESTFUL) {
					//No puede usarse el metodo de firma con otro gateway diferente a HARVESTFUL
					Response resp = new Response();
					resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
					resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+": Invalid Signature Gateway: "+customerGateway.getSignatureGatewayId());
					return resp;														
				}
				SignatureGateway sGw = signatureServices.findSignatureGatewayById(customerGateway.getSignatureGatewayId());
				String signingServiceUrl = sGw.getProductionUrl();
				RestTemplate restTemplate = new RestTemplate();
				HashMap<String, Object> requestSignature = new HashMap<>();
				requestSignature.put("folder", Long.toString(customerId));
				requestSignature.put("fileName", documentName);
				requestSignature.put("template", template);
				requestSignature.put("name", signerName);
				requestSignature.putAll(signatureMetadata);
				requestSignature.put("image", base64Image);
				HashMap<String, Object> responseSignature = restTemplate.postForObject(signingServiceUrl, requestSignature, HashMap.class);
				if (responseSignature.get("error") != null) {
					//Error
					HashMap<String, Object> error = (HashMap<String, Object>) responseSignature.get("error");
					Response resp = new Response();
					resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
					resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+error.toString());
					return resp;
				}
				contractSign.setSignatureUrl((String)responseSignature.get("url")); 
				contractSign.setSignatureStatus(true);
				contractSign.setSignedAt(new Timestamp(System.currentTimeMillis()));
				contractSign.setSignatureData(new JSONObject(signatureData).toString());
				signatureServices.updateCustomerUserEventContractSign(contractSign);
				//Se debe enviar el magic link de acceso al haberse concluido la firma
				try {
					CustomerUser cu = userServices.findCustomerUserById(contractSign.getCustomerUserId());
					User u = userServices.findUserById(cu.getUserId());
					userServices.closeAllCustomerUserSessions(customerUserId);
					try {
						SSEManager sse = SSEManager.getInstance();
						sse.closeSSE(customerId+"@"+userAccessToken);
					} catch (Exception e) {
						
					}
					userServices.authenticateCustomerUserByEmailV2(u.getEmail(), customerId, applicationId, "10.0.0.1", "SignatureCompleted", 1);
				} catch (Exception e) {
					System.out.println("*******ERROR EN ENVIO DE MAGIC LINK POST FIRMA**********: "+e);
				}
			} 
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("signature_url", contractSign.getSignatureUrl());
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
