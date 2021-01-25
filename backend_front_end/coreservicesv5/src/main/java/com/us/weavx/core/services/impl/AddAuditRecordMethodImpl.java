package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AuditRecord;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.AuditTxServices;

@Component
public class AddAuditRecordMethodImpl implements ServiceMethod {

	@Autowired
	private AuditTxServices auditServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			HashMap<String, Object> auditRecordParam = (HashMap<String, Object>) params.get("audit_record");
			AuditRecord auditRecord = new AuditRecord();
			auditRecord.setCustomerId(aInfo.getCustomerId());
			auditRecord.setApplicationId(aInfo.getApplicationId());
			auditRecord.setWho((String) auditRecordParam.get("who"));
			auditRecord.setWhat((String) auditRecordParam.get("what"));
			auditRecord.setIpAddress((String) auditRecordParam.get("ip_address"));
			auditRecord.setUserAgent((String) auditRecordParam.get("user_agent"));
			auditRecord.setSource((String) auditRecordParam.get("source"));
			auditRecord.setData((String) auditRecordParam.get("data"));
			auditRecord.setAuditLevelId(((Number) auditRecordParam.get("audit_level_id")).intValue());
			auditRecord = auditServices.addAuditRecord(auditRecord);
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("auditRecord", auditRecord);
			resp.setResult(result);
			return resp;
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());
			return resp;
		}
	}
}
