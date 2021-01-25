package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.GeneralDescription;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.util.GeneralDescriptionManager;

@Component
public class GetGeneralDescriptionMethodImpl implements ServiceMethod {

	@Autowired
	private GeneralDescriptionManager managerGeneralDescription;
	
	@Override
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			Long customerId =  aInfo.getCustomerId();
			
			List<GeneralDescription> resultTmp = managerGeneralDescription.getAllGeneralDescriptions(customerId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			res.setReturnDate(LocalDateTime.now().toString());
			HashMap<String, Object> result = new HashMap<>();
			List<Object> list = new ArrayList<>();
			list.add(resultTmp);
			result.put("generalDescriptions", list);
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
