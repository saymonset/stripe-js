package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.OTPKey;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.RestrictedEventAttendee;
import com.us.weavx.core.model.User;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.util.ApplicationParametersManager;
@Component
public class GenerateOTPMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userServices;
	@Autowired
	private EventRestrictionServicesTx eventRestrictionServices;
	@Autowired
	private ApplicationParametersManager appParamManager;

	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			String email = (String) params.get("email");
			int langId = ((Number) params.get("langId")).intValue();
			Boolean byp = (Boolean) params.get("byp");
			boolean isOTPEmailRequired = (byp == null)?true:!byp;
			//Se verifica si se trata de un evento restringido
			if (eventRestrictionServices.isRestrictedEvent(customerId, applicationId)) {
				//Se verifica si el usuario tiene acceso al evento
				RestrictedEventAttendee attendee = eventRestrictionServices.findEventAttendeeByCustomerIdApplicationIdAndEmail(customerId, aInfo.getApplicationId(), email);
				if (attendee == null) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.RESTRICTED_EVENT_CODE);
					res.setReturnMessage(ServiceReturnMessages.RESTRICTED_EVENT);
					return res;
				}
			}
			String bypassDomains = appParamManager.getApplicationParameter(applicationId, "DOMAIN_KEYWORD_BYPASS");
			if (bypassDomains != null) {
				if (email != null) {
					String emailDomain = email.substring(email.indexOf("@")+1);
					if (bypassDomains.toLowerCase().indexOf(emailDomain.toLowerCase()) != -1) {
						isOTPEmailRequired = false;
					}
				}
			}
			OTPKey otp = userServices.generateAuthenticationOTP(email, customerId, applicationId, langId, isOTPEmailRequired);
			User u = userServices.findUserByEmail(email);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("otp_key", otp);
			result.put("user_created_at", u.getCreatedAt());
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
