package com.us.weavx.core.data;

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

import com.us.weavx.core.model.Venue;

@Component("venuesDAO")
public class VenueServicesTxDAO extends JdbcDaoSupport {

	public VenueServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Venue addVenue(final Venue item) {
		final String insertSQL = "INSERT INTO VENUE(EVENT_ID, DESCRIPTION, ADDRESS, CAPACITY) VALUES (?,?,?,?)";
		PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getEventId(), item.getDescription(), item.getAddress(), item.getCapacity()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
		return item;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Venue updateVenue(Venue item) {
        final String updateSQL = "UPDATE VENUE SET EVENT_ID = ?, DESCRIPTION = ?, ADDRESS = ?, CAPACITY = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getEventId(), item.getDescription(), item.getAddress(), item.getCapacity(), item.getId()});
        return item;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Venue findVenueById(long id) {
        String sql = "SELECT ID, EVENT_ID, DESCRIPTION, ADDRESS, CAPACITY FROM VENUE WHERE ID = ?";
        try {
            Venue item = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<Venue>() {

                public Venue mapRow(ResultSet rs, int arg1) throws SQLException {
                    Venue v = new Venue();
                    v.setId(id);
                    v.setEventId(rs.getInt(2));
                    v.setDescription(rs.getString(3));
                    v.setAddress(rs.getString(4));
                    v.setCapacity(rs.getInt(5));
                    return v;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Venue findVenueByEventId(long eventId) {
        String sql = "SELECT ID, EVENT_ID, DESCRIPTION, ADDRESS, CAPACITY FROM VENUE WHERE EVENT_ID = ?";
        try {
            Venue item = getJdbcTemplate().queryForObject(sql, new Object[]{eventId}, new RowMapper<Venue>() {

                public Venue mapRow(ResultSet rs, int arg1) throws SQLException {
                    Venue v = new Venue();
                    v.setId(rs.getInt(1));
                    v.setEventId(eventId);
                    v.setDescription(rs.getString(3));
                    v.setAddress(rs.getString(4));
                    v.setCapacity(rs.getInt(5));
                    return v;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Venue> listAllVenues() {
		String querySQL = "SELECT ID, EVENT_ID, DESCRIPTION, ADDRESS, CAPACITY FROM VENUE";
        try {
            ArrayList<Venue> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{});
            for (Map<String, Object> m : result) {
                Venue item = new Venue();
                item.setId(((Number) m.get("id")).longValue());
                item.setEventId(((Number) m.get("event_id")).intValue());
                item.setDescription((String) m.get("description"));
                item.setAddress((String) m.get("address"));
                item.setCapacity(((Number) m.get("capacity")).intValue());
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
