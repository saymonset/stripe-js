package com.us.weavx.core.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

import com.us.weavx.core.model.Event;

@Component("eventsDAO")
public class EventServicesTxDAO extends JdbcDaoSupport {

	public EventServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Event addEvent(final Event item) {
		final String insertSQL = "INSERT INTO EVENT(APPLICATION_ID, NAME, SPEAKER, START_DATE, END_DATE) VALUES (?,?,?,?,?)";
		PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getApplicationId(), item.getName(), item.getSpeaker(), item.getStartDate(), item.getEndDate()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
		return item;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Event updateEvent(Event item) {
        final String updateSQL = "UPDATE EVENT SET APPLICATION_ID = ?, NAME = ?, SPEAKER = ?, START_DATE = ?, END_DATE = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getApplicationId(), item.getName(), item.getSpeaker(), item.getStartDate(), item.getEndDate(), item.getId()});
        return item;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Event findEventById(long id) {
        String sql = "SELECT ID, APPLICATION_ID, NAME, SPEAKER, START_DATE, END_DATE FROM EVENT WHERE ID = ?";
        try {
            Event item = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<Event>() {

                public Event mapRow(ResultSet rs, int arg1) throws SQLException {
                    Event e = new Event();
                    e.setId(id);
                    e.setApplicationId(rs.getLong(2));
                    e.setName(rs.getString(3));
                    e.setSpeaker(rs.getString(4));
                    e.setStartDate(rs.getDate(5));
                    e.setEndDate(rs.getDate(6));
                    return e;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Event findEventByIdAndApplicationId(long id, long applicactionId) {
        String sql = "SELECT ID, APPLICATION_ID, NAME, SPEAKER, START_DATE, END_DATE FROM EVENT WHERE ID = ? AND APPLICATION_ID = ?";
        try {
            Event item = getJdbcTemplate().queryForObject(sql, new Object[]{id, applicactionId}, new RowMapper<Event>() {

                public Event mapRow(ResultSet rs, int arg1) throws SQLException {
                    Event e = new Event();
                    e.setId(id);
                    e.setApplicationId(applicactionId);
                    e.setName(rs.getString(3));
                    e.setSpeaker(rs.getString(4));
                    e.setStartDate(rs.getDate(5));
                    e.setEndDate(rs.getDate(6));
                    return e;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Event> listAllEvents() {
		String querySQL = "SELECT ID, APPLICATION_ID, NAME, SPEAKER, START_DATE, END_DATE FROM EVENT";
        try {
            ArrayList<Event> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{});
            for (Map<String, Object> m : result) {
                Event item = new Event();
                item.setId(((Number) m.get("id")).longValue());
                item.setApplicationId(((Number) m.get("application_id")).longValue());
                item.setName((String) m.get("name"));
                item.setSpeaker((String) m.get("speaker"));
                item.setStartDate((Date) m.get("start_date"));
                item.setEndDate((Date) m.get("end_date"));
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public int countTicketsAvalaible(long eventId, long applicationId) {
		String querySQL = "select count(a.*) from transaction t "
				+ "inner join application ap on t.application_id = ap.id "
				+ "inner join event e on ap.id = e.application_id "
				+ "inner join assistant a on t.id = a.transaction_id "
				+ "inner join transaction_status ts on t.tx_status_id = ts.id "
				+ "where ts.name = 'SUCCESS_TX' and e.id=? and t.application_id=?";
		try {
			Integer resultado = getJdbcTemplate().queryForObject(querySQL, new Object[]{eventId, applicationId}, new RowMapper<Integer>() {
				public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
                    return rs.getInt(1);
                }
            });
			return resultado;
		} catch (Exception e) {
			return 0;
		}
	}
}
