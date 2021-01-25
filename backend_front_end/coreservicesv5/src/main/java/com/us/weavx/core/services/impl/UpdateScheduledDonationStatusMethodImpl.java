package com.us.weavx.core.services.impl;

import java.util.HashMap;

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
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.DonationTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
@Component
public class UpdateScheduledDonationStatusMethodImpl implements ServiceMethod {
	@Autowired
	private UserTxServices userTxServices;
	@Autowired
	private DonationTxServices donationServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			String userAccessToken = (String) params.get("userAccessToken");
			//Validaci�n del token
			UserAccessToken uAT = userTxServices.validateUserAccessToken(userAccessToken);
			long scheduledDonationId = ((Number) params.get("sheduled_donation_id")).longValue();
			int scheduledDonationStatusId = (Integer) params.get("donation_status_id");
			ScheduledDonation donation = new ScheduledDonation();
			donation.setId(scheduledDonationId);
			donation.setCustomer_user_id(uAT.getCustomerUserId());
			donation.setDonation_status_id(scheduledDonationStatusId);
			donationServices.updateScheduleDonationStatus(aInfo.getApplicationId(),donation);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			return res;
		} catch (RuntimeException e) {
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
		} catch (DonationGeneralException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" e: "+e.getMessage());
			return res;
		}
	}

	
	

}
