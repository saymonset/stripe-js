package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.ReportFilter;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ReportTxServices;

@Component
public class GlobalTransactionReportMethodImpl implements ServiceMethod {

	@Autowired
	ReportTxServices services;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			List<Map<String, Object>> filters = (List<Map<String, Object>>) params.get("filters");
			Iterator<Map<String, Object>> filterIterator = filters.iterator();
			ArrayList<ReportFilter> filtersParamList = new ArrayList<>();
			while (filterIterator.hasNext()) {
				Map<String, Object> tmp = filterIterator.next();
				ReportFilter newFilter = new ReportFilter();
				newFilter.setFieldName((String) tmp.get("fieldName"));
				newFilter.setFieldValue(tmp.get("fieldValue"));
				filtersParamList.add(newFilter);
			}
			List<Map<String, Object>> reportRecords = services.getCustomerTransactionReport(filtersParamList, -1);
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("report", reportRecords);
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
