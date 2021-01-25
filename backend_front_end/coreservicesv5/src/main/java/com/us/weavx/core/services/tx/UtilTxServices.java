package com.us.weavx.core.services.tx;

import com.us.weavx.core.exception.AlreadyUsedValidationCodeException;
import com.us.weavx.core.exception.EmailSendingGeneralException;
import com.us.weavx.core.exception.ExpiredValidationCodeException;
import com.us.weavx.core.exception.MaxValidationCodeTriesExceededException;
import com.us.weavx.core.exception.RequiredApplicationParameterNotFoundException;
import com.us.weavx.core.exception.UnknownCustomerPropertyException;
import com.us.weavx.core.exception.UnknownNotificationTemplateException;
import com.us.weavx.core.exception.UnsupportedDeviceException;
import com.us.weavx.core.model.Attachment;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.CustomerUserPurchaseInfo;
import com.us.weavx.core.model.EmailAgent;
import com.us.weavx.core.model.EmailAgentHist;
import com.us.weavx.core.model.EmailRecipient;
import com.us.weavx.core.model.EmailType;
import com.us.weavx.core.model.FailedRecipientInfo;
import com.us.weavx.core.model.PaymentResult;
import com.us.weavx.core.model.SendEmailRequest;
import com.us.weavx.core.model.SendEmailResponse;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.model.ValidationCode;
import com.us.weavx.core.util.ApplicationParametersManager;
import com.us.weavx.core.util.CustomerPropertyManager;
import com.us.weavx.core.util.DeviceTypeSelector;
import com.us.weavx.core.util.GeneralUtilities;
import com.us.weavx.core.util.MandrillCampaingSender;
import com.us.weavx.core.util.OTPGeneratorUtil;
import com.us.weavx.core.util.SystemSettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Service("utilTxServices")
public class UtilTxServices {

    @Autowired
    private TransactionTxServices transactionServices;

    @Autowired
    private ConfigurationTxServices confServices;

    @Autowired
    private UserTxServices userServices;

    @Autowired
    private ApplicationParametersManager appParamManager;

    @Autowired
    private CustomerPropertyManager custPropManager;

