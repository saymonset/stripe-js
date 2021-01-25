package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.EmailSendingGeneralException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.exception.UnknownApplicationException;
import com.us.weavx.core.exception.UnknownCustomerPropertyException;
import com.us.weavx.core.exception.UnknownCustomerUserException;
import com.us.weavx.core.exception.UnknownNotificationTemplateException;
import com.us.weavx.core.exception.UserAccessTokenGenerationException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.RestrictedEventAttendee;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;
import com.us.weavx.core.services.tx.UserTxServices;

@Component
public class AuthenticateCustomerUserByEmailMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;
	@Autowired
	private EventRestrictionServicesTx eventRestrictionServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			String ipAddress = (String) params.get("ipAddress");
			if (ipAddress == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.IP_ADDRESS_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.IP_ADDRESS_REQUIRED);
				return res;
			}
			String userAgent = (String) params.get("userAgent");
			if (userAgent == null) {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.USER_AGENT_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.USER_AGENT_REQUIRED);
				return res;				
			}
			Integer langId = (Integer) params.get("lang_id");
			langId = (langId == null)?1:langId;
			//Se inicializa el container de spring
			String email = (String) params.get("email");
			if (email != null) {
				email = email.toLowerCase();
			}
			//Se verifica si se trata de un evento restringido
			if (eventRestrictionServices.isRestrictedEvent(customerId, aInfo.getApplicationId())) {
				//Se verifica si el usuario tiene acceso al evento
				RestrictedEventAttendee attendee = eventRestrictionServices.findEventAttendeeByCustomerIdApplicationIdAndEmail(customerId, aInfo.getApplicationId(), email);
				if (attendee == null) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.RESTRICTED_EVENT_CODE);
					res.setReturnMessage(ServiceReturnMessages.RESTRICTED_EVENT);
					return res;
				}
			}
			boolean hasPurchases = userServices.authenticateCustomerUserByEmailV2(email, customerId, aInfo.getApplicationId(), ipAddress, userAgent, langId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("hasPurchases", hasPurchases);
			res.setResult(result);
			return res;
		} catch (RuntimeException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
			return res;
		} catch (UnknownCustomerUserException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER_CODE);
			res.setReturnMessage(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER);
			return res;
		} catch (UnknownNotificationTemplateException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.NOTIFICATION_TEMPLATE_NOT_FOUND_CODE);
			res.setReturnMessage(ServiceReturnMessages.NOTIFICATION_TEMPLATE_NOT_FOUND);
			return res;
		} catch (EmailSendingGeneralException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.EMAIL_SENDING_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.EMAIL_SENDING_ERROR);
			return res;
		} catch (UnknownApplicationException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.UNKNOWN_APPLICATION_CODE);
			res.setReturnMessage(ServiceReturnMessages.UNKNOWN_APPLICATION);
			return res;
		} catch (UserAccessTokenGenerationException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.ACCESS_TOKEN_GENERATION_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.ACCESS_TOKEN_GENERATION_ERROR);
			return res;
		} catch (MaximumCustomerUserSessionsForAppExceededException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED_CODE);
			res.setReturnMessage(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED);
			HashMap<String, Object> result = new HashMap<>();
			result.put("userSessions", e.getCurrentSessions());
			res.setResult(result);
			return res;				
		} catch (UnknownCustomerPropertyException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+": "+e);
			return res;
		}
	}

	
	

}
