package com.us.weavx.core.services.tx;

import com.us.weavx.core.data.AuditServicesTxDAO;
import com.us.weavx.core.model.AuditRecord;
import com.us.weavx.core.model.ReportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("auditTxServices")
public class AuditTxServices {

    @Autowired
    private AuditServicesTxDAO dao;
		
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=false)
	public List<Map<String, Object>> findAuditRecords(List<ReportFilter> filters, long customerId, int auditLevelId) {
		return dao.findAuditRecords(filters, customerId, auditLevelId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AuditRecord addAuditRecord(final AuditRecord item) {
		return dao.addAuditRecord(item);
	}
	
}
