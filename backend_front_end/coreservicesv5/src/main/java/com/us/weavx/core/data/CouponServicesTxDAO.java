package com.us.weavx.core.data;

import com.us.weavx.core.model.Coupon;
import com.us.weavx.core.model.CouponLog;
import com.us.weavx.core.model.CouponPromotion;
import com.us.weavx.core.model.CouponPromotionCustomerAppRestriction;
import com.us.weavx.core.model.TransactionCouponApplication;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("couponDAO")
public class CouponServicesTxDAO extends JdbcDaoSupport {

    public CouponServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Coupon addCoupon(final Coupon item) {
        final String insertSQL = "INSERT INTO COUPON(CODE, COUPON_STATUS_ID, APPLIER, COUPON_PROMOTION_ID) VALUES (?,?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(new Object[]{item.getCode(), item.getCouponStatusId(), item.getApplier(), item.getCouponPromotionid()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setId((Long) holder.getKeys().get("id"));
        return item;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Coupon updateCoupon(Coupon item) {
        final String updateSQL = "UPDATE COUPON SET COUPON_STATUS_ID = ?, APPLIER = ?, APPLIED_TIMES = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getCouponStatusId(), item.getApplier(), item.getAppliedTimes(), item.getId()});
        return item;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Coupon findCouponByCode(String code) {
        String sql = "SELECT ID, CODE, COUPON_STATUS_ID, APPLIER, APPLIED_TIMES, COUPON_PROMOTION_ID FROM COUPON WHERE CODE = ?";
        try {
            Coupon item = getJdbcTemplate().queryForObject(sql, new Object[]{code}, new RowMapper<Coupon>() {

                public Coupon mapRow(ResultSet rs, int arg1) throws SQLException {
                    Coupon c = new Coupon();
                    c.setId(rs.getLong(1));
                    c.setCode(code);
                    c.setCouponStatusId(rs.getInt(3));
                    c.setApplier(rs.getString(4));
                    c.setAppliedTimes(rs.getLong(5));
                    c.setCouponPromotionid(rs.getLong(6));
                    return c;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Coupon findCouponById(long id) {
        String sql = "SELECT ID, CODE, COUPON_STATUS_ID, APPLIER, APPLIED_TIMES, COUPON_PROMOTION_ID FROM COUPON WHERE ID = ?";
        try {
            Coupon item = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<Coupon>() {

                public Coupon mapRow(ResultSet rs, int arg1) throws SQLException {
                    Coupon c = new Coupon();
                    c.setId(id);
                    c.setCode("*******");
                    c.setCouponStatusId(rs.getInt(3));
                    c.setApplier(rs.getString(4));
                    c.setAppliedTimes(rs.getLong(5));
                    c.setCouponPromotionid(rs.getLong(6));
                    return c;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CouponLog addCouponLog(final CouponLog item) {
        final String insertSQL = "INSERT INTO COUPON_LOG (COUPON_STATUS_ID, COUPON_EVENT_ID, COUPON_ID, USER_EMAIL, USER_AGENT, IP_ADDRESS) VALUES (?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{item.getCouponStatusId(), item.getCouponEventId(), item.getCouponId(), item.getUserEmail(), item.getUserAgent(), item.getIpAddress()})
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
    public CouponPromotion addCouponPromotion(final CouponPromotion item) {
        final String insertSQL = "INSERT INTO COUPON_PROMOTION(NAME, CREATED_BY_CUSTOMER_ID, DISCOUNT_TYPE_ID, DISCOUNT_AMOUNT, MAX_COUPONS, VALID_FROM, VALID_TO, APPLICATION_LIMIT, USER_APPLICATION_LIMIT, IS_GENERIC, IS_ENABLED) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{item.getName(), item.getCreatedByCustomerId(), item.getDiscountTypeId(), item.getDiscountAmount(), item.getMaxCoupons(), item.getValidFrom(), item.getValidTo(), item.getApplicationLimit(), item.getUserApplicationLimit(), item.isGeneric(), item.isEnabled()})
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
    public CouponPromotion updateCouponPromotion(CouponPromotion item) {
        final String updateSQL = "UPDATE COUPON_PROMOTION SET NAME = ?, DISCOUNT_TYPE_ID = ?, DISCOUNT_AMOUNT = ?, MAX_COUPONS = ?, CURRENT_COUPONS = ?, VALID_FROM = ?, VALID_TO = ?, APPLICATION_LIMIT = ?, USER_APPLICATION_LIMIT = ?, IS_GENERIC = ?, IS_ENABLED = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{item.getName(), item.getDiscountTypeId(), item.getDiscountAmount(), item.getMaxCoupons(), item.getCurrentCoupons(), item.getValidFrom(), item.getValidTo(), item.getApplicationLimit(), item.getUserApplicationLimit(), item.isGeneric(), item.isEnabled(), item.getId()});
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CouponPromotion findCouponPromotionById(long id) {
        String sql = "SELECT ID, NAME, CREATED_BY_CUSTOMER_ID, CREATED_AT, DISCOUNT_TYPE_ID, DISCOUNT_AMOUNT, MAX_COUPONS, CURRENT_COUPONS, VALID_FROM, VALID_TO, APPLICATION_LIMIT, USER_APPLICATION_LIMIT, IS_GENERIC, IS_ENABLED FROM COUPON_PROMOTION WHERE ID = ?";
        try {
            CouponPromotion item = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<CouponPromotion>() {

                public CouponPromotion mapRow(ResultSet rs, int arg1) throws SQLException {
                    CouponPromotion item = new CouponPromotion();
                    item.setId(id);
                    item.setName(rs.getString(2));
                    item.setCreatedByCustomerId(rs.getLong(3));
                    item.setCreatedAt(rs.getTimestamp(4));
                    item.setDiscountTypeId(rs.getInt(5));
                    item.setDiscountAmount(rs.getDouble(6));
                    item.setMaxCoupons(rs.getLong(7));
                    item.setCurrentCoupons(rs.getLong(8));
                    item.setValidFrom(rs.getTimestamp(9));
                    item.setValidTo(rs.getTimestamp(10));
                    item.setApplicationLimit(rs.getLong(11));
                    item.setUserApplicationLimit(rs.getLong(12));
                    item.setGeneric(rs.getBoolean(13));
                    item.setEnabled(rs.getBoolean(14));
                    return item;
                }

            });
            return item;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CouponPromotionCustomerAppRestriction> findCouponPromotionCustomerAppRestrictionByCouponPromotionId(long couponPromotionId) {
        String querySQL = "select customer_id, coupon_promotion_id, created_at, application_id from COUPON_PROMOTION_CUSTOMER_APP_RESTRICTION where coupon_promotion_id = ?";
        try {
            ArrayList<CouponPromotionCustomerAppRestriction> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{couponPromotionId});
            for (Map<String, Object> m : result) {
                CouponPromotionCustomerAppRestriction item = new CouponPromotionCustomerAppRestriction();
                item.setCouponPromotionId(couponPromotionId);
                item.setCustomerId(((Number) m.get("customer_id")).longValue());
                item.setCreatedAt((Timestamp) m.get("created_at"));
                item.setApplicationId(((Number) m.get("application_id")).longValue());
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Long findCouponApplicationsByUserEmail(long couponId, String applier) {
        String sql = "select count(*) from TRANSACTION_COUPON_APPLICATION where coupon_id = ? and applier = ?";
        try {
            Long count = getJdbcTemplate().queryForObject(sql, new Object[]{couponId, applier}, new RowMapper<Long>() {

                public Long mapRow(ResultSet rs, int arg1) throws SQLException {
                    return new Long(rs.getLong(1));
                }

            });
            return count;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionCouponApplication addTransactionCoupon(final TransactionCouponApplication item) {
        final String insertSQL = "INSERT INTO TRANSACTION_COUPON_APPLICATION (TRANSACTION_ID, COUPON_ID, APPLIER) VALUES (?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{item.getTransactionId(), item.getCouponId(), item.getApplier()})
                        .setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        item.setAppliedAt((Timestamp) holder.getKeys().get("applied_at"));
        return item;
    }

}
