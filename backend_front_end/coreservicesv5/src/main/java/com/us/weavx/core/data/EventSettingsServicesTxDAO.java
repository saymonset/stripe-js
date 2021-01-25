package com.us.weavx.core.data;

import com.us.weavx.core.model.Asset;
import com.us.weavx.core.model.EventAsset;
import com.us.weavx.core.model.EventSettings;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("eventSettingsDAO")
public class EventSettingsServicesTxDAO extends JdbcDaoSupport {

    public EventSettingsServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EventAsset addEventAsset(final EventAsset item) {
        final String insertSQL = "INSERT INTO EVENT_ASSET_LANG(ASSET_ID, CUSTOMER_ID, APPLICATION_ID, LANG_ID, ASSET_VALUE, ASSET_PARAMS) VALUES (?,?,?,?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getAssetId(), item.getCustomerId(), item.getApplicationid(),
                    item.getLangId(), item.getAssetValue(), item.getAssetParams()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EventAsset findEventAssetById(int id) {
        String sql = "SELECT ID, ASSET_ID, CUSTOMER_ID, APPLICATION_ID, LANG_ID, ASSET_VALUE, ASSET_PARAMS FROM EVENT_ASSET_LANG WHERE ID = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{id}, (rs, arg1) -> {
                EventAsset c = new EventAsset();
                c.setId(rs.getInt(1));
                c.setAssetId(rs.getInt(2));
                c.setCustomerId(rs.getLong(3));
                c.setApplicationid(rs.getLong(4));
                c.setLangId(rs.getInt(5));
                c.setAssetValue(rs.getString(6));
                c.setAssetParams(rs.getString(7));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EventAsset> findEventAssetsByEventAndLang(long customerId, long applicationId, int langId) {
        final String querySQL = "SELECT ID, ASSET_ID, CUSTOMER_ID, APPLICATION_ID, LANG_ID, ASSET_VALUE, ASSET_PARAMS FROM EVENT_ASSET_LANG WHERE LANG_ID = ? and CUSTOMER_ID = ? AND APPLICATION_ID = ?";
        try {
            ArrayList<EventAsset> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, langId, customerId, applicationId);
            for (Map<String, Object> m : result) {
                EventAsset tmp = new EventAsset();
                tmp.setId(((Number) m.get("id")).longValue());
                tmp.setCustomerId(customerId);
                tmp.setApplicationid(applicationId);
                tmp.setLangId(langId);
                tmp.setAssetId(((Number) m.get("asset_id")).intValue());
                tmp.setAssetValue((String) m.get("asset_value"));
                tmp.setAssetParams((String) m.get("asset_params"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EventAsset> findEventAssetsByEvent(long customerId, long applicationId) {
        final String querySQL = "SELECT ID, ASSET_ID, CUSTOMER_ID, APPLICATION_ID, LANG_ID, ASSET_VALUE, ASSET_PARAMS FROM EVENT_ASSET_LANG WHERE CUSTOMER_ID = ? AND APPLICATION_ID = ? ORDER BY LANG_ID, CUSTOMER_ID, APPLICATION_ID, ASSET_ID";
        try {
            ArrayList<EventAsset> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, customerId, applicationId);
            for (Map<String, Object> m : result) {
                EventAsset tmp = new EventAsset();
                tmp.setId(((Number) m.get("id")).longValue());
                tmp.setCustomerId(customerId);
                tmp.setApplicationid(applicationId);
                tmp.setLangId(((Number) m.get("lang_id")).intValue());
                tmp.setAssetId(((Number) m.get("asset_id")).intValue());
                tmp.setAssetValue((String) m.get("asset_value"));
                tmp.setAssetParams((String) m.get("asset_params"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Asset> listAllAssets() {
        final String querySQL = "SELECT ID, NAME, DESCRIPTION FROM ASSET";
        try {
            ArrayList<Asset> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                Asset tmp = new Asset();
                tmp.setId(((Number) m.get("id")).intValue());
                tmp.setName((String) m.get("name"));
                tmp.setDescription((String) m.get("description"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EventAsset updateEventAsset(EventAsset item) {
        final String updateSQL = "UPDATE EVENT_ASSET_LANG SET ASSET_VALUE = ?, ASSET_PARAMS = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, item.getAssetValue(), item.getAssetParams(), item.getId());
        return item;
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EventSettings> listAllEventByName(String name) {
        final String querySQL = "SELECT ID, NAME, EVENT_ID, KEY, SECRET FROM EVENT_SETTINGS WHERE NAME = ?";
        try {
            ArrayList<EventSettings> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, name);
            for (Map<String, Object> m : result) {
            	EventSettings tmp = new EventSettings();
                tmp.setId(((Number) m.get("id")).intValue());
                tmp.setEventId(((Number) m.get("id")).longValue());
                tmp.setName((String) m.get("name"));
                tmp.setKey((String) m.get("key"));
                tmp.setSecret((String) m.get("secret"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


}
