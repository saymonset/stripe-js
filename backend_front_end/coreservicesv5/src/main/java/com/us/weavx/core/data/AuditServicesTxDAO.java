package com.us.weavx.core.data;

import com.us.weavx.core.model.AuditRecord;
import com.us.weavx.core.model.ReportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component("auditDAO")
public class AuditServicesTxDAO extends NamedParameterJdbcDaoSupport {

    public AuditServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Map<String, Object>> findAuditRecords(List<ReportFilter> filters, long customerId, int auditLevelId) {
        String querySQL = "SELECT A.ID, A.CUSTOMER_ID, A.APPLICATION_ID, A.WHO, A.CREATED_AT, A.SOURCE, A.WHAT, A.IP_ADDRESS, A.USER_AGENT, AL.NAME auditLevel, A.DATA FROM AUDIT A INNER JOIN AUDIT_LEVEL AL ON A.audit_level_id = AL.id WHERE A.CUSTOMER_ID = :customer_id AND AL.ID = :audit_level_id";
        List<String> minMax = Arrays.asList("min_date", "max_date");
        List<String> like = Arrays.asList("who", "what", "source", "ip_address", "user_agent", "data");
        HashMap<String, Object> minMaxData = new HashMap<String, Object>();
        try {
            Iterator<ReportFilter> iter = filters.iterator();
            StringBuffer queryBuf = new StringBuffer(querySQL);
            Map<String, Object> namedParameters = new HashMap<String, Object>();
            namedParameters.put("customer_id", customerId);
            namedParameters.put("audit_level_id", auditLevelId);
            while (iter.hasNext()) {
                ReportFilter tmp = iter.next();
                if (minMax.contains(tmp.getFieldName())) {
                    minMaxData.put(tmp.getFieldName(), tmp.getFieldValue());
                } else if (like.contains(tmp.getFieldName())) {
                    queryBuf.append(" and UPPER(A.").append(tmp.getFieldName()).append(") like :").append(tmp.getFieldName());
                    namedParameters.put(tmp.getFieldName(), ("%" + tmp.getFieldValue()).toUpperCase() + "%");
                } else {
                    queryBuf.append(" and A.").append(tmp.getFieldName()).append(" = :").append(tmp.getFieldName());
                    namedParameters.put(tmp.getFieldName(), tmp.getFieldValue());
                }
            }
            //Se procesan las clausulas de rango
            if (minMaxData.containsKey("date_min") && minMaxData.containsKey("date_max")) {
                queryBuf.append(" and A.created_at between :min_date and :max_date");
                namedParameters.put("min_date", new Timestamp(((Number) minMaxData.get("date_min")).longValue()));
                namedParameters.put("max_date", new Timestamp(((Number) minMaxData.get("date_max")).longValue()));
            }
            querySQL = queryBuf.toString();
            return getNamedParameterJdbcTemplate().queryForList(querySQL, namedParameters);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public AuditRecord addAuditRecord(final AuditRecord item) {
        final String insertSQL = "INSERT INTO AUDIT(customer_id, application_id, who, source, what, ip_address, user_agent, audit_level_id, data) values (?,?,?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getCustomerId(), item.getApplicationId(), item.getWho(), item.getSource(), item.getWhat(), item.getIpAddress(), item.getUserAgent(), item.getAuditLevelId(), item.getData()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
        return item;
    }
}
