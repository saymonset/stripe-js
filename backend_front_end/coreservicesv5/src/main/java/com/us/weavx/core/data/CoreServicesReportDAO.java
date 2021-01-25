package com.us.weavx.core.data;

import com.us.weavx.core.model.ReportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component("reportDAO")
public class CoreServicesReportDAO extends NamedParameterJdbcDaoSupport {

    public CoreServicesReportDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    private boolean existsInArray(String[] param, String key) {
        for (String s : param) {
            if (s.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Map<String, Object>> getCustomerTransactionReport(List<ReportFilter> filters, long customerId) {
    	String querySQL = "SELECT person_firstname, person_lastname, person_addressline1, person_addresscity, person_addressstate, person_addresszip, person_country, contribution_date, contribution_amount, contribution_discount, contribution_commission, contribution_onl_transactionid, contribution_paymentinfo, contribution_fund, contribution_containerid, person_email, person_authorizenetprofileid, contribution_onl_approvalnumber, contribution_form, transactioninternalid, Application_Name, transaction_status, payment_type, payment_gw, donation_source, donation_campaing, is_settled, transaction_medium, comments, operator_id, operator_email, person_phone, ip_address, user_agent FROM transaction_report";
        String minMax[] = {"contribution_date_min", "contribution_date_max", "contribution_amount_min", "contribution_amount_max"};
        String like[] = {"person_firstname","person_lastname","person_email","operator_email","person_phone","ip_address","user_agent"};
        HashMap<String, Object> minMaxData = new HashMap<String, Object>();
        try {
            Iterator<ReportFilter> iter = filters.iterator();
            StringBuffer queryBuf = new StringBuffer(querySQL);
            Map<String, Object> namedParameters = new HashMap<String, Object>();
            if (customerId != -1) {
				queryBuf.append(" where Customer_Id = :customer_id");
				namedParameters.put("customer_id", customerId);
			} else {
				queryBuf.append(" where Customer_id > 0 and Customer_Id != 205");
			}
            while (iter.hasNext()) {
                ReportFilter tmp = iter.next();
                if (existsInArray(minMax, tmp.getFieldName())) {
                    minMaxData.put(tmp.getFieldName(), tmp.getFieldValue());
                } else if (existsInArray(like, tmp.getFieldName())) {
                    queryBuf.append(" and UPPER(").append(tmp.getFieldName()).append(") like :").append(tmp.getFieldName());
                    namedParameters.put(tmp.getFieldName(), ("%" + tmp.getFieldValue()).toUpperCase() + "%");
                } else {
                    queryBuf.append(" and ").append(tmp.getFieldName()).append(" = :").append(tmp.getFieldName());
                    namedParameters.put(tmp.getFieldName(), tmp.getFieldValue());
                }
            }
            //Se procesan las clausulas de rango
            if (minMaxData.containsKey("contribution_date_min") && minMaxData.containsKey("contribution_date_max")) {
                queryBuf.append(" and contribution_date between :min_date and :max_date");
                namedParameters.put("min_date", new Timestamp(((Number) minMaxData.get("contribution_date_min")).longValue()));
                namedParameters.put("max_date", new Timestamp(((Number) minMaxData.get("contribution_date_max")).longValue()));
            }
            if (minMaxData.containsKey("contribution_amount_min") && minMaxData.containsKey("contribution_amount_max")) {
                queryBuf.append(" and contribution_amount between :min_amount and :max_amount");
                namedParameters.put("min_amount", minMaxData.get("contribution_amount_min"));
                namedParameters.put("max_amount", minMaxData.get("contribution_amount_max"));
            }
            querySQL = queryBuf.toString();
            List<Map<String, Object>> records = getNamedParameterJdbcTemplate().queryForList(querySQL, namedParameters);
            return records;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Map<String, Object>> getFinancesReport(List<ReportFilter> filters, long customerId) {
        String querySQL = "SELECT Person_FirstName,Person_LastName,Person_AddressLine1,Person_AddressCity, Person_AddressState,Person_AddressZIP,Person_Country,'' as Person_HomePhone , Contribution_Date,Contribution_Amount,Contribution_ONL_TransactionID,'' as Contribution_Comments,Contribution_ContainerID,Person_eMail,Person_AuthorizeNetProfileID,Contribution_ONL_ApprovalNumber,Contribution_Form,'Cleared' as Contribution_Status FROM transaction_report where transaction_status_id = 5 and Customer_Id = :customer_id";
        String minMax[] = {"contribution_date_min", "contribution_date_max"};
        HashMap<String, Object> minMaxData = new HashMap<String, Object>();
        try {
            Iterator<ReportFilter> iter = filters.iterator();
            StringBuffer queryBuf = new StringBuffer(querySQL);
            Map<String, Object> namedParameters = new HashMap<String, Object>();
            namedParameters.put("customer_id", customerId);
            while (iter.hasNext()) {
                ReportFilter tmp = iter.next();
                if (existsInArray(minMax, tmp.getFieldName())) {
                    minMaxData.put(tmp.getFieldName(), tmp.getFieldValue());
                } else {
                    queryBuf.append(" and ").append(tmp.getFieldName()).append(" = :").append(tmp.getFieldName());
                    namedParameters.put(tmp.getFieldName(), tmp.getFieldValue());
                }
            }
            //Se procesan las clausulas de rango
            if (minMaxData.containsKey("contribution_date_min") && minMaxData.containsKey("contribution_date_max")) {
                queryBuf.append(" and contribution_date between :min_date and :max_date");
                namedParameters.put("min_date", new Timestamp(((Number) minMaxData.get("contribution_date_min")).longValue()));
                namedParameters.put("max_date", new Timestamp(((Number) minMaxData.get("contribution_date_max")).longValue()));
            }
            querySQL = queryBuf.toString();
            List<Map<String, Object>> records = getNamedParameterJdbcTemplate().queryForList(querySQL, namedParameters);
            return records;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Map<String, Object>> getUsers(List<ReportFilter> filters, long customerId) {
        String querySQL = "SELECT cu.id, u.email, cu.first_name, cu.last_name, cu.created_at, cu.phone_number, coalesce(cuev.signature_status,false) signature_status, cuev.signed_at, cuev.signature_url FROM customer_user cu inner join users u on cu.user_id = u.id left join customer_user_event_contract_sign cuev on cu.id = cuev.customer_user_id where cu.customer_Id = :customer_id";
        String minMax[] = {"created_at_min", "created_at_max"};
        String like[] = {"first_name", "last_name", "email"};
        HashMap<String, Object> minMaxData = new HashMap<String, Object>();
        try {
            Iterator<ReportFilter> iter = filters.iterator();
            StringBuffer queryBuf = new StringBuffer(querySQL);
            Map<String, Object> namedParameters = new HashMap<String, Object>();
            namedParameters.put("customer_id", customerId);
            while (iter.hasNext()) {
                ReportFilter tmp = iter.next();
                if (existsInArray(minMax, tmp.getFieldName())) {
                    minMaxData.put(tmp.getFieldName(), tmp.getFieldValue());
                } else if (existsInArray(like, tmp.getFieldName())) {
                    queryBuf.append(" and UPPER(").append(tmp.getFieldName()).append(") like :").append(tmp.getFieldName());
                    namedParameters.put(tmp.getFieldName(), ("%" + tmp.getFieldValue()).toUpperCase() + "%");
                } else {
                    queryBuf.append(" and ").append(tmp.getFieldName()).append(" = :").append(tmp.getFieldName());
                    namedParameters.put(tmp.getFieldName(), tmp.getFieldValue());
                }
            }
            //Se procesan las clausulas de rango
            if (minMaxData.containsKey("created_at_min") && minMaxData.containsKey("created_at_max")) {
                queryBuf.append(" and cu.created_at between :min_date and :max_date");
                namedParameters.put("min_date", new Timestamp(((Number) minMaxData.get("created_at_min")).longValue()));
                namedParameters.put("max_date", new Timestamp(((Number) minMaxData.get("created_at_max")).longValue()));
            }
            querySQL = queryBuf.toString();
            List<Map<String, Object>> records = getNamedParameterJdbcTemplate().queryForList(querySQL, namedParameters);
            return records;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
