package com.us.weavx.core.data;

import com.us.weavx.core.model.CustomerSignatureGateway;
import com.us.weavx.core.model.CustomerUserEventContractSign;
import com.us.weavx.core.model.SignatureGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("signatureDAO")
public class SignatureServicesTxDAO extends JdbcDaoSupport {

    public SignatureServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public SignatureGateway findSignatureGatewayById(int id) {
        String sql = "SELECT id, name, sandbox_url, production_url from signature_gateway where id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{id}, (rs, arg1) -> {
                SignatureGateway c = new SignatureGateway();
                c.setId(id);
                c.setName(rs.getString(2));
                c.setSandboxUrl(rs.getString(3));
                c.setProductionUrl(rs.getString(4));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<SignatureGateway> listAllSignatureGateways() {
        final String querySQL = "SELECT id, name, sandbox_url, production_url from signature_gateway";
        try {
            List<SignatureGateway> resultList = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                SignatureGateway tmp = new SignatureGateway();
                tmp.setId(((Number) m.get("id")).intValue());
                tmp.setName((String) m.get("name"));
                tmp.setSandboxUrl((String) m.get("sandbox_url"));
                tmp.setProductionUrl((String) m.get("production_url"));
                resultList.add(tmp);
            }
            return resultList;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerSignatureGateway findCustomerSignatureGatewayByCustomerIdAndGatewayId(long customerId, int signatureGatewayId) {
        String sql = "SELECT customer_id, signature_gateway_id, signatureApiParam1, signatureApiParam2, signatureApiParam3, signatureApiParam4 from customer_signature_gateway where customer_id = ? and signature_gateway_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{customerId, signatureGatewayId}, (rs, arg1) -> {
                CustomerSignatureGateway c = new CustomerSignatureGateway();
                c.setCustomerId(customerId);
                c.setSignatureGatewayId(signatureGatewayId);
                c.setSignatureApiParam1(rs.getString(3));
                c.setSignatureApiParam2(rs.getString(4));
                c.setSignatureApiParam3(rs.getString(5));
                c.setSignatureApiParam4(rs.getString(6));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerSignatureGateway findCustomerSignatureGatewayByCustomerId(long customerId) {
        String sql = "SELECT customer_id, signature_gateway_id, signatureApiParam1, signatureApiParam2, signatureApiParam3, signatureApiParam4 from customer_signature_gateway where customer_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{customerId}, (rs, arg1) -> {
                CustomerSignatureGateway c = new CustomerSignatureGateway();
                c.setCustomerId(customerId);
                c.setSignatureGatewayId(rs.getInt(2));
                c.setSignatureApiParam1(rs.getString(3));
                c.setSignatureApiParam2(rs.getString(4));
                c.setSignatureApiParam3(rs.getString(5));
                c.setSignatureApiParam4(rs.getString(6));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerUserEventContractSign addCustomerUserEventContractSign(final CustomerUserEventContractSign item) {
        final String insertSQL = "INSERT INTO CUSTOMER_USER_EVENT_CONTRACT_SIGN(customer_user_id, application_id, signature_url, signature_status, signed_at, signature_data, transaction_id) VALUES (?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[] { item.getCustomerUserId(), item.getApplicationId(), item.getSignatureUrl(), item.isSignatureStatus(), item.getSignedAt(), item.getSignatureData(), item.getTransactionId()})
                        .setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUserEventContractSign findCustomerUserEventContractSignById(long id) {
        String sql = "SELECT id, customer_user_id, application_id, signature_url, signature_status, signed_at, signature_data, transaction_id from CUSTOMER_USER_EVENT_CONTRACT_SIGN where id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{id}, (rs, arg1) -> {
                CustomerUserEventContractSign c = new CustomerUserEventContractSign();
                c.setId(id);
                c.setCustomerUserId(rs.getLong(2));
                c.setApplicationId(rs.getLong(3));
                c.setSignatureUrl(rs.getString(4));
                c.setSignatureStatus(rs.getBoolean(5));
                c.setSignedAt(rs.getTimestamp(6));
                c.setSignatureData(rs.getString(7));
                c.setTransactionId(rs.getLong(8));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUserEventContractSign findCustomerUserEventContractSignByCustomerUserIdAndAppId(long customerUserId, long applicationId) {
        String sql = "SELECT id, application_id, signature_url, signature_status, signed_at, signature_data, customer_user_id, transaction_id from CUSTOMER_USER_EVENT_CONTRACT_SIGN where customer_user_id = ? and application_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{customerUserId, applicationId}, (rs, arg1) -> {
                CustomerUserEventContractSign c = new CustomerUserEventContractSign();
                c.setId(rs.getLong(1));
                c.setApplicationId(rs.getLong(2));
                c.setSignatureUrl(rs.getString(3));
                c.setSignatureStatus(rs.getBoolean(4));
                c.setSignedAt(rs.getTimestamp(5));
                c.setSignatureData(rs.getString(6));
                c.setCustomerUserId(rs.getLong(7));
                c.setTransactionId(rs.getLong(8));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerUserEventContractSign updateCustomerUserEventContractSign(CustomerUserEventContractSign item)  {
        final String updateSQL = "UPDATE CUSTOMER_USER_EVENT_CONTRACT_SIGN set signature_url = ?, signature_status = ?, signed_at = ?, signature_data = ? where id = ?";
        getJdbcTemplate().update(updateSQL, item.getSignatureUrl(), item.isSignatureStatus(), item.getSignedAt(), item.getSignatureData(), item.getId());
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerUserEventContractSign updateCustomerUserEventContractSignByCustomerUserIdAndApplication(CustomerUserEventContractSign item)  {
        final String updateSQL = "UPDATE CUSTOMER_USER_EVENT_CONTRACT_SIGN set signature_url = ?, signature_status = ?, signed_at = ?, signature_data = ? where customer_user_id = ? and application_id = ?";
        getJdbcTemplate().update(updateSQL, item.getSignatureUrl(), item.isSignatureStatus(), item.getSignedAt(), item.getSignatureData(), item.getCustomerUserId(), item.getApplicationId());
        return item;
    }


}
