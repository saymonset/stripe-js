package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.EmailAgent;
import com.us.weavx.core.model.EmailAgentHist;
import com.us.weavx.core.model.EmailType;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UtilTxServices;
import com.us.weavx.core.util.SystemSettingsManager;
@Component
public class RegisterEmailHealthCheckMethodImpl implements ServiceMethod {
	@Autowired
	private TransactionTxServices services;
	@Autowired
	private UtilTxServices utilServices;
	@Autowired
	private SystemSettingsManager settings;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			HashMap<String, Object> healthCheckRequest = (HashMap<String, Object>) params.get("healthCheckRequest");
			long customerId = aInfo.getCustomerId();
			int healthyChecksCount = Integer.parseInt(settings.getSystemProperty("HEALTHY_CHECKS_COUNT"));
	    	int unhealthyCheckCount = Integer.parseInt(settings.getSystemProperty("UNHEALTHY_CHECKS_COUNT"));
	    	long maxHealthyResponseTime = Long.parseLong(settings.getSystemProperty("MAX_HEALTHY_RESPONSE_TIME"));
			EmailAgentHist emailAgentHist = new EmailAgentHist();
			int emailAgentId = ((Number) healthCheckRequest.get("emailAgentId")).intValue();
			emailAgentHist.setEmailAgentId(((Number) healthCheckRequest.get("emailAgentId")).intValue());
			long responseTime = ((Number) healthCheckRequest.get("responseTime")).longValue();
			emailAgentHist.setAvgResponseTime(responseTime);
			emailAgentHist.setMinResponseTime(responseTime);
			emailAgentHist.setMaxResponseTime(responseTime);
			emailAgentHist.setEmailType(EmailType.HEALTH_CHECK);
			services.registerNewEmailAgentHist(emailAgentHist);
			EmailAgent currentEmailAgent = services.findEmailAgentById(emailAgentId);
			if (currentEmailAgent == null) {
				throw new Exception("Invalid emailAgentId: "+emailAgentId);
			}
			//Analizar el resultado del env�o
	    	int actualHealthyChecks = currentEmailAgent.getHealthyChecks();
	    	if (emailAgentHist.getAvgResponseTime() > maxHealthyResponseTime && currentEmailAgent.isHealthy()) {
	    		//Envio Unhealthy
	    		actualHealthyChecks = (actualHealthyChecks > 0)?0:actualHealthyChecks;
	    		actualHealthyChecks--;
	    		if (actualHealthyChecks == unhealthyCheckCount) {
	    			//Se debe marcar el agente como UNHEALTHY
	    			currentEmailAgent.setIsHealthy(false);
	    			utilServices.sendAlertEmail("UNHEALTHY AGENT -> "+currentEmailAgent.toString());
	    		}
	    	} else {
	    		//Envio Healthy
	    		actualHealthyChecks = (actualHealthyChecks < 0)?0:actualHealthyChecks;
	    		actualHealthyChecks++;
	    		if (actualHealthyChecks == healthyChecksCount && !currentEmailAgent.isHealthy()) {
	    			currentEmailAgent.setIsHealthy(true);
	    			utilServices.sendAlertEmail("HEALTHY AGENT -> "+currentEmailAgent.toString());
	    		}
	    	}
	    	currentEmailAgent.setHealthyChecks(actualHealthyChecks);
	    	services.updateEmailAgent(currentEmailAgent);
	    	Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
			return res;
		}
	}

	
	

}
