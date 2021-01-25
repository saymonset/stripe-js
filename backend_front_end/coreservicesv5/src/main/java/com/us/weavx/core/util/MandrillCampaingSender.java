package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cribbstechnologies.clients.mandrill.model.MandrillHtmlMessage;
import com.cribbstechnologies.clients.mandrill.model.MandrillMessageRequest;
import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.cribbstechnologies.clients.mandrill.model.response.message.SendMessageResponse;
import com.cribbstechnologies.clients.mandrill.request.MandrillMessagesRequest;
import com.cribbstechnologies.clients.mandrill.request.MandrillRESTRequest;
import com.cribbstechnologies.clients.mandrill.util.MandrillConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.us.weavx.core.exception.MandrillCampaingSenderGeneralException;
import com.us.weavx.core.exception.SendMandrillEmailException;
import com.us.weavx.core.model.CustomerUserPurchaseInfo;
import com.us.weavx.core.model.EmailRecipient;
import com.us.weavx.core.model.EmailType;
import com.us.weavx.core.services.tx.UtilTxServices;
@Component
public class MandrillCampaingSender {
	@Autowired
	private UtilTxServices utilServices;

	private static MandrillCampaingSender instance;
	private String apiKey;
	private String apiVersion;
	private String apiBaseURL;
	private MandrillConfiguration mandrilConfiguration;
	public static final String API_KEY = "apiKey";
	public static final String API_VERSION = "apiVersion";
	public static final String API_BASE_URL = "apiBaseURL";
	public static final String FROM_EMAIL = "fromEmail";
	public static final String FROM_NAME = "fromName";
	public static final String TEMPLATE_URL = "templateURL";
	public static final String FILLING_CONTENT = "fillingContent";
	
	
	private MandrillCampaingSender(Map<String, Object> initializationParameters) throws MandrillCampaingSenderGeneralException {
		try {
			this.apiKey = (String) initializationParameters.get(MandrillCampaingSender.API_KEY);
			this.apiVersion = (String) initializationParameters.get(MandrillCampaingSender.API_VERSION);
			this.apiBaseURL = (String) initializationParameters.get(MandrillCampaingSender.API_BASE_URL);
			this.mandrilConfiguration = new MandrillConfiguration();
			this.mandrilConfiguration.setApiKey(apiKey);
			this.mandrilConfiguration.setApiVersion(apiVersion);
			this.mandrilConfiguration.setBaseURL(apiBaseURL);
		} catch (Exception e) {
			throw new MandrillCampaingSenderGeneralException(e);
		}
		
	}
	
	public static MandrillCampaingSender getInstance(Map<String, Object> initializationParameters) throws MandrillCampaingSenderGeneralException {
		if (instance == null) {
			instance = new MandrillCampaingSender(initializationParameters);
		}
		return instance;
	}
	
	private void sendEmail(String from, String fromName, String subject, String htmlTemplate, Map<String, String> fillingContent, MandrillRecipient[] recipients) throws SendMandrillEmailException {
		MandrillRESTRequest request;
		ObjectMapper mapper;
		MandrillMessagesRequest messagesRequest;
		MandrillMessageRequest messageReq;
		HttpClient httpClient;
		try {
			//htmlTemplate = "c:/"+htmlTemplate;
			mapper = new ObjectMapper();
			request = new MandrillRESTRequest();
			request.setConfig(mandrilConfiguration);
			request.setObjectMapper(mapper);
			messagesRequest = new MandrillMessagesRequest();
			messagesRequest.setRequest(request);
			httpClient = new DefaultHttpClient();
			request.setHttpClient(httpClient);
			MandrillHtmlMessage message = new MandrillHtmlMessage();
			message.setFrom_email(from);
			message.setFrom_name(fromName);
			Map<String, String> headers = new HashMap<>();
			message.setHeaders(headers);
			String filledHtmlTemplate = GeneralUtilities.geTemplateFileContentFilled(htmlTemplate, fillingContent);
			message.setHtml(filledHtmlTemplate);
			message.setSubject(subject);
			message.setTo(recipients);
			message.setTrack_clicks(true);
			message.setTrack_opens(true);
			String[] tags = new String[] {};
			message.setTags(tags);
			messageReq = new MandrillMessageRequest();
			messageReq.setMessage(message);
			SendMessageResponse response = messagesRequest.sendMessage(messageReq);
		} catch (Throwable e) {
			throw new SendMandrillEmailException(e);
		} 
	}
	
	public ArrayList<String> sendEmailCampaing(List<CustomerUserPurchaseInfo> recipients, String from, String fromName, String subject, String templateUrl, Map<String, Object> aditionalInfo) {
		try {
			ArrayList<String> failedEmails = new ArrayList<>();
			int emailCount = 0;
			for (CustomerUserPurchaseInfo cu : recipients) {
				HashMap<String, String> fillingContent = new HashMap<>();
				fillingContent.put("firstName", cu.getFirstName());
				fillingContent.put("lastName", cu.getLastName());
				fillingContent.put("amount", new Double(cu.getAmount()).toString());
				fillingContent.put("purpose", cu.getPurposeName());
				fillingContent.putAll((Map<String, String>) aditionalInfo.get(MandrillCampaingSender.FILLING_CONTENT));
				try {
					List<EmailRecipient> recipientsList = new ArrayList<>();
					EmailRecipient recipient = new EmailRecipient();
					recipient.setName(cu.getFirstName()+" "+cu.getLastName());
					recipient.setEmail(cu.getEmail());
					recipientsList.add(recipient);
					utilServices.sendEmailWithManager(from, fromName, subject, templateUrl, recipientsList, new ArrayList<>(), fillingContent, EmailType.CAMPAING_EMAIL);
					//sendEmail(from, fromName, subject, templateUrl, fillingContent, new MandrillRecipient[] {new MandrillRecipient(cu.getFirstName()+" "+cu.getLastName(),cu.getEmail())});
					emailCount++;
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						
					}
				} catch (Exception e) {
					failedEmails.add(cu.getEmail());
					e.printStackTrace();
				}
			}
			return failedEmails;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

}
