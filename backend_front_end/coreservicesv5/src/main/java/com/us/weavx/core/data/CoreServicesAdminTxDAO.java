package com.us.weavx.core.data;

import com.us.weavx.core.model.AdminFunction;
import com.us.weavx.core.model.AdminRole;
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("adminDAO")
public class CoreServicesAdminTxDAO extends JdbcDaoSupport {

    public CoreServicesAdminTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public AdminRole addAdminRole(final AdminRole item) {
        final String insertSQL = "INSERT INTO ADMIN_ROLE(ID, NAME, DESCRIPTION) VALUES (?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getId(), item.getName(), item.getDescription()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setCreatedAt((Timestamp) holder.getKeys().get("created_at"));
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public AdminRole findAdminRoleById(int id) {
        String sql = "SELECT ID, NAME, DESCRIPTION, CREATED_AT FROM ADMIN_ROLE WHERE ID = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{id}, (rs, arg1) -> {
                AdminRole c = new AdminRole();
                c.setId(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setDescription(rs.getString(3));
                c.setCreatedAt(rs.getTimestamp(4));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<AdminFunction> findRoleAdminFunctions(int adminRoleId) {
        final String querySQL = "SELECT F.ID, F.NAME, F.DESCRIPTION, F.CREATED_AT FROM ADMIN_FUNCTION F, ADMIN_ROLE_FUNCTION R WHERE R.ADMIN_FUNCTION_ID = F.ID AND R.ADMIN_ROLE_ID = ?";
        try {
            ArrayList<AdminFunction> roleAdminFunctions = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, adminRoleId);
            for (Map<String, Object> m : result) {
                AdminFunction tmp = new AdminFunction();
                tmp.setId(((Number) m.get("id")).intValue());
                tmp.setName((String) m.get("name"));
                tmp.setDescription((String) m.get("description"));
                tmp.setCreatedAt((Timestamp) m.get("created_at"));
                roleAdminFunctions.add(tmp);
            }
            return roleAdminFunctions;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<AdminRole> findCustomerUserAdminRoles(long customerUserId) {
        final String querySQL = "SELECT R.ID, R.NAME, R.DESCRIPTION, R.CREATED_AT FROM ADMIN_ROLE R, CUSTOMER_USER_ADMIN_ROLE CR WHERE R.ID = CR.ADMIN_ROLE_ID AND CR.CUSTOMER_USER_ID = ?";
        try {
            ArrayList<AdminRole> customerUserRoles = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, customerUserId);
            for (Map<String, Object> m : result) {
                AdminRole tmp = new AdminRole();
                tmp.setId(((Number) m.get("id")).intValue());
                tmp.setName((String) m.get("name"));
                tmp.setDescription((String) m.get("description"));
                tmp.setCreatedAt((Timestamp) m.get("created_at"));
                customerUserRoles.add(tmp);
            }
            return customerUserRoles;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<AdminRole> listAllAdminRoles() {
        final String querySQL = "SELECT R.ID, R.NAME, R.DESCRIPTION, R.CREATED_AT FROM ADMIN_ROLE R";
        try {
            ArrayList<AdminRole> adminRoles = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                AdminRole tmp = new AdminRole();
                tmp.setId(((Number) m.get("id")).intValue());
                tmp.setName((String) m.get("name"));
                tmp.setDescription((String) m.get("description"));
                tmp.setCreatedAt((Timestamp) m.get("created_at"));
                adminRoles.add(tmp);
            }
            return adminRoles;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public AdminFunction addAdminFunction(final AdminFunction item) {
        final String insertSQL = "INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getId(), item.getName(), item.getDescription()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setCreatedAt((Timestamp) holder.getKeys().get("created_at"));
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public AdminFunction findAdminFunctionById(int id) {
        String sql = "SELECT ID, NAME, DESCRIPTION, CREATED_AT FROM ADMIN_FUNCTION WHERE ID = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{id}, (rs, arg1) -> {
                AdminFunction c = new AdminFunction();
                c.setId(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setDescription(rs.getString(3));
                c.setCreatedAt(rs.getTimestamp(4));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void associateAdminFunctionsToRole(final List<AdminFunction> adminFunctions, final int adminRoleId) {
        String insertSQL = "INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (?,?)";
        getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement arg0, int arg1) throws SQLException {
                AdminFunction tmp = adminFunctions.get(arg1);
                new ArgumentPreparedStatementSetter(new Object[]{adminRoleId, tmp.getId()})
                        .setValues(arg0);
            }

            @Override
            public int getBatchSize() {
                return adminFunctions.size();
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void associateAdminRolesToCustomerUser(final List<AdminRole> adminRoles, final long customerUserId) {
        String insertSQL = "INSERT INTO CUSTOMER_USER_ADMIN_ROLE(CUSTOMER_USER_ID, ADMIN_ROLE_ID) VALUES (?,?)";
        getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement arg0, int arg1) throws SQLException {
                AdminRole tmp = adminRoles.get(arg1);
                new ArgumentPreparedStatementSetter(new Object[]{customerUserId, tmp.getId()})
                        .setValues(arg0);
            }

            @Override
            public int getBatchSize() {
                return adminRoles.size();
            }
        });
    }


}
