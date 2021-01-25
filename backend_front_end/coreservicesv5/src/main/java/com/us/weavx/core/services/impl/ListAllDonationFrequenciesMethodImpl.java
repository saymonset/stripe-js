package com.us.weavx.core.services.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.ScheduledDonationFrequency;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.util.DonationFrequencyManager;
@Component
public class ListAllDonationFrequenciesMethodImpl implements ServiceMethod {
	@Autowired
	private DonationFrequencyManager manager;

	public Response executeMethod(Request request) {
		try {
			List<ScheduledDonationFrequency> resultTmp = manager.getAllDonationfrequencies();
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("donationFrequencies", resultTmp);
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
