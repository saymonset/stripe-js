package com.us.weavx.core.data;

import com.us.weavx.core.model.BlackListItem;
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

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("blacklistDAO")
public class BlackListServicesTxDAO extends JdbcDaoSupport {

    public BlackListServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public BlackListItem addBlacklistItem(final BlackListItem item) {
        final String insertSQL = "INSERT INTO BLACKLIST(DATA, DATA_TYPE_ID, BLOCKED_BY) VALUES (?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getData(), item.getDataTypeId(), item.getBlockedBy()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
        return item;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void cleanBLackListItem(BlackListItem item) {
        final String updateSQL = "UPDATE BLACKLIST SET IS_ACTIVE = false, CLEANED_AT = current_timestamp WHERE id = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getId()});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public BlackListItem findBlackListItemById(long id) {
        String sql = "SELECT ID, BLOCKED_AT, DATA, DATA_TYPE_ID, BLOCKED_BY, IS_ACTIVE, CLEANED_AT FROM BLACKLIST WHERE ID = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{id}, (rs, arg1) -> {
                BlackListItem i = new BlackListItem();
                i.setId(id);
                i.setBlockedAt(rs.getTimestamp(2));
                i.setData(rs.getString(3));
                i.setDataTypeId(rs.getInt(4));
                i.setBlockedBy(rs.getLong(5));
                i.setActive(rs.getBoolean(6));
                i.setCleanedAt(rs.getTimestamp(7));
                return i;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public BlackListItem findBlackListItemByDataTypeIdAndData(int dataTypeId, String data) {
        String sql = "SELECT ID, BLOCKED_AT, DATA, DATA_TYPE_ID, BLOCKED_BY, IS_ACTIVE, CLEANED_AT FROM BLACKLIST WHERE DATA_TYPE_ID = ? AND DATA = ? AND IS_ACTIVE";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{dataTypeId, data}, (rs, arg1) -> {
                BlackListItem i = new BlackListItem();
                i.setId(rs.getLong(1));
                i.setBlockedAt(rs.getTimestamp(2));
                i.setData(data);
                i.setDataTypeId(dataTypeId);
                i.setBlockedBy(rs.getLong(5));
                i.setActive(rs.getBoolean(6));
                i.setCleanedAt(rs.getTimestamp(7));
                return i;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<BlackListItem> listAllBlackListItems() {
        String querySQL = "SELECT ID, BLOCKED_AT, DATA, DATA_TYPE_ID, BLOCKED_BY, IS_ACTIVE, CLEANED_AT FROM BLACKLIST WHERE IS_ACTIVE ORDER BY ID";
        try {
            ArrayList<BlackListItem> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                BlackListItem itemTmp = new BlackListItem();
                Number tmp = null;
                itemTmp.setId(((Number) m.get("id")).longValue());
                itemTmp.setBlockedAt((Timestamp) m.get("blocked_at"));
                itemTmp.setData((String) m.get("data"));
                tmp = (Number) m.get("data_type_id");
                tmp = (tmp == null) ? -1 : tmp;
                itemTmp.setDataTypeId(tmp.intValue());
                tmp = (Number) m.get("blocked_by");
                tmp = (tmp == null) ? -1 : tmp;
                itemTmp.setBlockedBy(tmp.longValue());
                itemTmp.setActive((Boolean) m.get("is_active"));
                itemTmp.setCleanedAt((Timestamp) m.get("cleanedAt"));
                list.add(itemTmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<BlackListItem> findItemsBlockedByCleanedItem(long cleanedItemId) {
        String querySQL = "SELECT ID, BLOCKED_AT, DATA, DATA_TYPE_ID, BLOCKED_BY, IS_ACTIVE, CLEANED_AT FROM BLACKLIST WHERE IS_ACTIVE AND BLOCKED_BY = ? ORDER BY ID";
        try {
            ArrayList<BlackListItem> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{cleanedItemId});
            for (Map<String, Object> m : result) {
                BlackListItem itemTmp = new BlackListItem();
                Number tmp;
                itemTmp.setId(((Number) m.get("id")).longValue());
                itemTmp.setBlockedAt((Timestamp) m.get("blocked_at"));
                itemTmp.setData((String) m.get("data"));
                tmp = (Number) m.get("data_type_id");
                tmp = (tmp == null) ? -1 : tmp;
                itemTmp.setDataTypeId(tmp.intValue());
                tmp = (Number) m.get("blocked_by");
                tmp = (tmp == null) ? -1 : tmp;
                itemTmp.setBlockedBy(tmp.longValue());
                itemTmp.setActive((Boolean) m.get("is_active"));
                itemTmp.setCleanedAt((Timestamp) m.get("cleanedAt"));
                list.add(itemTmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}
