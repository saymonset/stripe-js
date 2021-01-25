
package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.EmailRecipient;
import com.us.weavx.core.model.EmailType;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.services.tx.UtilTxServices;
@Component
public class SendNotificationEmailMethodImpl implements ServiceMethod {
	@Autowired
	private ConfigurationTxServices configurationServices;
	@Autowired
	private UtilTxServices utilServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long applicationId = aInfo.getApplicationId();
			Application app = configurationServices.findApplicationInfo(applicationId);
			if (app.getName().indexOf("NotificationSender") == -1) {
				throw new Exception("Invalid access Key for sending notifications.");
			}
			String template = (String) params.get("template");
			String subject = (String) params.get("subject");
			String fromName = (String) params.get("fromName");
			String fromAddress = (String) params.get("fromAddress");
			String recipient = (String) params.get("recipient");
			String recipientName = (String) params.get("recipientName");
			List<EmailRecipient> recipients = new ArrayList<>();
			EmailRecipient emailRec = new EmailRecipient();
			emailRec.setType(EmailRecipient.TO);
			emailRec.setEmail(recipient);
			emailRec.setName(recipientName);
			emailRec.setRecipientInfo(new HashMap<>());
			recipients.add(emailRec);
			String managementAddress = (String) params.get("managementEmail");
			if (managementAddress != null) {
				EmailRecipient managementRecipient = new EmailRecipient();
				managementRecipient.setType(EmailRecipient.BCC);
				managementRecipient.setName("");
				managementRecipient.setEmail(managementAddress);
				managementRecipient.setRecipientInfo(new HashMap<>());
				recipients.add(managementRecipient);
			}
			Map<String, String> parameters = (HashMap<String, String>) params.get("contentVars");
			utilServices.sendEmailWithManager(fromAddress, fromName, subject, template, recipients, new ArrayList<>(), parameters, EmailType.NOTIFICATION_EMAIL);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e.getMessage());
			return res;
		}
	}

	
	

}