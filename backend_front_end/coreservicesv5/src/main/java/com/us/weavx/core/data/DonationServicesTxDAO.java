package com.us.weavx.core.data;

import com.us.weavx.core.model.DonationSchedule;
import com.us.weavx.core.model.DonationScheduleStatus;
import com.us.weavx.core.model.ScheduledDonation;
import com.us.weavx.core.model.ScheduledDonationFrequency;
import com.us.weavx.core.model.ScheduledDonationFrequencyDescription;
import com.us.weavx.core.model.ScheduledDonationFundDetail;
import com.us.weavx.core.model.ScheduledDonationHistory;
import com.us.weavx.core.model.ScheduledDonationSettingsLang;
import com.us.weavx.core.model.ScheduledDonationStatus;
import com.us.weavx.core.model.ScheduledDonationStatusDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
import java.util.List;

@Component("donationDAO")
public class DonationServicesTxDAO extends JdbcDaoSupport {

    public DonationServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ScheduledDonationFrequency> listAllScheduledDonationFrequencies() {
        String sql = "select id, name, description, frequency_handler_class from scheduled_donation_frequency";
        try {
            List<ScheduledDonationFrequency> list = getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper<>(ScheduledDonationFrequency.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ScheduledDonationFrequencyDescription> listAllScheduledDonationFrequencyDescriptions() {
        String sql = "select donation_frequency_id, lang_id, label from scheduled_donation_frequency_lang";
        try {
            List<ScheduledDonationFrequencyDescription> list = getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper<>(ScheduledDonationFrequencyDescription.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ScheduledDonation> findScheduledDonationsByCustomerUserId(long customerUserId) {
        String sql = "select id, created_at, starts_at, last_successful, last_failed, next_date, customer_user_id, donation_status_id, donation_frequency_id, amount, lang_id from scheduled_donation where customer_user_id = ? order by id desc";
        try {
            List<ScheduledDonation> list = getJdbcTemplate().query(sql, new Object[]{customerUserId}, new BeanPropertyRowMapper<>(ScheduledDonation.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public ScheduledDonation findScheduledDonationById(long scheduledDonationId) {
        String sql = "select id, created_at, starts_at, last_successful, last_failed, next_date, customer_user_id, donation_status_id, donation_frequency_id, amount, lang_id from scheduled_donation where id = ?";
        try {
            ScheduledDonation result = getJdbcTemplate().queryForObject(sql, new Object[]{scheduledDonationId}, new BeanPropertyRowMapper<>(ScheduledDonation.class));
            return result;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ScheduledDonation registerScheduledDonation(final ScheduledDonation scheduledDonation) {
        final String insertSQL = "insert into scheduled_donation (starts_at, next_date, donation_status_id, customer_user_id, donation_frequency_id, amount, lang_id) values (?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(
                    new Object[]{scheduledDonation.getStarts_at(), scheduledDonation.getNext_date(), scheduledDonation.getDonation_status_id(), scheduledDonation.getCustomer_user_id(), scheduledDonation.getDonation_frequency_id(), scheduledDonation.getAmount(), scheduledDonation.getLang_id()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        scheduledDonation.setId((Long) holder.getKeys().get("id"));
        scheduledDonation.setDonation_status_id((Integer) holder.getKeys().get("donation_status_id"));
        scheduledDonation.setCreated_at((Timestamp) holder.getKeys().get("created_at"));
        return scheduledDonation;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addScheduledDonationFunds(final List<ScheduledDonationFundDetail> funds, final long scheduledDonationId) {
        String insertSQL = "insert into SCHEDULED_DONATION_FUND(scheduled_donation_id, fund_id, amount) values (?,?,?)";
        getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement arg0, int arg1) throws SQLException {
                // TODO Auto-generated method stub
                ScheduledDonationFundDetail tmp = funds.get(arg1);
                new ArgumentPreparedStatementSetter(new Object[]{scheduledDonationId, tmp.getFund_id(), tmp.getAmount()})
                        .setValues(arg0);
            }

            @Override
            public int getBatchSize() {
                return funds.size();
            }
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ScheduledDonationFundDetail> findScheduledDonationFunds(long scheduledDonationId) {
        String sql = "select scheduled_donation_id, fund_id, amount from scheduled_donation_fund where scheduled_donation_id = ?";
        try {
            List<ScheduledDonationFundDetail> funds = getJdbcTemplate().query(sql, new Object[]{scheduledDonationId}, new BeanPropertyRowMapper<ScheduledDonationFundDetail>(ScheduledDonationFundDetail.class));
            return funds;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ScheduledDonationStatus> listAllScheduledDonationStatus() {
        String sql = "select id, name, description from scheduled_donation_status";
        try {
            List<ScheduledDonationStatus> list = getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper<>(ScheduledDonationStatus.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ScheduledDonationStatusDescription> listAllScheduledDonationStatusDescriptions() {
        String sql = "select donation_status_id, lang_id, label from scheduled_donation_status_lang";
        try {
            List<ScheduledDonationStatusDescription> list = getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper<>(ScheduledDonationStatusDescription.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateScheduleDonationStatus(ScheduledDonation d) {
        final String sql = "update scheduled_donation set donation_status_id = ? where id = ? and customer_user_id = ?";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            new ArgumentPreparedStatementSetter(new Object[]{d.getDonation_status_id(), d.getId(), d.getCustomer_user_id()}).setValues(ps);
            return ps;
        };
        getJdbcTemplate().update(psc);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public DonationSchedule registerDonationSchedule(final DonationSchedule newItem) {
        final String insertSQL = "insert into donation_scheduler (scheduled_donation_id, scheduled_date, scheduled_status) values (?,?,?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(
                    new Object[]{newItem.getScheduled_donation_id(), newItem.getScheduled_date(), newItem.getScheduled_status()})
                    .setValues(ps);
            return ps;
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        newItem.setId((Long) holder.getKeys().get("id"));
        return newItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ScheduledDonationSettingsLang> listAllScheduledDonationSettingsLang() {
        String sql = "select customer_id, application_id, lang_id, created_email_template, created_email_row_template, created_email_subject, created_email_from, created_email_from_name, confirmation_before_email_template, confirmation_before_email_subject, confirmation_before_email_from, confirmation_before_email_from_name, status_change_email_template, status_change_email_subject, status_change_email_from, status_change_email_from_name, failed_email_template, failed_email_subject, failed_email_from, failed_email_from_name from scheduled_donation_settings_lang";
        try {
            List<ScheduledDonationSettingsLang> list = getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper<>(ScheduledDonationSettingsLang.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<DonationSchedule> findDonationsScheduledBetween(Timestamp from, Timestamp to) {
        String sql = "select id, scheduled_donation_id, scheduled_date, scheduled_status, executed_at from donation_scheduler where scheduled_date >= ? and scheduled_date < ? and scheduled_status = ?";
        try {
            List<DonationSchedule> list = getJdbcTemplate().query(sql, new Object[]{from, to, DonationScheduleStatus.PENDING}, new BeanPropertyRowMapper<>(DonationSchedule.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateDonationSchedule(DonationSchedule d) {
        final String sql = "update donation_scheduler set scheduled_date = ?, scheduled_status = ?, executed_at = ? where scheduled_donation_id = ?";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            new ArgumentPreparedStatementSetter(new Object[]{d.getScheduled_date(), d.getScheduled_status(), d.getExecuted_at(), d.getScheduled_donation_id()}).setValues(ps);
            return ps;
        };
        getJdbcTemplate().update(psc);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateDonation(ScheduledDonation d) {
        final String sql = "update scheduled_donation set last_successful = ?, last_failed = ?, next_date = ? where id = ?";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            new ArgumentPreparedStatementSetter(new Object[]{d.getLast_successful(), d.getLast_failed(), d.getNext_date(), d.getId()}).setValues(ps);
            return ps;
        };
        getJdbcTemplate().update(psc);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ScheduledDonationHistory registerDonationScheduleHistory(final ScheduledDonationHistory newItem) {
        final String insertSQL = "insert into scheduled_donation_history(scheduled_donation_id, executed_at, transaction_id, success_donation) values (?,?,?,?)";
        Timestamp executedAt = (newItem.getExecuted_at() == null ? new Timestamp(System.currentTimeMillis()) : newItem.getExecuted_at());
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            new ArgumentPreparedStatementSetter(
                    new Object[]{newItem.getScheduled_donation_id(), executedAt, newItem.getTransaction_id(), newItem.isSuccess_donation()})
                    .setValues(ps);
            return ps;
        };
        int records = getJdbcTemplate().update(psc);
        if (records == 0) {
            return null;
        }
        return newItem;
    }

}
