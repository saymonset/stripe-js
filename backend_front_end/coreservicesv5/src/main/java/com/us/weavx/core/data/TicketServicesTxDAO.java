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

import com.us.weavx.core.model.Ticket;
import com.us.weavx.core.model.TicketHistory;
import com.us.weavx.core.model.TicketStatus;

@Component("ticketsDAO")
public class TicketServicesTxDAO extends JdbcDaoSupport {

	public TicketServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Ticket addTicket(final Ticket item) {
		final String insertSQL = "INSERT INTO TICKET(TICKET_SERIAL, ASSISTANT_ID, STATUS_ID) VALUES (?,?,?)";
		PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getTicketSerial(), item.getAssistantId(), item.getStatusId()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
		return item;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Ticket updateTicket(Ticket item) {
        final String updateSQL = "UPDATE TICKET SET TICKET_SERIAL = ?, ASSISTANT_ID = ?, STATUS_ID = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getTicketSerial(), item.getAssistantId(), item.getStatusId()});
        return item;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Ticket findTicketById(long id) {
        String sql = "SELECT ID, TICKET_SERIAL, ASSISTANT_ID, STATUS_ID FROM TICKET WHERE ID = ?";
        try {
        	Ticket item = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<Ticket>() {

                public Ticket mapRow(ResultSet rs, int arg1) throws SQLException {
                	Ticket t = new Ticket();
                    t.setId(id);
                    t.setTicketSerial(rs.getString(2));
                    t.setAssistantId(rs.getInt(3));
                    t.setStatusId(rs.getInt(4));
                    return t;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Ticket> listAllTickets() {
		String querySQL = "SELECT ID, TICKET_SERIAL, ASSISTANT_ID, STATUS_ID FROM TICKET";
        try {
            ArrayList<Ticket> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{});
            for (Map<String, Object> m : result) {
                Ticket item = new Ticket();
                item.setId(((Number) m.get("id")).longValue());
                item.setTicketSerial((String) m.get("ticket_serial"));
                item.setAssistantId(((Number) m.get("assistant_id")).intValue());
                item.setStatusId(((Number) m.get("status_id")).intValue());
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public TicketHistory addTicketHistory(final TicketHistory item) {
		final String insertSQL = "INSERT INTO TICKET_HISTORY(TICKET_ID, HISTORY_DATE, STATUS_ID, STATUS_NAME) VALUES (?,?,?,?)";
		PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getTicketId(), item.getHistoryDate(), item.getStatusId(), item.getStatusName()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
		return item;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TicketHistory updateTicketHistory(TicketHistory item) {
        final String updateSQL = "UPDATE TICKET_HISTORY SET TICKET_ID = ?, HISTORY_DATE = ?, STATUS_ID = ?, STATUS_NAME = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getTicketId(), item.getHistoryDate(), item.getStatusId(), item.getStatusName()});
        return item;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public TicketHistory findTicketHistoryById(long id) {
        String sql = "SELECT ID, TICKET_ID, HISTORY_DATE, STATUS_ID, STATUS_NAME, CREATED_AT FROM TICKET_HISTORY WHERE ID = ?";
        try {
        	TicketHistory item = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<TicketHistory>() {

                public TicketHistory mapRow(ResultSet rs, int arg1) throws SQLException {
                	TicketHistory t = new TicketHistory();
                    t.setId(id);
                    t.setTicketId(rs.getInt(2));
                    t.setHistoryDate(rs.getDate(3));
                    t.setStatusId(rs.getInt(4));
                    t.setStatusName(rs.getString(5));
                    t.setCreatedAt(rs.getDate(6));
                    return t;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TicketHistory> listAllTicketHistory() {
		String querySQL = "SELECT ID, TICKET_ID, HISTORY_DATE, STATUS_ID, STATUS_NAME, CREATED_AT FROM TICKET_HISTORY";
        try {
            ArrayList<TicketHistory> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{});
            for (Map<String, Object> m : result) {
                TicketHistory item = new TicketHistory();
                item.setId(((Number) m.get("id")).longValue());
                item.setTicketId(((Number) m.get("ticket_id")).intValue());
                item.setHistoryDate((Date) m.get("history_date"));
                item.setStatusId(((Number) m.get("status_id")).intValue());
                item.setStatusName((String) m.get("status_name"));
                item.setCreatedAt((Date) m.get("created_at"));
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public TicketStatus addTicketHistory(final TicketStatus item) {
		final String insertSQL = "INSERT INTO TICKET_STATUS(STATUS_NAME) VALUES (?)";
		PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getStatusName()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Integer) holder.getKeys().get("id"));
		return item;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TicketStatus updateTicketStatus(TicketStatus item) {
        final String updateSQL = "UPDATE TICKET_STATUS SET STATUS_NAME = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getStatusName()});
        return item;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public TicketStatus findTicketStatusById(int id) {
        String sql = "SELECT ID, STATUS_NAME FROM TICKET_STATUS WHERE ID = ?";
        try {
        	TicketStatus item = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<TicketStatus>() {

                public TicketStatus mapRow(ResultSet rs, int arg1) throws SQLException {
                	TicketStatus t = new TicketStatus();
                    t.setId(id);
                    t.setStatusName(rs.getString(2));
                    return t;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public TicketStatus findTicketStatusByName(String name) {
        String sql = "SELECT ID, STATUS_NAME FROM TICKET_STATUS WHERE STATUS_NAME = ?";
        try {
        	TicketStatus item = getJdbcTemplate().queryForObject(sql, new Object[]{name}, new RowMapper<TicketStatus>() {

                public TicketStatus mapRow(ResultSet rs, int arg1) throws SQLException {
                	TicketStatus t = new TicketStatus();
                    t.setId(rs.getInt(1));
                    t.setStatusName(rs.getString(2));
                    return t;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TicketStatus> listAllTicketStatus() {
		String querySQL = "SELECT ID, STATUS_NAME FROM TICKET_STATUS";
        try {
            ArrayList<TicketStatus> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{});
            for (Map<String, Object> m : result) {
                TicketStatus item = new TicketStatus();
                item.setId(((Number) m.get("id")).intValue());
                item.setStatusName((String) m.get("status_name"));
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
