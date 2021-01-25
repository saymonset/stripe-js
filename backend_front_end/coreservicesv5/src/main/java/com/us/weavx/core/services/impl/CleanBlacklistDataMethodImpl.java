package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.BlackListItem;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.BlackListTxServices;

@Component
public class CleanBlacklistDataMethodImpl implements ServiceMethod {

	@Autowired
	private BlackListTxServices blacklistServices;
	
	public Response executeMethod(Request request) {
		Response res = new Response();
		try {
			HashMap<String, Object> params = request.getParameters();
			long blackListDataItemId = ((Number) params.get("blackListDataItemId")).longValue();
			//Spring context
			List<BlackListItem> cleanedItems = new ArrayList<>();
			blacklistServices.cleanBlackListData(blackListDataItemId, cleanedItems);
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("cleanedItems", cleanedItems);
			res.setResult(result);
		} catch (Exception e) {
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
		} 
		return res;
	}

	
	

}
