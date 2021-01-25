package com.us.weavx.core.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.DonationTxServices;
@Component
public class ProccessScheduledDonationsMethodImpl implements ServiceMethod {
	@Autowired
	private DonationTxServices donationServices;

	public Response executeMethod(Request request) {
		try {
			AccessInfo aInfo = (AccessInfo) request.getParameters().get("accessInfo"); 
			donationServices.proccessScheduledDonations(aInfo.getApplicationId());
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
			return res;
		}
	}

	
	

}
