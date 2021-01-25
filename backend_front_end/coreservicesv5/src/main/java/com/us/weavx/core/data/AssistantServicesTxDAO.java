package com.us.weavx.core.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.model.Assistant;

@Component("assistantDAO")
public class AssistantServicesTxDAO extends JdbcDaoSupport{

	public AssistantServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Assistant addAssistant(final Assistant item) {
		final String insertSQL = "INSERT INTO ASSISTANT(FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, ADDRESS, COUNTRY_ID, TRANSACTION_ID) VALUES (?,?,?,?,?,?,?)";
		PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            	PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{item.getFirstName(), item.getLastName(), item.getEmail(), item.getPhoneNumber(), item.getAddress(), item.getCountryId(), item.getTransactionId()})
                        .setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
		return item;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Assistant updateAssistant(Assistant item) {
        final String updateSQL = "UPDATE ASSISTANT SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PHONE_NUMBER = ?, ADDRESS = ?, COUNTRY_ID = ?, TRANSACTION_ID = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getFirstName(), item.getLastName(), item.getEmail(), item.getPhoneNumber(), item.getAddress(), item.getCountryId(), item.getTransactionId(), item.getId()});
        return item;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Assistant findAssistantById(long id) {
        String sql = "SELECT ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, ADDRESS, COUNTRY_ID, TRANSACTION_ID FROM ASSISTANT WHERE ID = ?";
        try {
        	Assistant item = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<Assistant>() {

                public Assistant mapRow(ResultSet rs, int arg1) throws SQLException {
                	Assistant v = new Assistant();
                    v.setId(id);
                    v.setFirstName(rs.getString(2));
                    v.setLastName(rs.getString(3));
                    v.setEmail(rs.getString(4));
                    v.setPhoneNumber(rs.getString(5));
                    v.setAddress(rs.getString(6));
                    v.setCountryId(rs.getInt(6));
                    v.setTransactionId(rs.getInt(6));
                    return v;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Assistant> listAllAssistants() {
		String querySQL = "SELECT ID,FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, ADDRESS, COUNTRY_ID, TRANSACTION_ID FROM ASSISTANT";
        try {
            ArrayList<Assistant> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{});
            for (Map<String, Object> m : result) {
                Assistant item = new Assistant();
                item.setId(((Number) m.get("id")).longValue());
                item.setFirstName((String) m.get("first_name"));
                item.setLastName((String) m.get("last_name"));
                item.setEmail((String) m.get("email"));
                item.setPhoneNumber((String) m.get("phone_number"));
                item.setAddress((String) m.get("address"));
                item.setCountryId(((Number) m.get("country_id")).intValue());
                item.setTransactionId(((Number) m.get("transaction_id")).intValue());
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
