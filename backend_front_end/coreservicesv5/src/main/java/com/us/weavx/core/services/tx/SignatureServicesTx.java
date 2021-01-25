package com.us.weavx.core.services.tx;

import com.us.weavx.core.data.SignatureServicesTxDAO;
import com.us.weavx.core.model.CustomerSignatureGateway;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserEventContractSign;
import com.us.weavx.core.model.SignatureGateway;
import com.us.weavx.core.util.SystemSettingsManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Service("signatureTxServices")
public class SignatureServicesTx {

    @Autowired
    private SignatureServicesTxDAO dao;

    @Autowired
    private SystemSettingsManager settingsManager;


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public SignatureGateway findSignatureGatewayById(int id) {
		return dao.findSignatureGatewayById(id);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<SignatureGateway> listAllSignatureGateways() {
		return dao.listAllSignatureGateways();
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerSignatureGateway findCustomerSignatureGatewayByCustomerIdAndGatewayId(long customerId, int signatureGatewayId) {
		return dao.findCustomerSignatureGatewayByCustomerIdAndGatewayId(customerId, signatureGatewayId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerSignatureGateway findCustomerSignatureGatewayByCustomerId(long customerId) {
		return dao.findCustomerSignatureGatewayByCustomerId(customerId);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public CustomerUserEventContractSign addCustomerUserEventContractSign(final CustomerUserEventContractSign item) {
		return dao.addCustomerUserEventContractSign(item);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerUserEventContractSign findCustomerUserEventContractSignById(long id) {
		return dao.findCustomerUserEventContractSignById(id);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerUserEventContractSign findCustomerUserEventContractSignByCustomerUserIdAndAppId(long customerUserId, long applicationId) {
		return dao.findCustomerUserEventContractSignByCustomerUserIdAndAppId(customerUserId, applicationId);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public CustomerUserEventContractSign updateCustomerUserEventContractSign(CustomerUserEventContractSign item)  {
		return dao.updateCustomerUserEventContractSign(item);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public CustomerUserEventContractSign updateCustomerUserEventContractSignByCustomerUserIdAndApplication(CustomerUserEventContractSign item)  {
		return dao.updateCustomerUserEventContractSignByCustomerUserIdAndApplication(item);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public CustomerUserEventContractSign generateSignatureUrl(CustomerUser cu, long applicationId, int signatureGatewayId, String templateId, long transactionId)  {
		try {
			SignatureGateway signGw = findSignatureGatewayById(signatureGatewayId);
			CustomerSignatureGateway custSignGw = findCustomerSignatureGatewayByCustomerIdAndGatewayId(cu.getCustomerId(), signatureGatewayId);
			HashMap<String, Object> request = new HashMap<>();
			request.put("sandBoxURL", signGw.getSandboxUrl());
			request.put("signatureApiParam1", custSignGw.getSignatureApiParam1());
			request.put("signatureApiParam2", custSignGw.getSignatureApiParam2());
			request.put("signatureApiParam3", custSignGw.getSignatureApiParam3());
			request.put("signatureApiParam4", custSignGw.getSignatureApiParam4());
			request.put("templateId", templateId);
			request.put("documentName", "sdi-"+cu.getCustomerId()+"-"+applicationId+"-"+cu.getId()+"-"+cu.getFirstName()+"-"+cu.getLastName()+"-contract");
			request.put("fullname", cu.getFirstName()+" "+cu.getLastName());
			request.put("address", cu.getAddress());
			RestTemplate template = new RestTemplate();
			HashMap<String, Object> response = template.postForObject(settingsManager.getSystemProperty("SIGN_SERVICE_URL"), request, HashMap.class);
			String signatureUrl = (String) response.get("signatureUrl");
			CustomerUserEventContractSign customerUserEventContractSign = new CustomerUserEventContractSign();
			customerUserEventContractSign.setCustomerUserId(cu.getId());
			customerUserEventContractSign.setApplicationId(applicationId);
			customerUserEventContractSign.setSignatureUrl(signatureUrl);
			customerUserEventContractSign.setSignatureStatus(false);
			customerUserEventContractSign.setTransactionId(transactionId);
			customerUserEventContractSign = dao.addCustomerUserEventContractSign(customerUserEventContractSign);
			return customerUserEventContractSign;		
		} catch (Exception e) {
			System.out.println("******ERROR FIRMA*****: "+e);
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public CustomerUserEventContractSign generateUserContractURL(CustomerUser cu, long applicationId, int signatureGatewayId, String templateId, long transactionId)  {
		try {
			String url = "doc-"+cu.getCustomerId()+"-"+applicationId+"-"+cu.getId()+"-"+RandomStringUtils.random(6,true,true);
			CustomerUserEventContractSign customerUserEventContractSign = new CustomerUserEventContractSign();
			customerUserEventContractSign.setCustomerUserId(cu.getId());
			customerUserEventContractSign.setApplicationId(applicationId);
			customerUserEventContractSign.setSignatureUrl(url);
			customerUserEventContractSign.setSignatureStatus(false);
			customerUserEventContractSign.setTransactionId(transactionId);
			customerUserEventContractSign = dao.addCustomerUserEventContractSign(customerUserEventContractSign);
			return customerUserEventContractSign;		
		} catch (Exception e) {
			System.out.println("******ERROR FIRMA*****: "+e);
			return null;
		}
	}
	

}
