package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ReportTxServices;

@Component
public class GetTransactionReportDetailsMethodImpl implements ServiceMethod {

	@Autowired
	private ReportTxServices services;

	@Override
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			String transactionId = params.get("transactionId").toString();
			
			List<Map<String, Object>> reportRecords = services.getCustomerTransactionReportDetails(transactionId);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			res.setReturnDate(LocalDateTime.now().toString());
			HashMap<String, Object> result = new HashMap<>();
			result.put("details", reportRecords);
			res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e.getMessage());
			return res;
		}
	}
}