    @Autowired
    private SystemSettingsManager settings;


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public ArrayList<String> sendMandrillEventCampaing(long customerId, long applicationId) throws EmailSendingGeneralException {
        try {
            ArrayList<String> result = new ArrayList<>();
            //Spring context
            //Lectura de parametros de la app
            String eventStartDate = appParamManager.getApplicationParameter(applicationId, "EVENT_START_DATE");
            String eventEndDate = appParamManager.getApplicationParameter(applicationId, "EVENT_FINISH_DATE");
            String mandrillApiKey = appParamManager.getApplicationParameter(applicationId, "MANDRILL_API_KEY");
            String mandrillApiVersion = appParamManager.getApplicationParameter(applicationId, "MANDRILL_API_VERSION");
            String mandrillApiBaseURL = appParamManager.getApplicationParameter(applicationId, "MANDRILL_API_BASE_URL");
            HashMap<String, Object> aditionalInfo = new HashMap<>();
            aditionalInfo.put(MandrillCampaingSender.API_KEY, mandrillApiKey);
            aditionalInfo.put(MandrillCampaingSender.API_VERSION, mandrillApiVersion);
            aditionalInfo.put(MandrillCampaingSender.API_BASE_URL, mandrillApiBaseURL);
            List<CustomerUserPurchaseInfo> customers = transactionServices.findCustomerPurchasaeInfoForSuccessLiveEventPurchases(customerId, applicationId);
            for (CustomerUserPurchaseInfo cu : customers) {
                try {
                    HashMap<String, String> fillingData = new HashMap<>();
                    String eventTitle = custPropManager.findCustomerProperty(customerId, cu.getLangId(), "EVENT_TITLE");
                    String from = custPropManager.findCustomerProperty(customerId, cu.getLangId(), "MANDRILL_EMAIL_FROM");
                    String fromName = custPropManager.findCustomerProperty(customerId, cu.getLangId(), "MANDRILL_EMAIL_FROM_NAME");
                    String subject = custPropManager.findCustomerProperty(customerId, cu.getLangId(), "MANDRILL_EMAIL_SUBJECT");
                    String templateFile = custPropManager.findCustomerProperty(customerId, cu.getLangId(), "MANDRILL_EMAIL_TEMPLATE");
                    String url = custPropManager.findCustomerProperty(customerId, cu.getLangId(), "MANDRILL_EMAIL_URL");
                    fillingData.put("eventTitle", eventTitle);
                    fillingData.put("eventStartDate", eventStartDate);
                    fillingData.put("eventFinishDate", eventEndDate);
                    userServices.closeAllCustomerUserSessions(cu.getCustomerUserId());
                    AuthenticatedUserInfo userInfo = userServices.authenticateCampaingUser(cu.getCustomerUserId(), customerId, applicationId, "N/A", "N/A");
                    MandrillCampaingSender sender = MandrillCampaingSender.getInstance(aditionalInfo);
                    String userAccessToken = userInfo.getUserAccessToken().getToken();
                    StringBuilder sB = new StringBuilder(userAccessToken);
                    sB.append("?l=").append(cu.getLangId());
                    userAccessToken = sB.toString();
                    fillingData.put("TOKEN", userAccessToken);
                    fillingData.put("URL", url);
                    aditionalInfo.put(MandrillCampaingSender.FILLING_CONTENT, fillingData);
                    ArrayList<CustomerUserPurchaseInfo> dests = new ArrayList<>();
                    dests.add(cu);

                    ArrayList<String> tmpArr = sender.sendEmailCampaing(dests, from, fromName, subject, templateFile, aditionalInfo);
                    if (tmpArr != null) {
                        result.addAll(tmpArr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        } catch (Exception e) {
            throw new EmailSendingGeneralException();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public SendEmailResponse sendEmailSOS(SendEmailRequest emailRequest) throws UnknownNotificationTemplateException, EmailSendingGeneralException {
        Transport transport = null;
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            Session mailSession = Session.getDefaultInstance(props);
            transport = mailSession.getTransport();
            String host = emailRequest.getSettings().get("HOST");
            String user = emailRequest.getSettings().get("SES_USER");
            String password = emailRequest.getSettings().get("SES_PASSWORD");
            transport.connect(host, user, password);
            //Se itera y se envian los correos uno a uno
            List<EmailRecipient> emailRecipients = emailRequest.getRecipients();
            long minResponseTime = Long.MAX_VALUE;
            long totalResponseTime = 0;
            long countRecipients = 0;
            long maxResponseTime = 0;
            List<FailedRecipientInfo> failedRecipients = new ArrayList<>();
            for (EmailRecipient recipient : emailRecipients) {
                try {
                    MimeMessage msg = new MimeMessage(mailSession);
                    msg.setSubject(emailRequest.getSubject());
                    msg.setFrom(new InternetAddress(emailRequest.getFromEmail(), emailRequest.getFromName()));
                    switch (recipient.getType()) {
                        case EmailRecipient.TO:
                            msg.addRecipient(RecipientType.TO, new InternetAddress(recipient.getEmail(), recipient.getName()));
                            break;
                        case EmailRecipient.CC:
                            msg.addRecipient(RecipientType.CC, new InternetAddress(recipient.getEmail(), recipient.getName()));
                            break;
                        case EmailRecipient.BCC:
                            msg.addRecipient(RecipientType.BCC, new InternetAddress(recipient.getEmail(), recipient.getName()));
                            break;
                        default:
                            msg.addRecipient(RecipientType.TO, new InternetAddress(recipient.getEmail(), recipient.getName()));
                    }
                    Map<String, String> data = new HashMap<>();
                    if (emailRequest.getFillInfo() != null)
                        data.putAll(emailRequest.getFillInfo());
                    if (recipient.getRecipientInfo() != null)
                        data.putAll(recipient.getRecipientInfo());
                    msg.setContent(fillEmailContent(emailRequest.getContent(), data), "text/html; charset=utf-8");
                    long startTime = System.currentTimeMillis();
                    transport.sendMessage(msg, msg.getAllRecipients());
                    long executionTime = System.currentTimeMillis() - startTime;
                    if (executionTime > maxResponseTime) {
                        maxResponseTime = executionTime;
                    }
                    if (executionTime < minResponseTime) {
                        minResponseTime = executionTime;
                    }
                    totalResponseTime += executionTime;
                    countRecipients++;
                } catch (Throwable t) {
                    FailedRecipientInfo failedTmp = new FailedRecipientInfo();
                    failedTmp.setEmail(recipient.getEmail());
                    failedTmp.setName(recipient.getName());
                    failedTmp.setErrorMessage(t.getMessage());
                    failedRecipients.add(failedTmp);
                }
            }
            BigDecimal numerador = new BigDecimal(totalResponseTime);
            BigDecimal denominador = new BigDecimal(countRecipients);
            double averageResponseTime = numerador.divide(denominador).doubleValue();
            SendEmailResponse response = new SendEmailResponse();
            response.setAverageSendingTime(averageResponseTime);
            response.setMinSendingTime(minResponseTime);
            response.setMaxSendingTime(maxResponseTime);
            response.setFailedRecipients(failedRecipients);
            response.setResponseCode(0);
            response.setMessage("Success");
            return response;
        } catch (Exception e) {
            SendEmailResponse response = new SendEmailResponse();
            response.setResponseCode(-1);
            response.setMessage(e.getMessage());
            return response;
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private String fillEmailContent(String content, Map<String, String> data) {
        String contentTmp = content;
        Set<String> parameterNames = data.keySet();
        for (String parameter : parameterNames) {
            contentTmp = contentTmp.replace("${" + parameter + "}", data.get(parameter));
        }
        return contentTmp;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void sendEmailWithManager(String from, String fromName, String subject, String templateFile, List<EmailRecipient> recipients, List<Attachment> attachments, Map<String, String> parameters, int emailType) throws UnknownNotificationTemplateException, EmailSendingGeneralException {
        try {
            SendEmailRequest request = new SendEmailRequest();
            //templateFile = "c:/"+templateFile;
            request.setFromEmail(from);
            request.setFromName(fromName);
            request.setRecipients(recipients);
            String emailContent = GeneralUtilities.geTemplateFileContentFilled(templateFile, new HashMap<>());
            request.setContent(emailContent);
            String host = settings.getSystemProperty("SMTP_HOST");
            String user = settings.getSystemProperty("SES_USER");
            String password = settings.getSystemProperty("SES_PASSWORD");
            int healthyChecksCount = Integer.parseInt(settings.getSystemProperty("HEALTHY_CHECKS_COUNT"));
            int unhealthyCheckCount = Integer.parseInt(settings.getSystemProperty("UNHEALTHY_CHECKS_COUNT"));
            long maxHealthyResponseTime = Long.parseLong(settings.getSystemProperty("MAX_HEALTHY_RESPONSE_TIME"));
            Map<String, String> settingMap = new HashMap<>();
            settingMap.put("HOST", host);
            settingMap.put("SES_USER", user);
            settingMap.put("SES_PASSWORD", password);
            request.setSettings(settingMap);
            request.setFillInfo(parameters);
            request.setSubject(subject);
            request.setAttachments(attachments);
            //Se obtiene la configuracion de los agentes
            List<EmailAgent> agents = transactionServices.listAllEmailAgents();
            EmailAgent selectedEmailAgent = selectBestEmailAgent(agents);
            if (selectedEmailAgent == null) {
                //NO hay agentes HEALTHY por lo cual se intenta enviar localmente.
                SendEmailResponse resp = sendEmailSOS(request);
                sendAlertEmail("URGENT: NO HEALTHY AGENTS FOUND USED LOCAL COMPONENT SOS ESTATUS: " + resp);
            } else {
                String url = selectedEmailAgent.getUrl();
                RestTemplate template = new RestTemplate();
                SendEmailResponse resp = null;
                boolean failedAgent = false;
                try {
                    resp = template.postForObject(url, request, SendEmailResponse.class, new HashMap<>());
                } catch (Throwable e) {
                    failedAgent = true;
                }
                int actualHealthyChecks = selectedEmailAgent.getHealthyChecks();
                if (!failedAgent) {
                    EmailAgentHist emailAgentHist = new EmailAgentHist();
                    emailAgentHist.setEmailAgentId(selectedEmailAgent.getId());
                    emailAgentHist.setMinResponseTime(resp.getMinSendingTime());
                    emailAgentHist.setMaxResponseTime(resp.getMaxSendingTime());
                    emailAgentHist.setAvgResponseTime(resp.getAverageSendingTime());
                    emailAgentHist.setEmailType(emailType);
                    try {
                        transactionServices.registerNewEmailAgentHist(emailAgentHist);
                    } catch (Exception e) {
                        //IGNORE
                    }
                    //Analizar el resultado del env�o
                    if (emailAgentHist.getAvgResponseTime() > maxHealthyResponseTime && selectedEmailAgent.isHealthy()) {
                        //Envio Unhealthy
                        actualHealthyChecks = (actualHealthyChecks > 0) ? 0 : actualHealthyChecks;
                        actualHealthyChecks--;
                        if (actualHealthyChecks == unhealthyCheckCount) {
                            //Se debe marcar el agente como UNHEALTHY
                            selectedEmailAgent.setIsHealthy(false);
                            sendAlertEmail("UNHEALTHY AGENT -> " + selectedEmailAgent.toString());
                        }
                    } else {
                        //Envio Healthy
                        actualHealthyChecks = (actualHealthyChecks < 0) ? 0 : actualHealthyChecks;
                        actualHealthyChecks++;
                        if (actualHealthyChecks == healthyChecksCount && !selectedEmailAgent.isHealthy()) {
                            selectedEmailAgent.setIsHealthy(true);
                            sendAlertEmail("HEALTHY AGENT -> " + selectedEmailAgent.toString());
                        }
                    }
                } else {
                    //FailedAgent
                    actualHealthyChecks = unhealthyCheckCount;
                    selectedEmailAgent.setIsHealthy(false);
                    sendAlertEmail("UNHEALTHY AGENT (FAIL) -> " + selectedEmailAgent.toString());
                }
                selectedEmailAgent.setHealthyChecks(actualHealthyChecks);
                try {
                    transactionServices.updateEmailAgent(selectedEmailAgent);
                } catch (Exception e) {
                    //IGNORE
                }
                if (failedAgent) {
                    throw new Exception("Error enviando email con agente: " + selectedEmailAgent.getName());
                }
            }
        } catch (Exception e) {
            throw new EmailSendingGeneralException(e);
        }
    }

    private EmailAgent selectBestEmailAgent(List<EmailAgent> agents) {
        EmailAgent result = null;
        for (EmailAgent tmp : agents) {
            if (tmp.isHealthy()) {
                if (result == null) {
                    result = tmp;
                } else {
                    if (tmp.getHealthyDate().before(result.getHealthyDate())) {
                        result = tmp;
                    }
                }
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void sendAlertEmail(String alertText) {
        try {
            //templateFile = "c:/"+templateFile;
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            Session session = Session.getDefaultInstance(props);
            MimeMessage msg = new MimeMessage(session);
            String fromAddress = settings.getSystemProperty("ALERT_EMAIL_FROM");
            InternetAddress fromAd = new InternetAddress(fromAddress);
            String alertRecipients = settings.getSystemProperty("ALERT_EMAIL_RECIPIENTS");
            String alertSubject = settings.getSystemProperty("ALERT_EMAIL_SUBJECT");
            String recipients[] = alertRecipients.split(";");
            msg.setFrom(fromAd);
            for (String email : recipients) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
            msg.setSubject(alertSubject);
            msg.setContent(alertText, "text/html; charset=utf-8");
            Transport transport = session.getTransport();
            String host = settings.getSystemProperty("SMTP_HOST");
            String user = settings.getSystemProperty("SES_USER");
            String password = settings.getSystemProperty("SES_PASSWORD");
            transport.connect(host, user, password);
            long beforeTime = System.currentTimeMillis();
            transport.sendMessage(msg, msg.getAllRecipients());
            long sendingTime = System.currentTimeMillis() - beforeTime;
            EmailAgentHist emailAgentHist = new EmailAgentHist();
            emailAgentHist.setEmailAgentId(0);
            emailAgentHist.setMinResponseTime(sendingTime);
            emailAgentHist.setMaxResponseTime(sendingTime);
            emailAgentHist.setAvgResponseTime(sendingTime);
            emailAgentHist.setEmailType(EmailType.ALERT_EMAIL);
            try {
                transactionServices.registerNewEmailAgentHist(emailAgentHist);
            } catch (Exception e) {
                //IGNORE
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void sendAlertEmailRegisterCustomer(String mensaje, String email) {
        try {
            //templateFile = "c:/"+templateFile;
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            Session session = Session.getDefaultInstance(props);
            MimeMessage msg = new MimeMessage(session);
            String fromAddress = settings.getSystemProperty("ALERT_EMAIL_FROM");
            InternetAddress fromAd = new InternetAddress(fromAddress);
            String alertSubject = "Notificacion de registro AddAttendees - Carga Masiva";
            msg.setFrom(fromAd);
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.setSubject(alertSubject);
            msg.setContent(mensaje, "text/html; charset=utf-8");
            Transport transport = session.getTransport();
            String host = settings.getSystemProperty("SMTP_HOST");
            String user = settings.getSystemProperty("SES_USER");
            String password = settings.getSystemProperty("SES_PASSWORD");
            transport.connect(host, user, password);
            long beforeTime = System.currentTimeMillis();
            transport.sendMessage(msg, msg.getAllRecipients());
            long sendingTime = System.currentTimeMillis() - beforeTime;
            EmailAgentHist emailAgentHist = new EmailAgentHist();
            emailAgentHist.setEmailAgentId(0);
            emailAgentHist.setMinResponseTime(sendingTime);
            emailAgentHist.setMaxResponseTime(sendingTime);
            emailAgentHist.setAvgResponseTime(sendingTime);
            emailAgentHist.setEmailType(EmailType.ALERT_EMAIL);
            try {
                transactionServices.registerNewEmailAgentHist(emailAgentHist);
            } catch (Exception e) {
                //IGNORE
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ValidationCode validateDevice(String device, int deviceType, String userName, long customerId, long applicationId, int langId) throws UnsupportedDeviceException {
        switch (deviceType) {
            case DeviceTypeSelector.EMAIL:
                try {
                    device = device.toLowerCase();
                    String tmp = appParamManager.getApplicationParameter(applicationId, "OTP_EXPIRATION_TIME");
                    if (tmp == null) {
                        throw new RequiredApplicationParameterNotFoundException("parameter OTP_EXPIRATION_TIME is required.");
                    }
                    int expirationTime = Integer.parseInt(tmp);
                    String template = custPropManager.findCustomerProperty(customerId, langId, "OTP_EMAIL_TEMPLATE");
                    String subject = custPropManager.findCustomerProperty(customerId, langId, "OTP_EMAIL_SUBJECT");
                    int emailType = EmailType.NOTIFICATION_EMAIL;
                    String fromName = custPropManager.findCustomerProperty(customerId, langId, "OTP_EMAIL_FROM_NAME");
                    String from = custPropManager.findCustomerProperty(customerId, langId, "OTP_EMAIL_FROM");
                    HashMap<String, String> parameters = new HashMap<>();
                    String otp = OTPGeneratorUtil.generateOTP();
                    subject = subject.replaceAll(".nombre.", userName);
                    subject = subject.replaceAll(".otpcode.", otp);
                    ValidationCode code = new ValidationCode();
                    code.setCode(otp);
                    code.setDeviceTypeId(deviceType);
                    code.setDevice(device);
                    LocalDateTime time = LocalDateTime.now().plus(Duration.of(expirationTime, ChronoUnit.MILLIS));
                    code.setExpiresAt(Timestamp.valueOf(time));
                    code = userServices.registerNewValidationCode(code);
                    ArrayList<EmailRecipient> dests = new ArrayList<>();
                    EmailRecipient tmpEMail = new EmailRecipient();
                    tmpEMail.setEmail(device);
                    tmpEMail.setName(userName);
                    Map<String, String> recipientInfo = new HashMap<>();
                    recipientInfo.put("Name", userName);
                    recipientInfo.put("OTP", otp);
                    recipientInfo.put("Lastname", "");
                    parameters.put("Name", userName);
                    parameters.put("OTP", otp);
                    parameters.put("Lastname", "");
                    tmpEMail.setRecipientInfo(recipientInfo);
                    dests.add(tmpEMail);
                    sendEmailWithManager(from, fromName, subject, template, dests, new ArrayList<>(), parameters, emailType);
                    code.setCode("****");

                    return code;
                } catch (UnknownCustomerPropertyException e) {
                    throw new RuntimeException(e);
                } catch (UnknownNotificationTemplateException e) {
                    throw new RuntimeException(e);
                } catch (EmailSendingGeneralException e) {
                    throw new RuntimeException(e);
                } catch (RequiredApplicationParameterNotFoundException e) {
                    throw new RuntimeException(e);
                }
            default:
                throw new UnsupportedDeviceException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean validateDeviceCode(long validationCodeId, String deviceCode, long customerId, long applicationId) throws UnsupportedDeviceException, ExpiredValidationCodeException, AlreadyUsedValidationCodeException, RequiredApplicationParameterNotFoundException, MaxValidationCodeTriesExceededException {
        ValidationCode storedCode = userServices.findValidationCodeById(validationCodeId);
        //Se valida el estado del codigo
        if (storedCode.isExpired()) {
            throw new ExpiredValidationCodeException();
        } else if (storedCode.isUsed()) {
            throw new AlreadyUsedValidationCodeException();
        } else if (storedCode.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
            storedCode.setExpired(true);
            userServices.updateValidationCode(storedCode);
            throw new ExpiredValidationCodeException();
        } else {
            String tmp = appParamManager.getApplicationParameter(applicationId, "MAX_OTP_TRIES");
            if (tmp == null) {
                throw new RequiredApplicationParameterNotFoundException("parameter MAX_OTP_TRIES is required.");
            }
            int maxTries = Integer.parseInt(tmp);
            if (storedCode.getTryCount() >= maxTries) {
                throw new MaxValidationCodeTriesExceededException();
            } else {
                storedCode.setTryCount(storedCode.getTryCount() + 1);
                if (storedCode.getCode().equals(deviceCode)) {
                    storedCode.setUsed(true);
                    userServices.updateValidationCode(storedCode);
                    return true;
                } else {
                    userServices.updateValidationCode(storedCode);
                    return false;
                }
            }
        }
    }

	public ResponseEntity<String> registrarUsuaruioEnMailChimp(long applicationId ,TransactionUserData userData) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
		String tags = appParamManager.getApplicationParameter(applicationId, "EVENT_TAG_NAME") != null ? appParamManager.getApplicationParameter(applicationId, "EVENT_TAG_NAME") : null; 
		map.add("auth_token", settings.getSystemProperty("MAILCHIMP_AUTH_TOKEN"));
		map.add("list_id", appParamManager.getApplicationParameter(applicationId, "MAILCHIMP_LIST_ID"));
		map.add("api_key", appParamManager.getApplicationParameter(applicationId, "MAILCHIMP_KEY"));
		map.add("server", settings.getSystemProperty("MAILCHIMP_SERVER"));
		map.add("tags", tags);
		map.add("email", userData.getEmail());
		map.add("merge_field_key[0]", "FNAME");
		map.add("merge_field_value[0]", userData.getName());
		map.add("merge_field_key[1]", "LNAME");
		map.add("merge_field_value[1]", userData.getLastname());
		HttpEntity<MultiValueMap<String, Object>> requestMailchimp = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(settings.getSystemProperty("MAILCHIMP_ENDPOINT"), requestMailchimp , String.class );
		return response;
	}

}
