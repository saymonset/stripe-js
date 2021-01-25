package com.us.weavx.core.data;

import java.sql.PreparedStatement;
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
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.model.EventRRSS;

@Component("eventsRRSSDAO")
public class EventRRSSServicesTxDAO extends JdbcDaoSupport {

	public EventRRSSServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public EventRRSS addEvent(final EventRRSS item) {
		final String insertSQL = "INSERT INTO EVENT_RRSS(ID, EVENT_ID, URL, IMAGE, DESCRIPTION) VALUES VALUES (?,?,?,?,?)";
		PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getId(), item.getEventId(), item.getUrl(), item.getImage(), item.getDescription()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
		return item;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EventRRSS updateEvent(EventRRSS item) {
        final String updateSQL = "UPDATE EVENT_RRSS SET EVENT_ID = ?, URL = ?, IMAGE = ?, DESCRIPTION = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getEventId(), item.getUrl(), item.getImage(), item.getDescription(), item.getId()});
        return item;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EventRRSS> findByEventByIdAndApplicationId(long eventId, long applicactionId) {
        String querySQL = "select r.id, r.event_id, r.url, r.image, r.description, r.created_at from event_rrss r "
        		+ "inner join event e on r.event_id = e.id "
        		+ "where EVENT_ID = ? AND APPLICATION_ID = ?";
        try {
        	ArrayList<EventRRSS> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{eventId, applicactionId});
            for (Map<String, Object> m : result) {
                EventRRSS item = new EventRRSS();
                item.setId(((Number) m.get("id")).longValue());
                item.setEventId(((Number) m.get("event_id")).longValue());
                item.setUrl((String) m.get("url"));
                item.setImage((String) m.get("image"));
                item.setDescription((String) m.get("description"));
                item.setCreatedAt((Date) m.get("created_at"));
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
