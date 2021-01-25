package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.BlackListItem;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.BlackListTxServices;
@Component
public class AddBlacklistItemMethodImpl implements ServiceMethod{

	@Autowired
	private BlackListTxServices blackListTxServices;
	
	@Override
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			BlackListItem item = new BlackListItem();
			String data = (String) params.get("data");
			int dataTypeId = (int) params.get("dataTypeId");
			long blockedBy = 220;//es el id constante asignado a los bloqueos realizados desde la administración
			item.setData(data);
			item.setBlockedBy(blockedBy);
			item.setDataTypeId(dataTypeId);
			blackListTxServices.addBlacklistItem(item);
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
