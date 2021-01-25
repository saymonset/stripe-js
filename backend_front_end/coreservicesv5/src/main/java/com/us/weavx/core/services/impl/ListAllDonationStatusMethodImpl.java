package com.us.weavx.core.services.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.ScheduledDonationStatus;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.DonationTxServices;
@Component
public class ListAllDonationStatusMethodImpl implements ServiceMethod {
	@Autowired
	private DonationTxServices services;

	public Response executeMethod(Request request) {
		try {
			List<ScheduledDonationStatus> resultTmp = services.listAllScheduledDonationStatus();
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("donationStatus", resultTmp);
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
