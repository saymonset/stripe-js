package com.us.weavx.core.services.tx;

import com.us.weavx.core.data.CoreServicesReportDAO;
import com.us.weavx.core.model.ReportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("reportTxServices")
public class ReportTxServices {

    @Autowired
    private CoreServicesReportDAO dao;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Map<String, Object>> getCustomerTransactionReport(List<ReportFilter> filters, long customerId) {
        return dao.getCustomerTransactionReport(filters, customerId);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Map<String, Object>> getFinancesReport(List<ReportFilter> filters, long customerId) {
        return dao.getFinancesReport(filters, customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Map<String, Object>> getUsers(List<ReportFilter> filters, long customerId) {
        return dao.getUsers(filters, customerId);
    }

}
