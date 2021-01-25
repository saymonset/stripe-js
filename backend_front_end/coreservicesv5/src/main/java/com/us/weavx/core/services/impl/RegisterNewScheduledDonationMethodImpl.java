package com.us.weavx.core.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.DonationGeneralException;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.ScheduledDonation;
import com.us.weavx.core.model.ScheduledDonationFundDetail;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.DonationTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class RegisterNewScheduledDonationMethodImpl implements ServiceMethod {

	@Autowired
	private UserTxServices userTxServices;
	@Autowired
	private DonationTxServices donationServices;
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			String userAccessToken = (String) params.get("userAccessToken");
			//Validaci�n del token
			UserAccessToken uAT = userTxServices.validateUserAccessToken(userAccessToken);
			//Armado de ScheduledDonation
			Integer language = (Integer) params.get("lang_id");
			language=(language==null)?1:language;
			HashMap<String, Object> scheduledDonationJSON = (HashMap<String, Object>) params.get("scheduled_donation");
			List<HashMap<String,Object>> fundsJSON = (List<HashMap<String, Object>>) params.get("funds");
			List<ScheduledDonationFundDetail> details = new ArrayList<>();
			for (HashMap<String, Object> e : fundsJSON) {
				ScheduledDonationFundDetail scheduledDonationFundTmp = new ScheduledDonationFundDetail();
				scheduledDonationFundTmp.setFund_id((Integer) e.get("fund_id"));
				scheduledDonationFundTmp.setAmount(((Number) e.get("amount")).doubleValue());
				details.add(scheduledDonationFundTmp);
			}
			ScheduledDonation d = new ScheduledDonation();
			d.setCustomer_user_id(uAT.getCustomerUserId());
			d.setDonation_frequency_id((Integer) scheduledDonationJSON.get("donation_frequency_id"));
			d.setStarts_at(new Timestamp(((Number) scheduledDonationJSON.get("starts_at")).longValue())) ;
			d = donationServices.addScheduledDonation(customerId, applicationId, language, d, details);
			Response res = new Response();
			HashMap<String, Object> result = new HashMap<>();
			result.put("newScheduledDonation", d);
			res.setResult(result);
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			return res;
		} catch (RuntimeException | DonationGeneralException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" e: "+e.getMessage());
			return res;
		} catch (InvalidUserAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN_CODE);
			res.setReturnMessage(ServiceReturnMessages.INVALID_USER_ACCESS_TOKEN);
			return res;
		} catch (ExpiredUserAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.EXPIRED_USER_ACCESS_TOKEN_CODE);
			res.setReturnMessage(ServiceReturnMessages.EXPIRED_USER_ACCESS_TOKEN);
			return res;
		} 
	}

	
	

}
