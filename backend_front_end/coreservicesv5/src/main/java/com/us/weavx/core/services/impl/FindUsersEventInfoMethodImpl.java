package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.UserTxServices;

@Component
public class FindUsersEventInfoMethodImpl implements ServiceMethod{

	@Autowired
	private UserTxServices userTxServices; 
	
	@Override
	public Response executeMethod(Request request) {
		
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			HashMap<String, Object> totalUsuariosRegistradosYActivos = userTxServices.findUsersEventInfo(customerId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("registered_users", totalUsuariosRegistradosYActivos.get("registered_users"));
			result.put("online_users", totalUsuariosRegistradosYActivos.get("online_users"));
			res.setResult(result);
			return res;
		}catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
			return res;
		}
		
	}

}
