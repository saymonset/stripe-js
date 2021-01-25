package com.us.weavx.core.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.exception.InvalidEmailAddressException;
import com.us.weavx.core.model.EventFundSettings;
import com.us.weavx.core.model.RestrictedEventAttendee;

@Component("eventRestrictionDAO")
public class EventRestrictionServicesTxDAO extends JdbcDaoSupport {

    public EventRestrictionServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public RestrictedEventAttendee addRestrictedEventAttendee(final RestrictedEventAttendee item) {
        final String insertSQL = "INSERT INTO RESTRICTED_EVENT_ATTENDEE(CUSTOMER_ID, APPLICATION_ID, EMAIL) VALUES (?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getCustomerId(), item.getApplicationId(), item.getEmail().toLowerCase()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
        return item;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public RestrictedEventAttendee updateRestrictedEventAttendee(RestrictedEventAttendee item) throws InvalidEmailAddressException {
        if (!EmailValidator.getInstance().isValid(item.getEmail())) {
            throw new InvalidEmailAddressException();
        }
        final String updateSQL = "UPDATE RESTRICTED_EVENT_ATTENDEE SET EMAIL = ?, IS_ACTIVE = ? WHERE ID = ? AND CUSTOMER_ID = ? AND APPLICATION_ID = ?";
        getJdbcTemplate().update(updateSQL, item.getEmail().toLowerCase(), item.isActive(), item.getId(), item.getCustomerId(), item.getApplicationId());
        return item;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeRestrictedEventAttendee(long id, long customerId, long applicationId) {
        final String updateSQL = "UPDATE RESTRICTED_EVENT_ATTENDEE SET IS_ACTIVE = false WHERE ID = ? AND CUSTOMER_ID = ? AND APPLICATION_ID = ?";
        getJdbcTemplate().update(updateSQL, id, customerId, applicationId);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public RestrictedEventAttendee findEventAttendeeById(long id) {
        String sql = "SELECT ID, EMAIL, CUSTOMER_ID, APPLICATION_ID, IS_ACTIVE FROM RESTRICTED_EVENT_ATTENDEE WHERE ID = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{id}, (rs, arg1) -> {
                RestrictedEventAttendee c = new RestrictedEventAttendee();
                c.setId(rs.getLong(1));
                c.setEmail(rs.getString(2));
                c.setCustomerId(rs.getLong(3));
                c.setApplicationId(rs.getLong(4));
                c.setActive(rs.getBoolean(5));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public RestrictedEventAttendee findEventAttendeeByCustomerIdApplicationIdAndEmail(long customerId, long applicationId, String email) {
        String sql = "SELECT ID, EMAIL, CUSTOMER_ID, APPLICATION_ID, IS_ACTIVE FROM RESTRICTED_EVENT_ATTENDEE WHERE CUSTOMER_ID = ? AND APPLICATION_ID = ? AND EMAIL = ? AND IS_ACTIVE";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{customerId, applicationId, email}, (rs, arg1) -> {
                RestrictedEventAttendee c = new RestrictedEventAttendee();
                c.setId(rs.getLong(1));
                c.setEmail(rs.getString(2));
                c.setCustomerId(rs.getLong(3));
                c.setApplicationId(rs.getLong(4));
                c.setActive(rs.getBoolean(5));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Long getEventRestrictedAttendeesCount(long customerId, long applicationId) {
        String sql = "SELECT COUNT(*) FROM RESTRICTED_EVENT_ATTENDEE WHERE CUSTOMER_ID = ? AND APPLICATION_ID = ? AND IS_ACTIVE";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{customerId, applicationId}, (rs, arg1) -> rs.getLong(1));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addRestrictedAttendees(final List<RestrictedEventAttendee> restrictedEventAttendees) {
        String insertSQL = "INSERT INTO RESTRICTED_EVENT_ATTENDEE(CUSTOMER_ID, APPLICATION_ID, EMAIL) VALUES (?,?,?)";
        getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement arg0, int arg1) throws SQLException {
                RestrictedEventAttendee tmp = restrictedEventAttendees.get(arg1);
                new ArgumentPreparedStatementSetter(new Object[]{tmp.getCustomerId(), tmp.getApplicationId(), tmp.getEmail().toLowerCase()})
                        .setValues(arg0);
            }

            @Override
            public int getBatchSize() {
                return restrictedEventAttendees.size();
            }
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<RestrictedEventAttendee> listAllRestrictedAttendeesByEvent(long customerId, long applicationId) {
        final String querySQL = "SELECT ID, EMAIL, CUSTOMER_ID, APPLICATION_ID, IS_ACTIVE FROM RESTRICTED_EVENT_ATTENDEE WHERE CUSTOMER_ID = ? AND APPLICATION_ID = ? ORDER BY ID";
        try {
            ArrayList<RestrictedEventAttendee> eventAttendees = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, customerId, applicationId);
            for (Map<String, Object> m : result) {
                RestrictedEventAttendee tmp = new RestrictedEventAttendee();
                tmp.setId(((Number) m.get("id")).longValue());
                tmp.setEmail((String) m.get("email"));
                tmp.setCustomerId(customerId);
                tmp.setApplicationId(applicationId);
                tmp.setActive((Boolean) m.get("is_active"));
                eventAttendees.add(tmp);
            }
            return eventAttendees;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EventFundSettings findEventFundSettingsByCustomerIdAndApplicationId(long fundId, long customerId, long applicationId) {
        String sql = "SELECT id, customer_id, application_id, fund_id, allowedDaysToAccess, start_date, end_date, signature_required, signature_document_id, price, minimum, event_type, event_id  FROM EVENT_FUND_SETTINGS WHERE fund_id = ? and customer_id = ? and application_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{fundId, customerId, applicationId}, (rs, arg1) -> {
                EventFundSettings c = new EventFundSettings();
                c.setId(rs.getLong(1));
                c.setCustomerId(rs.getLong(2));
                c.setApplicationId(rs.getLong(3));
                c.setFundId(rs.getLong(4));
                c.setAllowedDaysToAccess(rs.getInt(5));
                c.setStartDate(rs.getTimestamp(6));
                c.setEndDate(rs.getTimestamp(7));
                c.setSignatureRequired(rs.getBoolean(8));
                c.setSignatureDocumentId(rs.getString(9));
                c.setPrice(rs.getDouble(10));
                c.setMinimum(rs.getInt(11));
                c.setEventType(rs.getBoolean(12));
                c.setEventId(rs.getLong(13));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EventFundSettings findEventFundSettingsByCustomerIdAndApplicationIdAndEventId(long fundId, long customerId, long applicationId, long eventId) {
        String sql = "SELECT id, customer_id, application_id, fund_id, allowedDaysToAccess, start_date, end_date, signature_required, signature_document_id, price, minimum, event_type, event_id  FROM EVENT_FUND_SETTINGS WHERE fund_id = ? and customer_id = ? and application_id = ? and event_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{fundId, customerId, applicationId, eventId}, (rs, arg1) -> {
                EventFundSettings c = new EventFundSettings();
                c.setId(rs.getLong(1));
                c.setCustomerId(rs.getLong(2));
                c.setApplicationId(rs.getLong(3));
                c.setFundId(rs.getLong(4));
                c.setAllowedDaysToAccess(rs.getInt(5));
                c.setStartDate(rs.getTimestamp(6));
                c.setEndDate(rs.getTimestamp(7));
                c.setSignatureRequired(rs.getBoolean(8));
                c.setSignatureDocumentId(rs.getString(9));
                c.setPrice(rs.getDouble(10));
                c.setMinimum(rs.getInt(11));
                c.setEventType(rs.getBoolean(12));
                c.setEventId(rs.getLong(13));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EventFundSettings addEventFundSettings(final EventFundSettings item) {
        final String insertSQL = "INSERT INTO EVENT_FUND_SETTINGS(customer_id, application_id, fund_id, allowedDaysToAccess, start_date, end_date, signature_required, signature_document_id, price, minimum, event_type, event_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{item.getCustomerId(), item.getApplicationId(), item.getFundId(), item.getAllowedDaysToAccess(), item.getStartDate(), item.getEndDate(), item.getSignatureRequired(), item.getSignatureDocumentId(), item.getPrice(), item.getMinimum(), item.getEventType(), item.getEventId()})
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
    public void removeEventFundSettings(long id) {
        final String updateSQL = "DELETE FROM EVENT_FUND_SETTINGS WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EventFundSettings updateEventFundSettings(EventFundSettings item) {
        final String updateSQL = "UPDATE EVENT_FUND_SETTINGS SET ALLOWEDDAYSTOACCESS = ?, START_DATE = ?, END_DATE = ?, SIGNATURE_REQUIRED = ?, SIGNATURE_DOCUMENT_ID = ?, PRICE = ?, MINIMUM = ?, EVENT_TYPE = ?  WHERE ID = ? AND FUND_ID = ?";
        getJdbcTemplate().update(updateSQL, item.getAllowedDaysToAccess(), item.getStartDate(), item.getEndDate(), item.getSignatureRequired(), item.getSignatureDocumentId(), item.getPrice(), item.getMinimum(), item.getEventType(), item.getId(), item.getFundId());
        return item;
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Map<String, Object>> getPriceByApplicationIdAndEventIdAndApplicationId(long applicationId, long eventId) {
        String querySQL = "select ef.fund_id, ef.price, ef.minimum, ef.capacity, fl.label, fl.lang_id, tl.id as tag_id, tl.name as tag_description from event_fund_settings ef "
        		+ "inner join event e on ef.event_id=e.id "
        		+ "inner join fund f on ef.fund_id=f.id "
        		+ "inner join fund_lang fl on f.id=fl.fund_id "
        		+ "inner join tag_lang tl on fl.tag_id=tl.id "
        		+ "where e.application_id=? and e.id=? ";
        try {
        	List<Map<String, Object>> eventFundSettingsList = new ArrayList<>();
        	List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, applicationId, eventId);
            for (Map<String, Object> m : result) {
            	Map<String, Object> tmp = new HashMap<>();
                tmp.put("fundId", ((Number) m.get("fund_id")).longValue());
                tmp.put("price", ((Number) m.get("price")).doubleValue());
                tmp.put("minimum", ((Number) m.get("minimum")).intValue());
                tmp.put("capacity", ((Number) m.get("capacity")).intValue());
                tmp.put("fundLabel", m.get("label").toString());
                tmp.put("langId", ((Number) m.get("lang_id")).intValue());
                tmp.put("tagId", ((Number) m.get("tag_id")).intValue());
                tmp.put("tagDescription", m.get("tag_description").toString());
                eventFundSettingsList.add(tmp);
            }
            return eventFundSettingsList;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
