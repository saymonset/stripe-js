package com.us.weavx.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.us.weavx.core.exception.UnknownNotificationTemplateException;
import com.us.weavx.core.model.BulkTransactionVerificationResultItem;
import com.us.weavx.core.model.PaymentResult;
import com.us.weavx.core.model.Response;

public class GeneralUtilities {
	
	public static Date parseDate(String dateStr) {
		SimpleDateFormat sDF = (SimpleDateFormat) SimpleDateFormat.getInstance();
		sDF.applyPattern("yyyy/MM/dd");
		try {
			return sDF.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String generateTransactionId() {
		long currentTime = System.currentTimeMillis();
		return new Long(currentTime).toString();
	}
	
	public static String geTemplateFileContentFilled(String templateFile, Map<String, String> fillingContent) throws UnknownNotificationTemplateException {
		BufferedReader bR = null;
    	try {
    		String htmlContent = TemplateLoader.getInstance().loadTemplate(templateFile);
    		Set<String> parameterNames = fillingContent.keySet();
	    	for (String parameter: parameterNames) {
	    		htmlContent = htmlContent.replace("${"+parameter+"}", fillingContent.get(parameter));
	    	}
    		return htmlContent;
    	} catch (Exception e) {
    		throw new UnknownNotificationTemplateException(e);
    	} finally {
    		if (bR != null) {
    			try {
					bR.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
	}
	
	public static String currentDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        return localDateTime.format(formatter);
	}
	
	public static JSONObject findOffset(JSONObject playerParams) {
		if (playerParams.get("sim-live-enabled") != null && playerParams.getBoolean("sim-live-enabled") ) {
			long eventStartMillis = playerParams.getLong("event-start");
			long eventEndMillis = playerParams.getLong("event-end");
			long currentTimeMillis = Instant.now().toEpochMilli();
			playerParams.put("current_time", currentTimeMillis);
			if (currentTimeMillis > eventStartMillis && currentTimeMillis < eventEndMillis) {
				playerParams.put("running_event", true);
				playerParams.put("player_offset", (currentTimeMillis - eventStartMillis)/1000);
			} else {
				playerParams.put("running_event", false);
			}
		} else {
			playerParams = null;
		}
		return playerParams;
	}
	
	public static String setMessageAlertEmailRegisterCustomer(ArrayList<HashMap<String, Object>> listTransaction) {
		 Collections.sort(listTransaction, new Comparator<HashMap<String, Object>>() {
 			@Override
 			public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
 				return o2.get("returnCode").toString().compareTo(o1.get("returnCode").toString());
 			}
 		});
		StringBuffer mensajeHtml = new StringBuffer();
        mensajeHtml.append("  <table class='table'>  \r\n" + 
        		"    <tr>  \r\n" +
        		"        <th>name</th>  \r\n" +
        		"        <th>lasname</th>  \r\n" +
        		"        <th>email</th>  \r\n" +
        		"        <th>status</th>  \r\n" + 
        		"        <th>message</th>  \r\n" +
        		"        <th>txId</th>  \r\n" +
        		"    </tr>\n");
        for (HashMap<String, Object> hashMap : listTransaction) {
        	int returnCode = Integer.parseInt(hashMap.get("returnCode").toString());
        	HashMap<String, Object> paymentResult =  (HashMap<String, Object>) hashMap.get("paymentResult");
        	PaymentResult pResult = (PaymentResult) paymentResult.get("paymentResult");
        	HashMap<String, Object> authorizationInfo = pResult.getAuthorizationInfo();
        	mensajeHtml.append("<tr>\n");
        		mensajeHtml.append("<td>"+hashMap.get("name")+"</td>\n");
        		mensajeHtml.append("<td>"+hashMap.get("lastname")+"</td>\n");
        		mensajeHtml.append("<td>"+hashMap.get("email")+"</td>\n");
        		mensajeHtml.append("<td>"+(returnCode == 0 ? "SUCCESS" : "FAIL")+"</td>\n");
        		mensajeHtml.append("<td>"+(pResult.getResultMessage()!=null ? pResult.getResultMessage() : "null")+"</td>\n");
        		mensajeHtml.append("<td>"+(authorizationInfo!=null ? authorizationInfo.get("tx_id"):"0")+"</td>\n");
        	mensajeHtml.append("</tr>\n");
        }
        mensajeHtml.append("</table>");
		return mensajeHtml.toString();
	}
	
	public static String setMessageErrorAlertEmailRegisterCustomer(Response resp) {
		
		HashMap<String, Object> verifiedAttendeesResult = resp.getResult();
		ArrayList<BulkTransactionVerificationResultItem> bulkTransactionVerificationResultItems = (ArrayList<BulkTransactionVerificationResultItem>) verifiedAttendeesResult.get("verifiedAttendeesResult");
	   StringBuffer mensajeHtml = new StringBuffer();
       mensajeHtml.append("  <table class='table'>  \r\n" + 
       		"    <tr>  \r\n" +
       		"        <th>codigo error</th>  \r\n" +
       		"        <th>mensaje</th>  \r\n" +
       		"    </tr>\n");
       mensajeHtml.append("<tr>\n");
       		mensajeHtml.append("<td>"+resp.getReturnCode()+"</td>\n");
       		mensajeHtml.append("<td>"+resp.getReturnMessage()+"</td>\n");
       	mensajeHtml.append("</tr>\n");
       	mensajeHtml.append("</table>");
    	mensajeHtml.append("<hr style=\"width:50%;text-align:left;margin-left:0\">");
       	mensajeHtml.append("  <table class='table'>  \r\n" + 
        		"    <tr>  \r\n" +
        		"        <th>email</th>  \r\n" +
        		"        <th>name</th>  \r\n" +
        		"        <th>lasname</th>  \r\n" +
        		"        <th>paymentType</th>  \r\n" + 
        		"        <th>message</th>  \r\n" +
        		"    </tr>\n");
        for (BulkTransactionVerificationResultItem bulkTransactionVerificationResultItem : bulkTransactionVerificationResultItems) {
        	if(!bulkTransactionVerificationResultItem.isValid()) {
        		mensajeHtml.append("<tr>\n");
        		mensajeHtml.append("<td>"+bulkTransactionVerificationResultItem.getItem().getEmail()+"</td>\n");
               	mensajeHtml.append("<td>"+bulkTransactionVerificationResultItem.getItem().getName()+"</td>\n");
               	mensajeHtml.append("<td>"+bulkTransactionVerificationResultItem.getItem().getLastname()+"</td>\n");
               	mensajeHtml.append("<td>"+bulkTransactionVerificationResultItem.getItem().getPaymentType()+"</td>\n");
               	mensajeHtml.append("<td>"+bulkTransactionVerificationResultItem.getMessage()+"</td>\n");
               	mensajeHtml.append("</tr>\n");
        	}
		}
        mensajeHtml.append("</table>");
		return mensajeHtml.toString();
	}

}
