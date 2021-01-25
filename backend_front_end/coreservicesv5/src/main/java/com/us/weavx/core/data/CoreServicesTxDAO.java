package com.us.weavx.core.data;

import com.us.weavx.core.constants.TokenStatus;
import com.us.weavx.core.exception.ObjectDoesNotExistsException;
import com.us.weavx.core.exception.UnauthorizedAccessException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AccessKey;
import com.us.weavx.core.model.AccessToken;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.ApplicationParameter;
import com.us.weavx.core.model.City;
import com.us.weavx.core.model.Continent;
import com.us.weavx.core.model.Country;
import com.us.weavx.core.model.CreditCardPaymentData;
import com.us.weavx.core.model.CustomerDefaultPaymentGateway;
import com.us.weavx.core.model.CustomerIdentityProvider;
import com.us.weavx.core.model.CustomerPaymentGateway;
import com.us.weavx.core.model.CustomerProperty;
import com.us.weavx.core.model.CustomerSupportedExternalPaymentType;
import com.us.weavx.core.model.CustomerSupportedPaymentType;
import com.us.weavx.core.model.CustomerSystemMessage;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserPaymentProfile;
import com.us.weavx.core.model.CustomerUserPurchaseInfo;
import com.us.weavx.core.model.CustomerUserWIthEmail;
import com.us.weavx.core.model.Developer;
import com.us.weavx.core.model.DeveloperKey;
import com.us.weavx.core.model.DiscountType;
import com.us.weavx.core.model.DonationAmount;
import com.us.weavx.core.model.EmailAgent;
import com.us.weavx.core.model.EmailAgentHist;
import com.us.weavx.core.model.EventCommissionSettings;
import com.us.weavx.core.model.EventDescription;
import com.us.weavx.core.model.EventFaqLangDescription;
import com.us.weavx.core.model.ExternalPaymentType;
import com.us.weavx.core.model.ExternalProfile;
import com.us.weavx.core.model.Fund;
import com.us.weavx.core.model.FundDescription;
import com.us.weavx.core.model.FundImage;
import com.us.weavx.core.model.Gender;
import com.us.weavx.core.model.GenderDescription;
import com.us.weavx.core.model.GeneralDescription;
import com.us.weavx.core.model.IdentityProvider;
import com.us.weavx.core.model.Language;
import com.us.weavx.core.model.Method;
import com.us.weavx.core.model.OTPKey;
import com.us.weavx.core.model.PaymentData;
import com.us.weavx.core.model.PaymentGateway;
import com.us.weavx.core.model.PaymentType;
import com.us.weavx.core.model.PaymentTypeDescription;
import com.us.weavx.core.model.Property;
import com.us.weavx.core.model.State;
import com.us.weavx.core.model.SystemMessage;
import com.us.weavx.core.model.SystemProperty;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.TransactionCampaing;
import com.us.weavx.core.model.TransactionDetail;
import com.us.weavx.core.model.TransactionDetailInfo;
import com.us.weavx.core.model.TransactionMedium;
import com.us.weavx.core.model.TransactionSource;
import com.us.weavx.core.model.TransactionStatus;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.model.UniqueCustomerApplicationUserCode;
import com.us.weavx.core.model.User;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.model.UserAccessTokenInfo;
import com.us.weavx.core.model.ValidationCode;
import com.us.weavx.core.model.VenueDescription;
import com.us.weavx.core.paymentgw.GWTransactionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("servicesDAO")
public class CoreServicesTxDAO extends JdbcDaoSupport {

    public CoreServicesTxDAO(@Autowired JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Country> listAllCountries() {
        String querySQL = "select id, shortname, name, phone_code from countries";
        try {
            ArrayList<Country> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                Country countryTmp = new Country();
                countryTmp.setId((Integer) m.get("id"));
                countryTmp.setName((String) m.get("name"));
                countryTmp.setShortName((String) m.get("shortname"));
                countryTmp.setPhoneCode((String) m.get("phone_code"));
                list.add(countryTmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<State> listStatesByCountry(int countryId) {
        String querySQL = "select id, name, country_id from states where country_id = ?";
        try {
            ArrayList<State> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{countryId});
            for (Map<String, Object> m : result) {
                State tmp = new State();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                tmp.setCountryId((Integer) m.get("country_id"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<State> listAllStates() {
        String querySQL = "select id, name, country_id from states";
        try {
            ArrayList<State> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                State tmp = new State();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                tmp.setCountryId((Integer) m.get("country_id"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<City> listAllCities() {
        String querySQL = "select id, name, state_id from cities";
        try {
            ArrayList<City> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                City tmp = new City();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                tmp.setStateId((Integer) m.get("state_id"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<City> listCitiesByState(int stateId) {
        String querySQL = "select id, name, state_id from cities where state_id = ?";
        try {
            ArrayList<City> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{stateId});
            for (Map<String, Object> m : result) {
                City tmp = new City();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                tmp.setStateId((Integer) m.get("state_id"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void registerNewAccessKey(AccessKey key) {
        String insertSQL = "insert into access_keys(key,secret,customer_id,application_id,is_admin) values (?,?,?,?,?)";
        getJdbcTemplate().update(insertSQL, new Object[]{key.getKey(), key.getSecret(), key.getCustomerId(), key.getApplicationId(), key.isAdmin()});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public boolean validateAccessKey(AccessKey keys) {
        String sql = "select key from access_keys where key = ? and secret = ? and customer_id = ? and application_id = ?";
        try {
            String key = getJdbcTemplate().queryForObject(sql, new Object[]{keys.getKey(), keys.getSecret(), keys.getCustomerId(), keys.getApplicationId()}, new RowMapper<String>() {

                public String mapRow(ResultSet rs, int arg1) throws SQLException {
                    return rs.getString(1);
                }

            });
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public AccessKey getAccessKeyInfo(String accessKey, String accessSecret) {
        String sql = "select id, key, secret, customer_id, application_id, is_admin, created_at from access_keys where key = ? and secret = ?";
        try {
            AccessKey key = getJdbcTemplate().queryForObject(sql, new Object[]{accessKey, accessSecret}, new RowMapper<AccessKey>() {

                public AccessKey mapRow(ResultSet rs, int arg1) throws SQLException {
                    AccessKey tmp = new AccessKey();
                    tmp.setId(rs.getInt(1));
                    tmp.setKey(accessKey);
                    tmp.setSecret(accessSecret);
                    tmp.setCustomerId(rs.getLong(4));
                    tmp.setApplicationId(rs.getLong(5));
                    tmp.setAdmin(rs.getBoolean(6));
                    return tmp;
                }

            });
            return key;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public DeveloperKey getDeveloperKeyInfo(String devKey, String devSecret) {
        String sql = "select id, dev_key, dev_secret, productionEnabled, developer_id, created_at from developer_key where dev_key = ? and dev_secret = ?";
        try {
            DeveloperKey key = getJdbcTemplate().queryForObject(sql, new Object[]{devKey, devSecret}, new RowMapper<DeveloperKey>() {

                public DeveloperKey mapRow(ResultSet rs, int arg1) throws SQLException {
                    DeveloperKey tmp = new DeveloperKey();
                    tmp.setId(rs.getInt(1));
                    tmp.setDevKey(devKey);
                    tmp.setDevSecret(devSecret);
                    tmp.setProductionEnabled(rs.getBoolean(4));
                    tmp.setDeveloperId(rs.getLong(5));
                    tmp.setCreatedAt(rs.getTimestamp(6));
                    return tmp;
                }

            });
            return key;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public HashMap<String, Object> findDeveloperAppCustomerInfo(String accessKey, String accessSecret, String devKey, String devSecret) throws UnauthorizedAccessException {
        String sql = "select ak.id, ak.key, ak.customer_id, ak.is_admin, dk.dev_key, a.id, a.name, a.description, a.short_token_duration, a.long_token_duration, a.short_user_token_duration, a.long_user_token_duration, a.created_by from access_keys ak, developer_key dk, application a where ak.application_id = a.id and (a.created_by = dk.developer_id or exists (select developer_id from developer_app where app_id = a.id and developer_id = dk.developer_id)) and ak.key = ? and ak.secret = ? and dk.dev_key = ? and dk.dev_secret = ?";
        try {
            HashMap<String, Object> result = getJdbcTemplate().queryForObject(sql, new Object[]{accessKey, accessSecret, devKey, devSecret}, new RowMapper<HashMap<String, Object>>() {

                public HashMap<String, Object> mapRow(ResultSet rs, int arg1) throws SQLException {
                    HashMap<String, Object> resultHM = new HashMap<>();
                    AccessKey aK = new AccessKey();
                    aK.setId(rs.getInt(1));
                    aK.setKey(accessKey);
                    aK.setCustomerId(rs.getLong(3));
                    aK.setAdmin(rs.getBoolean(4));
                    aK.setSecret(accessSecret);
                    aK.setApplicationId(rs.getLong(6));
                    Application app = new Application();
                    app.setId(rs.getLong(6));
                    app.setName(rs.getString(7));
                    app.setDescription(rs.getString(8));
                    app.setShortTokenDuration(rs.getLong(9));
                    app.setLongTokenDuration(rs.getLong(10));
                    app.setShortUserTokenDuration(rs.getLong(11));
                    app.setLongUserTokenDuration(rs.getLong(12));
                    app.setDeveloperId(rs.getLong(13));
                    resultHM.put("accessKey", aK);
                    resultHM.put("application", app);
                    return resultHM;
                }

            });
            return result;
        } catch (EmptyResultDataAccessException e) {
            throw new UnauthorizedAccessException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int registerNewAccessToken(AccessToken token, long duration) {
        String insertSQL = "insert into access_token(token, access_key_id, expire_at) values (?,?,current_timestamp + interval '" + duration + " minutes')";
        return getJdbcTemplate().update(insertSQL, new Object[]{token.getToken(), token.getAccessKeyId()});
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void changeAccessTokenStatus(AccessToken token) throws ObjectDoesNotExistsException {
        String insertSQL = "update access_token set status_id = ? where id = ?";
        if (getJdbcTemplate().update(insertSQL, new Object[]{token.getStatus(), token.getId()}) == 0) {
            throw new ObjectDoesNotExistsException();
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void renewAccessToken(AccessToken token) throws ObjectDoesNotExistsException {
        String insertSQL = "update access_token set expire_at = ?, status_id = ? where id = ?";
        if (getJdbcTemplate().update(insertSQL, new Object[]{token.getExpirationDate(), token.getStatus(), token.getId()}) == 0) {
            throw new ObjectDoesNotExistsException();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public AccessInfo getAccessInfo(String accessToken) throws ObjectDoesNotExistsException {
        String sql = "select at.id, at.token, at.created_at, at.expire_at, at.status_id, at.access_key_id, ak.application_id, ak.customer_id from access_token at, access_keys ak where at.access_key_id = ak.id and token = ?";
        try {
            AccessInfo key = getJdbcTemplate().queryForObject(sql, new Object[]{accessToken}, new RowMapper<AccessInfo>() {

                public AccessInfo mapRow(ResultSet rs, int arg1) throws SQLException {
                    AccessToken tokenTmp = new AccessToken(rs.getLong("id"), rs.getString("token"), rs.getTimestamp("expire_at"), rs.getInt("status_id"), rs.getTimestamp("created_at"), rs.getLong("access_key_id"));
                    AccessInfo info = new AccessInfo(tokenTmp, rs.getLong("customer_id"), rs.getLong("application_id"));
                    return info;
                }

            });
            return key;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectDoesNotExistsException();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Method getMethodInfoByName(String methodName) throws ObjectDoesNotExistsException {
        String sql = "select id, name, description, implementor_class, method_type_id, module_id from method where name = ?";
        try {
            Method key = getJdbcTemplate().queryForObject(sql, new Object[]{methodName}, new RowMapper<Method>() {

                public Method mapRow(ResultSet rs, int arg1) throws SQLException {
                    Method method = new Method(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("implementor_class"), rs.getInt("method_type_id"), rs.getInt("module_id"));
                    return method;
                }

            });
            return key;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectDoesNotExistsException();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public IdentityProvider getIdentityProviderInfo(int identityProviderId) throws ObjectDoesNotExistsException {
        String sql = "select id, provider_name, provider_implementor_class from identity_provider where id = ?";
        try {
            IdentityProvider key = getJdbcTemplate().queryForObject(sql, new Object[]{identityProviderId}, new RowMapper<IdentityProvider>() {

                public IdentityProvider mapRow(ResultSet rs, int arg1) throws SQLException {
                    IdentityProvider provider = new IdentityProvider(rs.getInt("id"), rs.getString("provider_name"), rs.getString("provider_implementor_class"));
                    return provider;
                }

            });
            return key;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectDoesNotExistsException();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerIdentityProvider> getCustomerIdentityProviders(long customerId) {
        final String querySQL = "select customer_id, provider_id, provider_key, provider_secret from customer_identity_provider where customer_id = ?";
        ArrayList<CustomerIdentityProvider> list = new ArrayList<>();
        List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
        for (Map<String, Object> m : result) {
            CustomerIdentityProvider tmp = new CustomerIdentityProvider();
            tmp.setCustomerId((Long) m.get("customer_id"));
            tmp.setProviderId((Integer) m.get("provider_id"));
            tmp.setProviderKey((String) m.get("provider_key"));
            tmp.setProviderSecret((String) m.get("provider_secret"));
            list.add(tmp);
        }
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public ArrayList<IdentityProvider> listAllIdentityProviders() {
        final String querySQL = "select id, provider_name, provider_implementor_class from identity_provider";
        ArrayList<IdentityProvider> list = new ArrayList<>();
        List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
        for (Map<String, Object> m : result) {
            IdentityProvider tmp = new IdentityProvider();
            tmp.setId((Integer) m.get("id"));
            tmp.setProviderName((String) m.get("provider_name"));
            tmp.setProviderImplementorClass((String) m.get("provider_implementor_class"));
            list.add(tmp);
        }
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Property> listAllProperties() {
        String querySQL = "select id, name from property";
        try {
            ArrayList<Property> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                Property tmp = new Property();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Language> listAllLanguages() {
        String querySQL = "select id, locale, description, is_default from lang";
        try {
            ArrayList<Language> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                Language tmp = new Language();
                tmp.setId((Integer) m.get("id"));
                tmp.setLocale((String) m.get("locale"));
                tmp.setDescription((String) m.get("description"));
                tmp.setIsDefault((String) m.get("is_default"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Gender> getCustomerGenders(long customerId) {
        final String querySQL = "select g.id, g.name from gender g, customer_gender cg where cg.gender_id = g.id and cg.customer_id = ?";
        ArrayList<Gender> list = new ArrayList<>();
        List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
        for (Map<String, Object> m : result) {
            Gender tmp = new Gender();
            tmp.setId(((Integer) m.get("id")).longValue());
            tmp.setName((String) m.get("name"));
            list.add(tmp);
        }
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<GenderDescription> getCustomerGendersDescription(long customerId) {
        final String querySQL = "select customer_id, gender_id, lang_id, label, description from gender_customer_lang where customer_id = ?";
        ArrayList<GenderDescription> list = new ArrayList<>();
        List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
        for (Map<String, Object> m : result) {
            GenderDescription tmp = new GenderDescription();
            tmp.setCustomerId((Long) m.get("customer_id"));
            tmp.setGenderId((Integer) m.get("gender_id"));
            tmp.setLangId((Integer) m.get("lang_id"));
            tmp.setLabel((String) m.get("label"));
            tmp.setDescription((String) m.get("description"));
            list.add(tmp);
        }
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Property addProperty(Property property) {
        String insertSQL = "INSERT INTO PROPERTY(ID,NAME) VALUES (?,?)";
        getJdbcTemplate().update(insertSQL, new Object[]{property.getId(), property.getName()});
        return property;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Property updateProperty(Property property) {
        String updateSQL = "UPDATE PROPERTY SET NAME = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{property.getName(), property.getId()});
        return property;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerProperty addCustomerProperty(CustomerProperty property) {
        String insertSQL = "INSERT INTO CUSTOMER_PROPERTY_LANG(customer_id,property_id,lang_id,property_value) VALUES (?,?,?,?)";
        getJdbcTemplate().update(insertSQL, new Object[]{property.getCustomerId(), property.getPropertyId(),
                property.getLangId(), property.getPropertyValue()});
        return property;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Property getPropertyByName(String propertyName) {
        String getSQL = "select id,name from property where name = ?";
        try {
            Property prop = getJdbcTemplate().queryForObject(getSQL, new Object[]{propertyName},
                    new RowMapper<Property>() {

                        public Property mapRow(ResultSet arg0, int arg1) throws SQLException {
                            Property prop = new Property();
                            prop.setId(arg0.getInt(1));
                            prop.setName(arg0.getString(2));
                            return prop;
                        }
                    });
            return prop;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Country getCountry(int countryId) {
        String getSQL = "select id, shortname, name, continent_id from countries where id = ?";
        try {
            Country country = getJdbcTemplate().queryForObject(getSQL, new Object[]{countryId},
                    new RowMapper<Country>() {

                        public Country mapRow(ResultSet arg0, int arg1) throws SQLException {
                            Country country = new Country();
                            country.setId(arg0.getInt(1));
                            country.setShortName(arg0.getString(2));
                            country.setName(arg0.getString(3));
                            country.setContinentId(arg0.getInt(4));
                            return country;
                        }
                    });
            return country;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionUserData addTransactionUserData(final TransactionUserData userData) {
        final String insertSQL = "INSERT INTO TRANSACTION_USER_DATA(name, lastname, country, state, stateText, city, cityText, address, postcode, email, customer_user_id, countryText) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{userData.getName(), userData.getLastname(),
                        userData.getCountry(), userData.getState(), userData.getStateText(), userData.getCity(),
                        userData.getCityText(), userData.getAddress(), userData.getPostcode(), userData.getEmail(),
                        userData.getCustomerUserId(), userData.getCountryText()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        userData.setId((Long) holder.getKeys().get("id"));
        return userData;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerUserPaymentProfile addCustomerUserPaymentProfile(final CustomerUserPaymentProfile customerUserPaymentProfile) {
        final String insertSQL = "INSERT INTO CUSTOMER_USER_PAYMENT_PROFILE(customer_user_id, customer_id, payment_gateway_id, payment_gw_customer_id) values (?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{customerUserPaymentProfile.getCustomerUserId(), customerUserPaymentProfile.getCustomerId(), customerUserPaymentProfile.getPaymentGwId(), customerUserPaymentProfile.getPaymentGwCustomerId()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        customerUserPaymentProfile.setId((Long) holder.getKeys().get("id"));
        return customerUserPaymentProfile;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteCustomerUserPaymentProfile(final CustomerUserPaymentProfile customerUserPaymentProfile) {
        final String updateSQL = "delete FROM CUSTOMER_USER_PAYMENT_PROFILE where id = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{customerUserPaymentProfile.getId()});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUserPaymentProfile findCustomerPaymentProfile(final long customerUserId, final long paymentGatewayId) {
        String sql = "select id, customer_user_id, customer_id, payment_gateway_id, payment_gw_customer_id from customer_user_payment_profile where customer_user_id = ? and payment_gateway_id = ?";
        try {
            CustomerUserPaymentProfile custUserPaymentProfile = getJdbcTemplate().queryForObject(sql, new Object[]{customerUserId, paymentGatewayId}, new RowMapper<CustomerUserPaymentProfile>() {

                public CustomerUserPaymentProfile mapRow(ResultSet rs, int arg1) throws SQLException {
                    CustomerUserPaymentProfile cuPPTmp = new CustomerUserPaymentProfile();
                    cuPPTmp.setId(rs.getLong(1));
                    cuPPTmp.setCustomerUserId(customerUserId);
                    cuPPTmp.setCustomerId(rs.getLong(3));
                    cuPPTmp.setPaymentGwId(paymentGatewayId);
                    cuPPTmp.setPaymentGwCustomerId(rs.getString(5));
                    return cuPPTmp;
                }

            });
            return custUserPaymentProfile;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public UserAccessToken getUserTokenInfo(String userAccessToken) {
        String sql = "select id, customer_user_id, created_at, expires_at, status_id, application_id, ip_address, user_agent from user_access_token where token = ?";
        try {
            UserAccessToken uAT = getJdbcTemplate().queryForObject(sql, new Object[]{userAccessToken},
                    new RowMapper<UserAccessToken>() {

                        public UserAccessToken mapRow(ResultSet arg0, int arg1) throws SQLException {
                            UserAccessToken u = new UserAccessToken();
                            u.setId(arg0.getLong(1));
                            u.setCustomerUserId(arg0.getLong(2));
                            u.setCreatedAt(arg0.getTimestamp(3));
                            u.setExpiresAt(arg0.getTimestamp(4));
                            u.setStatus(arg0.getInt(5));
                            u.setApplicationId(arg0.getLong(6));
                            u.setIpAddress(arg0.getString(7));
                            u.setUserAgent(arg0.getString(8));
                            return u;
                        }

                    });
            uAT.setToken(userAccessToken);
            return uAT;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<UserAccessToken> findActiveUserSessionAccessTokens(long customerUserId, long applicationId) {
        final String querySQL = "select id, customer_user_id, created_at, expires_at, status_id, application_id, ip_address, user_agent, token from user_access_token where customer_user_id = ? and application_id = ? and status_id = ?";
        ArrayList<UserAccessToken> list = new ArrayList<>();
        List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                new Object[]{customerUserId, applicationId, TokenStatus.ACTIVE});
        for (Map<String, Object> m : result) {
            UserAccessToken tmp = new UserAccessToken();
            tmp.setId((Long) m.get("id"));
            tmp.setCustomerUserId(customerUserId);
            tmp.setToken((String) m.get("token"));
            tmp.setCreatedAt((Timestamp) m.get("created_at"));
            tmp.setExpiresAt((Timestamp) m.get("expires_at"));
            tmp.setStatus((Integer) m.get("status_id"));
            tmp.setApplicationId(applicationId);
            tmp.setIpAddress((String) m.get("ip_address"));
            tmp.setUserAgent((String) m.get("user_agent"));
            list.add(tmp);
        }
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<UserAccessTokenInfo> getUserAccessTokenInfoByEvent(String userEmail, long customerId, long applicationId) {
        String sql = "SELECT U.EMAIL userEmail, C.ID customerId, C.NAME customerName, A.id applicationId, A.NAME applicationName, UAT.ID id, "
                + "UAT.IP_ADDRESS ipAddress, UAT.USER_AGENT userAgent, UAT.CREATED_AT createdAt, TS.ID tokenStatusId, UPPER(TS.NAME) tokenStatusName "
                + "FROM USER_ACCESS_TOKEN UAT INNER JOIN CUSTOMER_USER CU ON UAT.CUSTOMER_USER_ID = CU.ID "
                + "INNER JOIN CUSTOMER C ON CU.CUSTOMER_ID = C.ID INNER JOIN APPLICATION A ON UAT.APPLICATION_ID = A.ID "
                + "INNER JOIN USERS U ON CU.USER_ID = U.ID INNER JOIN TOKEN_STATUS TS ON UAT.STATUS_ID = TS.ID "
                + "WHERE U.EMAIL = ? AND CU.CUSTOMER_ID = ? AND UAT.APPLICATION_ID = ? ORDER BY UAT.ID ASC";
        try {
            List<UserAccessTokenInfo> list = getJdbcTemplate().query(sql, new Object[]{userEmail, customerId, applicationId}, new BeanPropertyRowMapper(UserAccessTokenInfo.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void closeAllCustomerUserSessions(long customerUserId) {
        String insertSQL = "update user_access_token set status_id = ? where customer_user_id = ?";
        getJdbcTemplate().update(insertSQL, new Object[]{TokenStatus.CLOSED_BY_USER, customerUserId});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUser findCustomerUserByUserId(long userId, long customerId) {
        String sql = "select id, customer_id, user_id, first_name, last_name, gender_id, birth_date, profile_image, country_id, state_id, state_text, city_id, city_text, address, postalCode, phone_number  from customer_user where user_id  = ? and customer_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{userId, customerId},
                    new RowMapper<CustomerUser>() {

                        public CustomerUser mapRow(ResultSet rs, int arg1) throws SQLException {
                            CustomerUser custUser = new CustomerUser();
                            custUser.setId(rs.getLong(1));
                            custUser.setCustomerId(rs.getLong(2));
                            custUser.setUserId(rs.getLong(3));
                            custUser.setFirstName(rs.getString(4));
                            custUser.setLastName(rs.getString(5));
                            custUser.setGenderId(rs.getInt(6));
                            custUser.setBirthDate(rs.getDate(7));
                            custUser.setProfileImage(rs.getString(8));
                            custUser.setCountryId(rs.getInt(9));
                            custUser.setStatedId(rs.getInt(10));
                            custUser.setStateText(rs.getString(11));
                            custUser.setCityId(rs.getInt(12));
                            custUser.setCityText(rs.getString(13));
                            custUser.setAddress(rs.getString(14));
                            custUser.setPostalCode(rs.getString(15));
                            custUser.setPhoneNumber(rs.getString(16));
                            return custUser;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUser findCustomerUserByEmail(String userEmail, long customerId) {
        String sql = "select cu.id, cu.customer_id, cu.user_id, cu.first_name, cu.last_name, cu.gender_id, cu.birth_date, cu.profile_image, cu.country_id, cu.state_id, cu.state_text, cu.city_id, cu.city_text, cu.address, cu.postalCode, cu.phone_number  from customer_user cu, users u where cu.user_id = u.id and u.email  = ? and cu.customer_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{userEmail, customerId},
                    new RowMapper<CustomerUser>() {

                        public CustomerUser mapRow(ResultSet rs, int arg1) throws SQLException {
                            CustomerUser custUser = new CustomerUser();
                            custUser.setId(rs.getLong(1));
                            custUser.setCustomerId(rs.getLong(2));
                            custUser.setUserId(rs.getLong(3));
                            custUser.setFirstName(rs.getString(4));
                            custUser.setLastName(rs.getString(5));
                            custUser.setGenderId(rs.getInt(6));
                            custUser.setBirthDate(rs.getDate(7));
                            custUser.setProfileImage(rs.getString(8));
                            custUser.setCountryId(rs.getInt(9));
                            custUser.setStatedId(rs.getInt(10));
                            custUser.setStateText(rs.getString(11));
                            custUser.setCityId(rs.getInt(12));
                            custUser.setCityText(rs.getString(13));
                            custUser.setAddress(rs.getString(14));
                            custUser.setPostalCode(rs.getString(15));
                            custUser.setPhoneNumber(rs.getString(16));
                            return custUser;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public ExternalProfile findUserExternalProfile(int identityProviderId, long userId) {
        String sql = "select id, id_identity_provider, user_id, access_token from external_profile where id_identity_provider = ? and user_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{identityProviderId, userId},
                    new RowMapper<ExternalProfile>() {

                        public ExternalProfile mapRow(ResultSet rs, int arg1) throws SQLException {
                            ExternalProfile tmp = new ExternalProfile();
                            tmp.setId(rs.getLong(1));
                            tmp.setIdentityProviderId(rs.getInt(2));
                            tmp.setUserId(rs.getLong(3));
                            return tmp;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ExternalProfile registerNewExternalUser(ExternalProfile user) {
        final String insertSQL = "insert into external_profile(id_identity_provider, user_id, access_token) values (?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{user.getIdentityProviderId(), user.getUserId(), user.getAccesstoken()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        user.setId((Long) holder.getKeys().get("id"));
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateExternalUserProfileToken(final ExternalProfile user) {
        final String updateSQL = "update external_profile set access_token = ? where id = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{user.getAccesstoken(), user.getId()});
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PaymentData addPaymentData(final PaymentData paymentData) {
        final String insertSQL = "INSERT INTO PAYMENT_DATA(payment_type_id) values (?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{paymentData.getPaymentTypeId()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        paymentData.setId((Long) holder.getKeys().get("id"));
        return paymentData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public PaymentType findPaymentType(final int id) {
        String sql = "select id, name, url_logo, enabled from payment_type where id = ? ";
        try {
            PaymentType pT = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<PaymentType>() {

                public PaymentType mapRow(ResultSet arg0, int arg1) throws SQLException {
                    PaymentType pT = new PaymentType();
                    pT.setId(arg0.getInt(1));
                    pT.setName(arg0.getString(2));
                    pT.setUrlLogo(arg0.getString(3));
                    pT.setEnabled(arg0.getBoolean(4));
                    return pT;
                }

            });
            return pT;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CreditCardPaymentData addCreditCardPaymentData(final CreditCardPaymentData paymentData) {
        final String insertSQL = "INSERT INTO CREDIT_CARD_PAYMENT_DATA(payment_data_id, credit_card_masked, credit_card_brand_id) values (?,?,?)";
        getJdbcTemplate().update(insertSQL, new Object[]{paymentData.getId(), paymentData.getCreditCardMasked(),
                paymentData.getCreditCardBrandId()});
        return paymentData;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CreditCardPaymentData updateCreditCardPaymentData(final CreditCardPaymentData paymentData) {
        final String updateSQL = "UPDATE CREDIT_CARD_PAYMENT_DATA SET CREDIT_CARD_MASKED = ?, CREDIT_CARD_BRAND_ID = ? where PAYMENT_DATA_ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{paymentData.getCreditCardMasked(),
                paymentData.getCreditCardBrandId(), paymentData.getId()});
        return paymentData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public PaymentGateway getPaymentGateway(int paymentGatewayId) {
        final String querySQL = "SELECT id, name, enabled, implementor_class from PAYMENT_GATEWAY where id = ?";
        try {
            PaymentGateway paymentGateway = getJdbcTemplate().queryForObject(querySQL,
                    new Object[]{paymentGatewayId}, new RowMapper<PaymentGateway>() {

                        @Override
                        public PaymentGateway mapRow(ResultSet rs, int arg1) throws SQLException {
                            PaymentGateway res = new PaymentGateway();
                            res.setId(rs.getInt(1));
                            res.setName(rs.getString(2));
                            res.setEnabled(rs.getBoolean(3));
                            res.setImplementorClass(rs.getString(4));
                            return res;
                        }

                    });
            return paymentGateway;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<PaymentType> listAllPaymentTypes() {
        final String querySQL = "select id, name, url_logo, enabled from payment_type";
        try {
            ArrayList<PaymentType> paymentTypes = new ArrayList<PaymentType>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                PaymentType tmp = new PaymentType();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                tmp.setUrlLogo((String) m.get("url_logo"));
                tmp.setEnabled((Boolean) m.get("enabled"));
                paymentTypes.add(tmp);
            }
            return paymentTypes;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<PaymentTypeDescription> listAllPaymentTypeDescriptions() {
        final String querySQL = "select payment_type_id, lang_id, label from payment_type_lang";
        try {
            ArrayList<PaymentTypeDescription> paymentTypes = new ArrayList<PaymentTypeDescription>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                PaymentTypeDescription tmp = new PaymentTypeDescription();
                tmp.setPaymentTypeId((Integer) m.get("payment_type_id"));
                tmp.setLangId((Integer) m.get("lang_id"));
                tmp.setLabel((String) m.get("label"));
                paymentTypes.add(tmp);
            }
            return paymentTypes;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerSupportedPaymentType> getCustomerSupportedPaymentTypes(long customerId) {
        final String querySQL = "select customer_id, payment_type_id, enabled from CUSTOMER_SUPPORTED_PAYMENT_TYPES where customer_id = ?";
        try {
            ArrayList<CustomerSupportedPaymentType> custSupportedPaymentTypes = new ArrayList<CustomerSupportedPaymentType>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                CustomerSupportedPaymentType tmp = new CustomerSupportedPaymentType();
                tmp.setCustomerId(customerId);
                tmp.setPaymentTypeId((Integer) m.get("payment_type_id"));
                tmp.setEnabled((Boolean) m.get("enabled"));
                custSupportedPaymentTypes.add(tmp);
            }
            return custSupportedPaymentTypes;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerPaymentGateway> getCustomerPaymentGateways(long customerId) {
        final String querySQL = "SELECT customer_id, payment_gateway_id, auth_key_1, auth_key_2, auth_key_3, auth_key_4, payment_mode, auth_key_1_public, auth_key_2_public, auth_key_3_public, auth_key_4_public, connected_payment_gateway_account from CUSTOMER_PAYMENT_GATEWAY where customer_id = ?";
        try {
            ArrayList<CustomerPaymentGateway> customerPGws = new ArrayList<CustomerPaymentGateway>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                CustomerPaymentGateway tmp = new CustomerPaymentGateway();
                tmp.setCustomerId(customerId);
                tmp.setPaymentGatewayId((Integer) m.get("payment_gateway_id"));
                tmp.setAuthKey1((String) m.get("auth_key_1"));
                tmp.setAuthKey2((String) m.get("auth_key_2"));
                tmp.setAuthKey3((String) m.get("auth_key_3"));
                tmp.setAuthKey4((String) m.get("auth_key_4"));
                tmp.setPaymentModeId((Integer) m.get("payment_mode"));
                tmp.setPublicAuthKey1((Boolean) m.get("auth_key_1_public"));
                tmp.setPublicAuthKey2((Boolean) m.get("auth_key_2_public"));
                tmp.setPublicAuthKey3((Boolean) m.get("auth_key_3_public"));
                tmp.setPublicAuthKey4((Boolean) m.get("auth_key_4_public"));
                tmp.setConnectedAccountId((String) m.get("connected_payment_gateway_account"));
                customerPGws.add(tmp);
            }
            return customerPGws;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerPaymentGateway getCustomerPaymentGatewayByPaymentGatewayId(long customerId, int paymentGatewayId) {
        final String querySQL = "SELECT customer_id, payment_gateway_id, auth_key_1, auth_key_2, auth_key_3, auth_key_4, payment_mode, auth_key_1_public, auth_key_2_public, auth_key_3_public, auth_key_4_public, connected_payment_gateway_account from CUSTOMER_PAYMENT_GATEWAY where customer_id = ? and payment_gateway_id = ?";
        try {
            return getJdbcTemplate().queryForObject(querySQL,
                    new Object[]{customerId, paymentGatewayId}, new RowMapper<CustomerPaymentGateway>() {

                        @Override
                        public CustomerPaymentGateway mapRow(ResultSet rs, int arg1) throws SQLException {
                            CustomerPaymentGateway res = new CustomerPaymentGateway();
                            res.setCustomerId(rs.getLong(1));
                            res.setPaymentGatewayId(rs.getInt(2));
                            res.setAuthKey1(rs.getString(3));
                            res.setAuthKey2(rs.getString(4));
                            res.setAuthKey3(rs.getString(5));
                            res.setAuthKey4(rs.getString(6));
                            res.setPaymentModeId(rs.getInt(7));
                            res.setPublicAuthKey1(rs.getBoolean(8));
                            res.setPublicAuthKey2(rs.getBoolean(9));
                            res.setPublicAuthKey3(rs.getBoolean(10));
                            res.setPublicAuthKey4(rs.getBoolean(11));
                            res.setConnectedAccountId(rs.getString(12));
                            return res;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerDefaultPaymentGateway> getCustomerDefaultPaymentGateways(long customerId) {
        final String querySQL = "select customer_id, payment_type_id, payment_gateway_id from CUSTOMER_DEFAULT_PAYMENT_GATEWAYS where customer_id = ?";
        try {
            ArrayList<CustomerDefaultPaymentGateway> custDefaultPGw = new ArrayList<CustomerDefaultPaymentGateway>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                CustomerDefaultPaymentGateway tmp = new CustomerDefaultPaymentGateway();
                tmp.setCustomerId(customerId);
                tmp.setPaymentTypeId((Integer) m.get("payment_type_id"));
                tmp.setPaymentGatewayId((Integer) m.get("payment_gateway_id"));
                custDefaultPGw.add(tmp);
            }
            return custDefaultPGw;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<PaymentGateway> listAllPaymentGateways() {
        final String querySQL = "select id, name, enabled from payment_gateway order by id";
        try {
            ArrayList<PaymentGateway> paymentGws = new ArrayList<PaymentGateway>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                PaymentGateway tmp = new PaymentGateway();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                tmp.setEnabled((Boolean) m.get("enabled"));
                paymentGws.add(tmp);
            }
            return paymentGws;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Application> getCustomerApplications(long customerId) {
        final String querySQL = "select distinct id, name, description, short_token_duration, long_token_duration, short_user_token_duration, long_user_token_duration from application a where exists (select application_id from access_keys where application_id = a.id and customer_id = ?)";
        try {
            ArrayList<Application> applications = new ArrayList<Application>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                Application app = new Application();
                app.setId((Long) m.get("id"));
                app.setName((String) m.get("name"));
                app.setDescription((String) m.get("description"));
                app.setShortTokenDuration((Long) m.get("short_token_duration"));
                app.setLongTokenDuration((Long) m.get("long_token_duration"));
                app.setShortUserTokenDuration((Long) m.get("short_user_token_duration"));
                app.setLongUserTokenDuration((Long) m.get("long_user_token_duration"));
                applications.add(app);
            }
            return applications;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionStatus> listTransactionStatus() {
        final String querySQL = "select id, name from transaction_status order by id";
        try {
            ArrayList<TransactionStatus> statusList = new ArrayList<TransactionStatus>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                TransactionStatus tStatus = new TransactionStatus();
                tStatus.setId((Integer) m.get("id"));
                tStatus.setName((String) m.get("name"));
                statusList.add(tStatus);
            }
            return statusList;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionSource addTransactionSource(final TransactionSource transactionSource) {
        final String insertSQL = "INSERT INTO TRANSACTION_SOURCE(name, key, description, customer_id) values (?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{transactionSource.getName(), transactionSource.getKey(), transactionSource.getDescription(), transactionSource.getCustomerId()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        transactionSource.setId((Integer) holder.getKeys().get("id"));
        return transactionSource;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionSource> listCustomerSources(long customerId) {
        final String querySQL = "select id, name, key, description, customer_id from transaction_source where customer_id = ? order by id";
        try {
            ArrayList<TransactionSource> custSourceList = new ArrayList<TransactionSource>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                TransactionSource custSource = new TransactionSource();
                custSource.setId((Integer) m.get("id"));
                custSource.setName((String) m.get("name"));
                custSource.setKey((String) m.get("key"));
                custSource.setDescription((String) m.get("description"));
                custSource.setCustomerId(customerId);
                custSourceList.add(custSource);
            }
            return custSourceList;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionCampaing> listTransactionCampaings(long customerId) {
        final String querySQL = "select id, name, key, valid_from, valid_to, description, customer_id from transaction_campaing where customer_id = ? order by id";
        try {
            ArrayList<TransactionCampaing> custCampaingList = new ArrayList<TransactionCampaing>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                TransactionCampaing custCampaingTmp = new TransactionCampaing();
                custCampaingTmp.setId(((Number) m.get("id")).longValue());
                custCampaingTmp.setName((String) m.get("name"));
                custCampaingTmp.setKey((String) m.get("key"));
                custCampaingTmp.setCustomerId(customerId);
                custCampaingTmp.setDescription((String) m.get("description"));
                custCampaingTmp.setValidFrom((Timestamp) m.get("valid_from"));
                custCampaingTmp.setValidTo((Timestamp) m.get("valid_to"));
                custCampaingList.add(custCampaingTmp);
            }
            return custCampaingList;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionCampaing addTransactionCampaing(final TransactionCampaing transactionCampaing) {
        final String insertSQL = "INSERT INTO TRANSACTION_CAMPAING(name, key, description, customer_id, valid_from, valid_to) values (?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{transactionCampaing.getName(), transactionCampaing.getKey(), transactionCampaing.getDescription(), transactionCampaing.getCustomerId(), transactionCampaing.getValidFrom(), transactionCampaing.getValidTo()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        transactionCampaing.setId((Long) holder.getKeys().get("id"));
        return transactionCampaing;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerDefaultPaymentGateway updateCustomerDefaultPaymentGatewayByPaymentType(
            CustomerDefaultPaymentGateway custDefaultPGw) {
        final String updateSQL = "UPDATE CUSTOMER_DEFAULT_PAYMENT_GATEWAYS SET PAYMENT_GATEWAY_ID = ? WHERE CUSTOMER_ID = ? AND PAYMENT_TYPE_ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{custDefaultPGw.getPaymentGatewayId(),
                custDefaultPGw.getCustomerId(), custDefaultPGw.getPaymentTypeId()});
        return custDefaultPGw;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Transaction updateTransactionStatus(Transaction transaction) {
        final String updateSQL = "UPDATE TRANSACTION SET TX_STATUS_ID = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{transaction.getTransactionStatus(), transaction.getId()});
        return transaction;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Transaction updateTransactionGwInfo(Transaction transaction) {
        final String updateSQL = "UPDATE TRANSACTION SET gwAuthId_1 = ?, gwAuthId_2 = ? WHERE id = ?";
        getJdbcTemplate().update(updateSQL,
                new Object[]{transaction.getAuthGw1(), transaction.getAuthGw2(), transaction.getId()});
        return transaction;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Transaction addTransaction(final Transaction transaction) {
		final String insertSQL = "INSERT INTO TRANSACTION(tx_id, tx_user_data_id, tx_payment_data_id, customer_id, application_id, amount, tx_payment_gw_id, tx_transaction_source_id, tx_transaction_campaing_id, tx_transaction_medium_id, tx_lang_id, is_scheduled, discount, commission, comments, operator_id, operator_email, ip_address, user_agent) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
				new ArgumentPreparedStatementSetter(new Object[] { transaction.getTransactionId(),
						transaction.getTransactionUserDataId(), transaction.getTransactionPaymentDataId(),
						transaction.getCustomerId(), transaction.getApplicationId(), transaction.getAmount(),
						transaction.getPaymentGwId(), transaction.getSourceId(), transaction.getCampaingId(), transaction.getMediumId(), transaction.getLangId(), transaction.isScheduled(), transaction.getDiscount(), transaction.getCommission(), transaction.getComments(), transaction.getOperatorId(), transaction.getOperatorEmail(), transaction.getIpAddress(), transaction.getUserAgent()})
								.setValues(ps);
				return ps;
			}
		};
		KeyHolder holder = new GeneratedKeyHolder();
		getJdbcTemplate().update(psc, holder);
		transaction.setId((Long) holder.getKeys().get("id"));
		return transaction;
	}

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addTransactionDetails(final List<TransactionDetail> transactionDetails, final long transactionId) {
        String insertSQL = "insert into TRANSACTION_DETAIL(transaction_id, fund_id, amount) values (?,?,?)";
        getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement arg0, int arg1) throws SQLException {
                // TODO Auto-generated method stub
                TransactionDetail tmp = transactionDetails.get(arg1);
                new ArgumentPreparedStatementSetter(new Object[]{transactionId, tmp.getFundId(), tmp.getAmount()})
                        .setValues(arg0);
            }

            @Override
            public int getBatchSize() {
                return transactionDetails.size();
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setSettledTransactions(final List<GWTransactionInfo> settledTransactions, final long customerId) {
        String insertSQL = "update transaction_report set Is_settled = true where Contribution_ONL_TransactionID = ? and Customer_id = ?";
        getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement arg0, int arg1) throws SQLException {
                // TODO Auto-generated method stub
                String tmp = settledTransactions.get(arg1).getGwTransactionId();
                new ArgumentPreparedStatementSetter(new Object[]{tmp, customerId}).setValues(arg0);
            }

            @Override
            public int getBatchSize() {
                return settledTransactions.size();
            }
        });
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Fund> getCustomerFunds(long customerId) {
        final String querySQL = "select id, customer_id, business_code, is_default, name, valid_from, valid_to, isSchedulable from fund where customer_id = ?";
        try {
            ArrayList<Fund> funds = new ArrayList<Fund>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                Fund fund = new Fund();
                fund.setId((Integer) m.get("id"));
                fund.setCustomerId((Long) m.get("customer_id"));
                fund.setBusinessCode((String) m.get("business_code"));
                fund.setDefault((Boolean) m.get("is_default"));
                fund.setName((String) m.get("name"));
                fund.setValidFrom((Timestamp) m.get("valid_from"));
                fund.setValidTo((Timestamp) m.get("valid_to"));
                fund.setIsSchedulable((Boolean) m.get("isSchedulable"));
                funds.add(fund);
            }
            return funds;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Fund findFundById(int fundId) {
        final String querySQL = "SELECT id, customer_id, business_code, is_default, name, valid_from, valid_to, isSchedulable from fund where id = ?";
        try {
            return getJdbcTemplate().queryForObject(querySQL,
                    new Object[]{fundId}, new RowMapper<Fund>() {

                        @Override
                        public Fund mapRow(ResultSet rs, int arg1) throws SQLException {
                            Fund f = new Fund();
                            f.setId(rs.getInt(1));
                            f.setCustomerId(rs.getLong(2));
                            f.setBusinessCode(rs.getString(3));
                            f.setDefault(rs.getBoolean(4));
                            f.setName(rs.getString(5));
                            f.setValidFrom(rs.getTimestamp(6));
                            f.setValidTo(rs.getTimestamp(7));
                            f.setIsSchedulable(rs.getBoolean(8));
                            return f;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<FundDescription> getCustomerFundsDescription(long customerId) {
        final String querySQL = "select fl.fund_id, fl.lang_id, fl.label, fl.description from fund_lang fl inner join fund f on fl.fund_id = f.id where f.customer_id = ?";
        try {
            ArrayList<FundDescription> funds = new ArrayList<FundDescription>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                FundDescription fund = new FundDescription();
                fund.setFundId((Integer) m.get("fund_id"));
                fund.setFundDescription((String) m.get("description"));
                fund.setLangId((Integer) m.get("lang_id"));
                fund.setFundLabel((String) m.get("label"));
                funds.add(fund);
            }
            return funds;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<FundImage> getCustomerFundImages(long customerId) {
        final String querySQL = "select fi.id, fi.fund_id, fi.image_url, fi.lang_id from fund_image fi inner join fund f on fi.fund_id = f.id where f.customer_id = ?";
        try {
            ArrayList<FundImage> fundImages = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                FundImage fundImg = new FundImage();
                fundImg.setFundId((Integer) m.get("fund_id"));
                fundImg.setImageId((Integer) m.get("id"));
                fundImg.setImageUrl((String) m.get("image_url"));
                fundImg.setLangId((Integer) m.get("lang_id"));
                fundImages.add(fundImg);
            }
            return fundImages;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<DonationAmount> getCustomerDonationAmounts(long customerId) {
        final String querySQL = "select id, customer_id, amount, valid_from, valid_to from donation_amount where customer_id = ?";
        try {
            ArrayList<DonationAmount> amounts = new ArrayList<DonationAmount>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                DonationAmount amount = new DonationAmount();
                amount.setId((Long) m.get("id"));
                amount.setAmount(((Number) m.get("amount")).doubleValue());
                amount.setCustomerId((Long) m.get("customer_id"));
                amount.setValidFrom((Timestamp) m.get("valid_from"));
                amount.setValidTo((Timestamp) m.get("valid_to"));
                amounts.add(amount);
            }
            return amounts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<SystemProperty> getSystemProperties() {
        final String querySQL = "select id, name, value, description from system_property";
        try {
            ArrayList<SystemProperty> properties = new ArrayList<SystemProperty>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                SystemProperty property = new SystemProperty();
                property.setId((Long) m.get("id"));
                property.setName((String) m.get("name"));
                property.setValue((String) m.get("value"));
                property.setDescription((String) m.get("description"));
                properties.add(property);
            }
            return properties;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // USer Access Token
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public UserAccessToken registerNewUserAccessToken(final UserAccessToken userAccessToken, final long duration) {
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        cal.add(Calendar.MINUTE, new Long(duration).intValue());
        final Timestamp tokenExpiration = new Timestamp(cal.getTimeInMillis());
        final String insertSQL = "insert into user_access_token(customer_user_id, token, expires_at, application_id, ip_address, user_agent, status_id) values (?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{userAccessToken.getCustomerUserId(),
                        userAccessToken.getToken(), tokenExpiration, userAccessToken.getApplicationId(),
                        userAccessToken.getIpAddress(), userAccessToken.getUserAgent(), userAccessToken.getStatus()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        userAccessToken.setId((Long) holder.getKeys().get("id"));
        userAccessToken.setCreatedAt((Timestamp) holder.getKeys().get("created_at"));
        userAccessToken.setExpiresAt(tokenExpiration);
        return userAccessToken;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Application registerNewApplication(Application app) {
        final String insertSQL = "insert into application(name, description, short_token_duration, long_token_duration, short_user_token_duration, long_user_token_duration, created_by, application_controller_class) values (?,?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{app.getName(), app.getDescription(), app.getShortTokenDuration(), app.getLongTokenDuration(), app.getShortUserTokenDuration(), app.getLongUserTokenDuration(), app.getDeveloperId(), app.getApplicationControllerClass()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        app.setId((Long) holder.getKeys().get("id"));
        return app;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Developer registerNewDeveloper(Developer developer) {
        final String insertSQL = "insert into developer(name, email, password) values (?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{developer.getName(), developer.getEmail(), developer.getPassword()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        developer.setId((Long) holder.getKeys().get("id"));
        return developer;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public boolean existsDeveloperKey(String key) {
        final String querySQL = "select dev_key from developer_key where dev_key = ?";
        try {
            getJdbcTemplate().queryForObject(querySQL, new Object[]{key},
                    new RowMapper<String>() {

                        @Override
                        public String mapRow(ResultSet rs, int arg1) throws SQLException {
                            return rs.getString(1);
                        }

                    });
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public DeveloperKey registerNewDeveloperKey(DeveloperKey key) {
        final String insertSQL = "insert into developer_key(dev_key, dev_secret, productionEnabled, developer_id) values (?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{key.getDevKey(), key.getDevSecret(), key.isProductionEnabled(), key.getDeveloperId()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        key.setId((Long) holder.getKeys().get("id"));
        return key;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Application findApplicationInfo(long applicationId) {
        final String querySQL = "select id,name,description,short_token_duration,long_token_duration,short_user_token_duration,long_user_token_duration,created_by,application_controller_class from application where id = ?";
        try {
            return getJdbcTemplate().queryForObject(querySQL, new Object[]{applicationId},
                    new RowMapper<Application>() {

                        @Override
                        public Application mapRow(ResultSet rs, int arg1) throws SQLException {
                            Application res = new Application();
                            res.setId(rs.getLong(1));
                            res.setName(rs.getString(2));
                            res.setDescription(rs.getString(3));
                            res.setShortTokenDuration(rs.getLong(4));
                            res.setLongTokenDuration(rs.getLong(5));
                            res.setShortUserTokenDuration(rs.getLong(6));
                            res.setLongUserTokenDuration(rs.getLong(7));
                            res.setDeveloperId(rs.getLong(8));
                            res.setApplicationControllerClass(rs.getString(9));
                            return res;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUser findCustomerUserByAccessCredentials(String email, String encryptedPassword, long customerId) {
        final String querySQL = "select cu.id, cu.customer_id, cu.user_id, cu.first_name, cu.last_name, cu.gender_id, cu.birth_date, cu.profile_image, cu.country_id, cu.state_id, cu.state_text, cu.city_id, cu.city_text, cu.address, cu.postalCode, cu.phone_number  from customer_user cu, users u where cu.user_id = u.id and cu.password = ? and u.email  = ? and cu.customer_id = ?";
        try {
            return getJdbcTemplate().queryForObject(querySQL, new Object[]{encryptedPassword, email, customerId},
                    new RowMapper<CustomerUser>() {

                        public CustomerUser mapRow(ResultSet rs, int arg1) throws SQLException {
                            CustomerUser custUser = new CustomerUser();
                            custUser.setId(rs.getLong(1));
                            custUser.setCustomerId(rs.getLong(2));
                            custUser.setUserId(rs.getLong(3));
                            custUser.setFirstName(rs.getString(4));
                            custUser.setLastName(rs.getString(5));
                            custUser.setGenderId(rs.getInt(6));
                            custUser.setBirthDate(rs.getDate(7));
                            custUser.setProfileImage(rs.getString(8));
                            custUser.setCountryId(rs.getInt(9));
                            custUser.setStatedId(rs.getInt(10));
                            custUser.setStateText(rs.getString(11));
                            custUser.setCityId(rs.getInt(12));
                            custUser.setCityText(rs.getString(13));
                            custUser.setAddress(rs.getString(14));
                            custUser.setPostalCode(rs.getString(15));
                            custUser.setPhoneNumber(rs.getString(16));
                            return custUser;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUser findCustomerUserById(long customerUserId) {
        final String querySQL = "select cu.id, cu.customer_id, cu.user_id, cu.first_name, cu.last_name, cu.gender_id, cu.birth_date, cu.profile_image, cu.country_id, cu.state_id, cu.state_text, cu.city_id, cu.city_text, cu.address, cu.postalCode, cu.phone_number  from customer_user cu where cu.id = ?";
        try {
            return getJdbcTemplate().queryForObject(querySQL, new Object[]{customerUserId},
                    new RowMapper<CustomerUser>() {

                        public CustomerUser mapRow(ResultSet rs, int arg1) throws SQLException {
                            CustomerUser custUser = new CustomerUser();
                            custUser.setId(rs.getLong(1));
                            custUser.setCustomerId(rs.getLong(2));
                            custUser.setUserId(rs.getLong(3));
                            custUser.setFirstName(rs.getString(4));
                            custUser.setLastName(rs.getString(5));
                            custUser.setGenderId(rs.getInt(6));
                            custUser.setBirthDate(rs.getDate(7));
                            custUser.setProfileImage(rs.getString(8));
                            custUser.setCountryId(rs.getInt(9));
                            custUser.setStatedId(rs.getInt(10));
                            custUser.setStateText(rs.getString(11));
                            custUser.setCityId(rs.getInt(12));
                            custUser.setCityText(rs.getString(13));
                            custUser.setAddress(rs.getString(14));
                            custUser.setPostalCode(rs.getString(15));
                            custUser.setPhoneNumber(rs.getString(16));
                            return custUser;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUser findCustomerUserById(long customerUserId, long customerId) {
        final String querySQL = "select cu.id, cu.customer_id, cu.user_id, cu.first_name, cu.last_name, cu.gender_id, cu.birth_date, cu.profile_image, cu.country_id, cu.state_id, cu.state_text, cu.city_id, cu.city_text, cu.address, cu.postalCode, cu.phone_number  from customer_user cu where cu.customer_id = ? and cu.id = ?";
        try {
            return getJdbcTemplate().queryForObject(querySQL, new Object[]{customerId, customerUserId},
                    new RowMapper<CustomerUser>() {

                        public CustomerUser mapRow(ResultSet rs, int arg1) throws SQLException {
                            CustomerUser custUser = new CustomerUser();
                            custUser.setId(rs.getLong(1));
                            custUser.setCustomerId(rs.getLong(2));
                            custUser.setUserId(rs.getLong(3));
                            custUser.setFirstName(rs.getString(4));
                            custUser.setLastName(rs.getString(5));
                            custUser.setGenderId(rs.getInt(6));
                            custUser.setBirthDate(rs.getDate(7));
                            custUser.setProfileImage(rs.getString(8));
                            custUser.setCountryId(rs.getInt(9));
                            custUser.setStatedId(rs.getInt(10));
                            custUser.setStateText(rs.getString(11));
                            custUser.setCityId(rs.getInt(12));
                            custUser.setCityText(rs.getString(13));
                            custUser.setAddress(rs.getString(14));
                            custUser.setPostalCode(rs.getString(15));
                            custUser.setPhoneNumber(rs.getString(16));
                            return custUser;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerUserWIthEmail findCustomerUserWIthEmailById(long customerUserId, long customerId) {
        final String querySQL = "select cu.id, cu.customer_id, cu.user_id, cu.first_name, cu.last_name, cu.gender_id, cu.birth_date, cu.profile_image, cu.country_id, cu.state_id, cu.state_text, cu.city_id, cu.city_text, cu.address, cu.postalCode, u.email, cu.phone_number  from customer_user cu, users u where cu.user_id = u.id and cu.customer_id = ? and cu.id = ?";
        try {
            return getJdbcTemplate().queryForObject(querySQL, new Object[]{customerId, customerUserId},
                    new RowMapper<CustomerUserWIthEmail>() {

                        public CustomerUserWIthEmail mapRow(ResultSet rs, int arg1) throws SQLException {
                            CustomerUserWIthEmail custUserWithEmail = new CustomerUserWIthEmail();
                            CustomerUser custUser = new CustomerUser();
                            custUser.setId(rs.getLong(1));
                            custUser.setCustomerId(rs.getLong(2));
                            custUser.setUserId(rs.getLong(3));
                            custUser.setFirstName(rs.getString(4));
                            custUser.setLastName(rs.getString(5));
                            custUser.setGenderId(rs.getInt(6));
                            custUser.setBirthDate(rs.getDate(7));
                            custUser.setProfileImage(rs.getString(8));
                            custUser.setCountryId(rs.getInt(9));
                            custUser.setStatedId(rs.getInt(10));
                            custUser.setStateText(rs.getString(11));
                            custUser.setCityId(rs.getInt(12));
                            custUser.setCityText(rs.getString(13));
                            custUser.setAddress(rs.getString(14));
                            custUser.setPostalCode(rs.getString(15));
                            custUser.setPhoneNumber(rs.getString(17));
                            custUserWithEmail.setCustUser(custUser);
                            custUserWithEmail.setEmail(rs.getString(16));
                            return custUserWithEmail;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Transaction> findCustomerUserSuccessTransactionsByApplication(long customerId, long customerUserId,
                                                                              long applicationId) {
        String querySQL = "select t.id, t.tx_id, t.tx_date, t.tx_user_data_id, t.tx_payment_data_id, t.tx_status_id, t.customer_id, t.application_id, t.amount, t.gwAuthId_1, t.gwAuthId_2, t.tx_payment_gw_id, t.tx_transaction_source_id, t.tx_transaction_campaing_id, t.tx_transaction_medium_id, t.tx_lang_id, t.is_scheduled from transaction as t, transaction_user_Data as tud where t.tx_user_data_id = tud.id and customer_id = ? and t.application_id = ? and t.tx_status_id = ? and tud.customer_user_id = ?";
        try {
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{customerId, applicationId, Transaction.SUCCESS_TX, customerUserId});
            for (Map<String, Object> m : result) {
                Transaction tx = new Transaction();
                tx.setId((Long) m.get("id"));
                tx.setTransactionId((String) m.get("tx_id"));
                tx.setTransactionDate((Timestamp) m.get("tx_date"));
                tx.setTransactionUserDataId((Long) m.get("tx_user_data_id"));
                tx.setTransactionPaymentDataId((Long) m.get("tx_payment_data_id"));
                tx.setTransactionStatus((Integer) m.get("tx_status_id"));
                tx.setCustomerId(customerId);
                tx.setApplicationId(applicationId);
                tx.setAmount(((Number) m.get("amount")).doubleValue());
                if (tx.getAmount() > 0) {
                    tx.setAuthGw1((String) m.get("gwAuthId_1"));
                    tx.setAuthGw2((String) m.get("gwAuthId_2"));
                    tx.setPaymentGwId((Integer) m.get("tx_payment_gw_id"));
                }
                tx.setSourceId((Integer) m.get("tx_transaction_source_id"));
                tx.setCampaingId((Long) m.get("tx_transaction_campaing_id"));
                tx.setMediumId((Long) m.get("tx_transaction_medium_id"));
                tx.setLangId((Integer) m.get("tx_lang_id"));
                if (m.get("t.is_scheduled") != null) {
                    tx.setScheduled((Boolean) m.get("t.is_scheduled"));
                }
                transactions.add(tx);
            }
            return transactions;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerUserPurchaseInfo> findCustomerPurchasaeInfoForSuccessLiveEventPurchases(long customerId, long applicationId) {
        String querySQL = "select distinct u.email, cu.first_name, cu.last_name, f.id, f.name, f.valid_from, f.valid_to, td.amount, t.tx_date, t.tx_lang_id, cu.id as cuid from customer_user as cu, users as u, transaction_detail as td, transaction as t, transaction_user_data as tud, fund as f where td.transaction_id = t.id and cu.user_id = u.id and tud.id = t.tx_user_data_id and tud.customer_user_id = cu.id and td.fund_id = f.id and t.customer_id = ? and t.tx_status_id = ? and t.application_id = ?";
        try {
            ArrayList<CustomerUserPurchaseInfo> transactions = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL,
                    new Object[]{customerId, Transaction.SUCCESS_TX, applicationId});
            for (Map<String, Object> m : result) {
                CustomerUserPurchaseInfo item = new CustomerUserPurchaseInfo();
                item.setCustomerUserId(((Number) m.get("cuid")).longValue());
                item.setEmail((String) m.get("email"));
                item.setFirstName((String) m.get("first_name"));
                item.setLastName((String) m.get("last_name"));
                item.setPurposeName((String) m.get("name"));
                item.setPurposeId(((Integer) m.get("id")).longValue());
                item.setPurposeValidFrom((Timestamp) m.get("valid_from"));
                item.setPurposeValidTo((Timestamp) m.get("valid_to"));
                item.setAmount(((Number) m.get("amount")).doubleValue());
                item.setPurchaseDate((Timestamp) m.get("tx_date"));
                item.setLangId((Integer) m.get("tx_lang_id"));
                transactions.add(item);
            }
            return transactions;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void changeUserAccessTokenStatus(UserAccessToken uAT) {
        final String updateSQL = "UPDATE USER_ACCESS_TOKEN SET STATUS_ID = ? WHERE ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{uAT.getStatus(), uAT.getId()});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public User findUserByEmail(final String email) {
        String sql = "select id, email, created_at from users where email = ?";
        try {
            User user = getJdbcTemplate().queryForObject(sql, new Object[]{email}, new RowMapper<User>() {

                public User mapRow(ResultSet rs, int arg1) throws SQLException {
                    User u = new User();
                    u.setId(rs.getInt(1));
                    u.setEmail(email);
                    u.setCreatedAt(rs.getTimestamp(3));
                    return u;
                }

            });
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public User findUserById(final long userId) {
        String sql = "select id, email, created_at from users where id = ?";
        try {
            User user = getJdbcTemplate().queryForObject(sql, new Object[]{userId}, new RowMapper<User>() {

                public User mapRow(ResultSet rs, int arg1) throws SQLException {
                    User u = new User();
                    u.setId(userId);
                    u.setEmail(rs.getString(2));
                    u.setCreatedAt(rs.getTimestamp(3));
                    return u;
                }

            });
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public User findUserByCustomerUserId(final long customerUserId) {
        String sql = "select u.id, u.email, u.created_at from users u, customer_user cu where u.id = cu.user_id and cu.id = ?";
        try {
            User user = getJdbcTemplate().queryForObject(sql, new Object[]{customerUserId}, new RowMapper<User>() {

                public User mapRow(ResultSet rs, int arg1) throws SQLException {
                    User u = new User();
                    u.setId(rs.getLong(1));
                    u.setEmail(rs.getString(2));
                    u.setCreatedAt(rs.getTimestamp(3));
                    return u;
                }

            });
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Long findCustomerUserIdByUserId(long userId, long customerId) {
        String sql = "select id from customer_user where user_id  = ? and customer_id = ?";
        try {
            Long id = getJdbcTemplate().queryForObject(sql, new Object[]{userId, customerId}, new RowMapper<Long>() {

                public Long mapRow(ResultSet rs, int arg1) throws SQLException {
                    return rs.getLong(1);
                }

            });
            return id;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Long findCustomerUserIdByEmail(String email, long customerId) {
        String sql = "select cu.id from users u, customer_user cu where u.id = cu.user_id and cu.customer_id = ? and u.email = ?";
        try {
            Long id = getJdbcTemplate().queryForObject(sql, new Object[]{customerId, email}, new RowMapper<Long>() {

                public Long mapRow(ResultSet rs, int arg1) throws SQLException {
                    return rs.getLong(1);
                }

            });
            return id;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User registerNewUser(final User user) {
        user.setEmail(user.getEmail().toLowerCase());
        final String insertSQL = "insert into users(email) values (?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{user.getEmail()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        user.setId((Long) holder.getKeys().get("id"));
        user.setCreatedAt((Timestamp) holder.getKeys().get("created_at"));
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerUser registerNewCustomerUser(final CustomerUser user) {
        final String insertSQL = "insert into customer_user(customer_id, user_id, first_name, last_name, password, gender_id, birth_date, profile_image, country_id, state_id, city_id, state_text, city_text, address, postalCode, phone_number) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(
                        new Object[]{user.getCustomerId(), user.getUserId(), user.getFirstName(), user.getLastName(),
                                user.getPassword(), user.getGenderId(), user.getBirthDate(), user.getProfileImage(),
                                (user.getCountryId() == Country.UNDEFINED_COUNTRY) ? null : user.getCountryId(),
                                (user.getStatedId() == State.UNDEFINED_STATE) ? null : user.getStatedId(),
                                (user.getCityId() == City.UNDEFINED_CITY) ? null : user.getCityId(),
                                user.getStateText(), user.getCityText(), user.getAddress(), user.getPostalCode(), user.getPhoneNumber()})
                        .setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        user.setId((Long) holder.getKeys().get("id"));
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerUser updateCustomerUser(final CustomerUser user) {
        final String insertSQL = "update customer_user set first_name = ?, last_name = ?, profile_image = ?, country_id = ?, state_id = ?, city_id = ?, state_text = ?, city_text = ?, address = ?, postalCode = ?, phone_number = ? where id = ?";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL);
                new ArgumentPreparedStatementSetter(new Object[]{user.getFirstName(), user.getLastName(),
                        user.getProfileImage(),
                        (user.getCountryId() == Country.UNDEFINED_COUNTRY) ? null : user.getCountryId(),
                        (user.getStatedId() == State.UNDEFINED_STATE) ? null : user.getStatedId(),
                        (user.getCityId() == City.UNDEFINED_CITY) ? null : user.getCityId(), user.getStateText(),
                        user.getCityText(), user.getAddress(), user.getPostalCode(), user.getPhoneNumber(), user.getId()}).setValues(ps);
                return ps;
            }
        };
        int records = getJdbcTemplate().update(psc);
        if (records == 0) {
            return null;
        }
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerUser updateCustomerUserEmail(final CustomerUser user) {
        final String insertSQL = "update customer_user set user_id = ? where id = ?";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL);
                new ArgumentPreparedStatementSetter(new Object[]{user.getUserId(), user.getId()}).setValues(ps);
                return ps;
            }
        };
        int records = getJdbcTemplate().update(psc);
        if (records == 0) {
            return null;
        }
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ApplicationParameter> findAllApplicationParameters(long applicationId) {
        String querySQL = "select id, application_id, name, value, description from application_parameter where application_id = ?";
        try {
            ArrayList<ApplicationParameter> appParameters = new ArrayList<ApplicationParameter>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{applicationId});
            for (Map<String, Object> m : result) {
                ApplicationParameter appParam = new ApplicationParameter();
                appParam.setId((Long) m.get("id"));
                appParam.setApplicationId(applicationId);
                appParam.setName((String) m.get("name"));
                appParam.setValue((String) m.get("value"));
                appParam.setDescription((String) m.get("description"));
                appParameters.add(appParam);
            }
            return appParameters;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerProperty> getCustomerProperties(long customerId) {
        String querySQL = "select customer_id, property_id, lang_id, property_value from customer_property_lang where customer_id = ?";
        try {
            ArrayList<CustomerProperty> custProperties = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                CustomerProperty custProp = new CustomerProperty();
                custProp.setCustomerId((Long) m.get("customer_id"));
                custProp.setPropertyId((Integer) m.get("property_id"));
                custProp.setLangId((Integer) m.get("lang_id"));
                custProp.setPropertyValue((String) m.get("property_value"));
                custProperties.add(custProp);
            }
            return custProperties;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerProperty findCustomerProperty(final long customerId, final int langId, final String propertyName) {
        String sql = "select cp.property_id, cp.property_value from customer_property_lang cp, property p where cp.property_id = p.id and cp.lang_id = ? and p.name = ? and cp.customer_id = ? ";
        try {
            CustomerProperty user = getJdbcTemplate().queryForObject(sql, new Object[]{langId, propertyName, customerId}, new RowMapper<CustomerProperty>() {

                public CustomerProperty mapRow(ResultSet rs, int arg1) throws SQLException {
                    CustomerProperty u = new CustomerProperty();
                    u.setCustomerId(customerId);
                    u.setLangId(langId);
                    u.setPropertyId(rs.getInt(1));
                    u.setPropertyValue(rs.getString(2));
                    return u;
                }

            });
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<SystemMessage> listAllSystemMessages() {
        String querySQL = "select id, name from system_message";
        try {
            ArrayList<SystemMessage> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                SystemMessage tmp = new SystemMessage();
                tmp.setId(((Number) m.get("id")).longValue());
                tmp.setName((String) m.get("name"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerSystemMessage> getCustomerSystemMessages(long customerId) {
        String querySQL = "select customer_id, system_message_id, lang_id, message from customer_system_messages_lang where customer_id = ?";
        try {
            ArrayList<CustomerSystemMessage> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                CustomerSystemMessage item = new CustomerSystemMessage();
                item.setCustomerId((Long) m.get("customer_id"));
                item.setSystemMessageId((Long) m.get("system_message_id"));
                item.setLangId((Integer) m.get("lang_id"));
                item.setMessage((String) m.get("message"));
                list.add(item);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EmailAgent> listAllEmailAgents() {
        String sql = "select id,name,url,healthychecks, isHealthy, isEnabled, healthyDate from email_agent where isEnabled";
        try {
            List<EmailAgent> list = getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper(EmailAgent.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EmailAgent findEmailAgentById(int emailAgentId) {
        final String sql = "select * from email_agent where id = ?";
        EmailAgent result = (EmailAgent) getJdbcTemplate().queryForObject(sql, new Object[]{emailAgentId}, new BeanPropertyRowMapper(EmailAgent.class));
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateEmailAgent(EmailAgent agent) {
        final String sql = "update email_agent set healthyChecks = ?, isHealthy = ?, isEnabled = ?, healthyDate = current_timestamp where id = ?";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql);
                new ArgumentPreparedStatementSetter(new Object[]{agent.getHealthyChecks(), agent.isHealthy(), agent.isEnabled(), agent.getId()}).setValues(ps);
                return ps;
            }
        };
        getJdbcTemplate().update(psc);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EmailAgentHist registerNewEmailAgentHist(final EmailAgentHist emailAgentHist) {
        final String insertSQL = "insert into EMAIL_AGENT_HIST(emailAgentId, minResponseTime, maxResponseTime, avgResponseTime, emailTypeId) values (?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(
                        new Object[]{emailAgentHist.getEmailAgentId(), emailAgentHist.getMinResponseTime(), emailAgentHist.getMaxResponseTime(), emailAgentHist.getAvgResponseTime(), emailAgentHist.getEmailType()})
                        .setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        emailAgentHist.setId((Long) holder.getKeys().get("id"));
        return emailAgentHist;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public OTPKey registerNewOTPKey(final OTPKey otpKey) {
        final String insertSQL = "insert into OTP_KEY(customer_user_id, application_id, code) values (?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(
                        new Object[]{otpKey.getCustomer_user_id(), otpKey.getApplication_id(), otpKey.getCode()})
                        .setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        otpKey.setId((Long) holder.getKeys().get("id"));
        return otpKey;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public OTPKey findOTPById(long otpId) {
        final String sql = "select * from otp_key where id = ?";
        OTPKey result = (OTPKey) getJdbcTemplate().queryForObject(sql, new Object[]{otpId}, new BeanPropertyRowMapper<OTPKey>(OTPKey.class));
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public OTPKey findCustomerUserOTPByApplicationAndCode(long customerUserId, long applicationId, String code) {
        final String sql = "select * from otp_key where customer_user_id = ? and application_id = ? and code = ?";
        OTPKey result = (OTPKey) getJdbcTemplate().queryForObject(sql, new Object[]{customerUserId, applicationId, code}, new BeanPropertyRowMapper<OTPKey>(OTPKey.class));
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateOTPKey(OTPKey otpKey) {
        final String sql = "update otp_key set otp_key_status_id = ?, validation_try_count = ?, last_validation_try = current_timestamp where id = ?";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql);
                new ArgumentPreparedStatementSetter(new Object[]{otpKey.getOtp_key_status_id(), otpKey.getValidation_try_count(), otpKey.getId()}).setValues(ps);
                return ps;
            }
        };
        getJdbcTemplate().update(psc);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionDetailInfo> findCustomerUserTransactions(long customerUserId, long applicationId, Timestamp fromTS, Timestamp toTS) {
        String sql = "select t.id txId, ts.name status, t.tx_date txDate, fl.label fund, ccpd.credit_card_masked cardMasked, ccb.name cardBrand, td.amount, t.is_scheduled is_scheduled from transaction t, transaction_detail td, transaction_status ts, transaction_user_data tud, credit_card_payment_Data ccpd, credit_Card_brand ccb, fund_lang fl where t.id = td.transaction_id and t.tx_status_id = ts.id and t.tx_user_data_id = tud.id and t.tx_payment_data_id = ccpd.payment_Data_id and ccpd.credit_card_brand_id = ccb.id and td.fund_id = fl.fund_id and t.tx_lang_id = fl.lang_id and t.tx_date >= ? and t.tx_date < ? and t.application_id = ? and t.tx_status_id = ? and tud.customer_user_id = ? order by t.id desc";
        try {
            List<TransactionDetailInfo> list = getJdbcTemplate().query(sql, new Object[]{fromTS, toTS, applicationId, Transaction.SUCCESS_TX, customerUserId}, new BeanPropertyRowMapper<TransactionDetailInfo>(TransactionDetailInfo.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionDetailInfo> findCustomerUserTransactionsAll(long customerUserId) {
        String sql = "select t.id txId, ts.name status, t.tx_date txDate, fl.label fund, ccpd.credit_card_masked cardMasked, ccb.name cardBrand, td.amount, t.is_scheduled is_scheduled, t.gwauthid_1 paymentInfo, t.tx_id txInternalId from transaction t inner join transaction_status ts on ts.id = t.tx_status_id inner join transaction_user_data tud on t.tx_user_data_id = tud.id inner join transaction_detail td on td.transaction_id = t.id inner join fund f on f.id = td.fund_id inner join fund_lang fl on fl.fund_id = f.id and fl.lang_id = t.tx_lang_id left join credit_card_payment_data ccpd on t.tx_payment_data_id = ccpd.payment_data_id left join credit_card_brand ccb on ccpd.credit_card_brand_id = ccb.id where tud.customer_user_id = ? order by t.id;";
        try {
            List<TransactionDetailInfo> list = getJdbcTemplate().query(sql, new Object[]{customerUserId}, new BeanPropertyRowMapper<TransactionDetailInfo>(TransactionDetailInfo.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionDetailInfo> findCustomerUserTransactionInfo(long transactionId) {
        String sql = "select t.id txId, ts.name status, t.tx_date txDate, fl.label fund, ccpd.credit_card_masked cardMasked, ccb.name cardBrand, td.amount, t.is_scheduled is_scheduled, t.gwauthid_1 paymentInfo, t.tx_id txInternalId from transaction t inner join transaction_status ts on ts.id = t.tx_status_id inner join transaction_user_data tud on t.tx_user_data_id = tud.id inner join transaction_detail td on td.transaction_id = t.id inner join fund f on f.id = td.fund_id inner join fund_lang fl on fl.fund_id = f.id and fl.lang_id = t.tx_lang_id left join credit_card_payment_data ccpd on t.tx_payment_data_id = ccpd.payment_data_id left join credit_card_brand ccb on ccpd.credit_card_brand_id = ccb.id where t.id = ? order by t.id;";
        try {
            List<TransactionDetailInfo> list = getJdbcTemplate().query(sql, new Object[]{transactionId}, new BeanPropertyRowMapper<TransactionDetailInfo>(TransactionDetailInfo.class));
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Transaction findTransactionById(long transactionId) {
        String sql = "select id, tx_id, tx_date, tx_user_data_id, tx_payment_data_id, tx_status_id, customer_id, application_id, amount, gwAuthId_1, gwAuthId_2, tx_payment_gw_id, tx_transaction_source_id, tx_transaction_campaing_id, tx_transaction_medium_id, tx_lang_id, is_scheduled, discount, commission from transaction where id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{transactionId}, new RowMapper<Transaction>() {

                public Transaction mapRow(ResultSet rs, int arg1) throws SQLException {
                    Transaction c = new Transaction();
                    c.setId(rs.getLong(1));
                    c.setTransactionId(rs.getString(2));
                    c.setTransactionDate(rs.getTimestamp(3));
                    c.setTransactionUserDataId(rs.getLong(4));
                    c.setTransactionPaymentDataId(rs.getLong(5));
                    c.setTransactionStatus(rs.getInt(6));
                    c.setCustomerId(rs.getLong(7));
                    c.setApplicationId(rs.getLong(8));
                    c.setAmount(rs.getDouble(9));
                    c.setAuthGw1(rs.getString(10));
                    c.setAuthGw2(rs.getString(11));
                    c.setPaymentGwId(rs.getInt(12));
                    c.setSourceId(rs.getInt(13));
                    c.setCampaingId(rs.getLong(14));
                    c.setMediumId(rs.getLong(15));
                    c.setLangId(rs.getInt(16));
                    c.setScheduled(rs.getBoolean(17));
                    c.setDiscount(rs.getDouble(18));
                    c.setCommission(rs.getDouble(19));
                    return c;
                }

            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionMedium> listCustomerMediums(long customerId) {
        final String querySQL = "select id, name, key, description, customer_id from transaction_medium where customer_id = ? order by id";
        try {
            ArrayList<TransactionMedium> custMediumList = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                TransactionMedium custMedium = new TransactionMedium();
                custMedium.setId(((Number) m.get("id")).longValue());
                custMedium.setName((String) m.get("name"));
                custMedium.setKey((String) m.get("key"));
                custMedium.setDescription((String) m.get("description"));
                custMedium.setCustomer_id(customerId);
                custMediumList.add(custMedium);
            }
            return custMediumList;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionMedium addTransactionMedium(final TransactionMedium transactionMedium) {
        final String insertSQL = "INSERT INTO TRANSACTION_MEDIUM(name, key, description, customer_id) values (?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{transactionMedium.getName(), transactionMedium.getKey(), transactionMedium.getDescription(), transactionMedium.getCustomer_id()}).setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(psc, holder);
        transactionMedium.setId((Long) holder.getKeys().get("id"));
        return transactionMedium;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Continent> listAllContinents() {
        String querySQL = "select id, name from continents";
        try {
            ArrayList<Continent> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                Continent tmp = new Continent();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public ExternalPaymentType findExternalPaymentType(int id) {
        final String sql = "select id, name, icon_url from external_payment_type where id = ?";
        ExternalPaymentType result = (ExternalPaymentType) getJdbcTemplate().queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<ExternalPaymentType>(ExternalPaymentType.class));
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ExternalPaymentType> listAllExternalPaymentTypes() {
        String querySQL = "select id, name, icon_url from external_payment_type";
        try {
            ArrayList<ExternalPaymentType> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                ExternalPaymentType tmp = new ExternalPaymentType();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                tmp.setIconUrl((String) m.get("icon_url"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerSupportedExternalPaymentType> listCustomerSupportedExternalPaymentTypes(long customerId) {
        final String querySQL = "select customer_id, external_pt_id, is_enabled from CUSTOMER_SUPPORTED_EXTERNAL_PAYMENT_TYPE where customer_id = ?";
        try {
            ArrayList<CustomerSupportedExternalPaymentType> custCupEPTList = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
                CustomerSupportedExternalPaymentType custSupEPT = new CustomerSupportedExternalPaymentType();
                custSupEPT.setCustomerId(customerId);
                custSupEPT.setExternalPTId(((Number) m.get("external_pt_id")).intValue());
                custSupEPT.setEnabled((Boolean) m.get("is_enabled"));
                custCupEPTList.add(custSupEPT);
            }
            return custCupEPTList;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<DiscountType> listAllDiscountTypes() {
        String querySQL = "select id, name from discount_type";
        try {
            ArrayList<DiscountType> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL);
            for (Map<String, Object> m : result) {
                DiscountType tmp = new DiscountType();
                tmp.setId((Integer) m.get("id"));
                tmp.setName((String) m.get("name"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public UniqueCustomerApplicationUserCode registerNewUniqueCustomerApplicationUserCode(final UniqueCustomerApplicationUserCode userCode) {
        final String insertSQL = "insert into UNIQUE_CUSTOMER_APPLICATION_USER_CODE(customer_user_id, application_id, user_code) values (?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(
                        new Object[]{userCode.getCustomerUserId(), userCode.getApplicationId(), userCode.getUserCode()})
                        .setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        userCode.setId((Long) holder.getKeys().get("id"));
        return userCode;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public UniqueCustomerApplicationUserCode findUniqueCustomerApplicationUserCode(long customerUserId, long applicationId, boolean isEnabled) {
        final String sql = "select id, customer_user_id customerUserId, application_id applicationId, user_code userCode, created_at createdAt, unsuccessful_validation_tries unsuccessfulValidationTries, is_enabled isEnabled  from UNIQUE_CUSTOMER_APPLICATION_USER_CODE where customer_user_id = ? and application_id = ? and is_enabled = ?";
        UniqueCustomerApplicationUserCode result = (UniqueCustomerApplicationUserCode) getJdbcTemplate().queryForObject(sql, new Object[]{customerUserId, applicationId, isEnabled}, new BeanPropertyRowMapper<UniqueCustomerApplicationUserCode>(UniqueCustomerApplicationUserCode.class));
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateUniqueCustomerApplicationUserCode(UniqueCustomerApplicationUserCode userCode) {
        final String sql = "update UNIQUE_CUSTOMER_APPLICATION_USER_CODE set unsuccessful_validation_tries = ?, is_enabled = ? where id = ?";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql);
                new ArgumentPreparedStatementSetter(new Object[]{userCode.getUnsuccessfulValidationTries(), userCode.isEnabled(), userCode.getId()}).setValues(ps);
                return ps;
            }
        };
        getJdbcTemplate().update(psc);
    }


    public CustomerUser updateCustomerUserPassword(CustomerUser user) {
        final String insertSQL = "update customer_user set password = ? where id = ?";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL);
                new ArgumentPreparedStatementSetter(new Object[]{user.getPassword(), user.getId()}).setValues(ps);
                return ps;
            }
        };
        int records = getJdbcTemplate().update(psc);
        if (records == 0) {
            return null;
        }
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Fund> listAllTransactionFundsByTransactionId(long transactionid) {
        String querySQL = "select f.id, f.customer_id, f.business_code, f.is_default, f.created_at, f.name, f.valid_from, f.valid_to, f.isschedulable from transaction_detail td, fund f where td.fund_id = f.id and td.transaction_id = ?";
        try {
            ArrayList<Fund> list = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, transactionid);
            for (Map<String, Object> m : result) {
                Fund tmp = new Fund();
                tmp.setId((Integer) m.get("id"));
                tmp.setCustomerId((Long) m.get("customer_id"));
                tmp.setBusinessCode((String) m.get("business_code"));
                tmp.setDefault((Boolean) m.get("is_default"));
                tmp.setName((String) m.get("name"));
                tmp.setValidFrom((Timestamp) m.get("valid_from"));
                tmp.setValidTo((Timestamp) m.get("valid_to"));
                tmp.setIsSchedulable((Boolean) m.get("isschedulable"));
                list.add(tmp);
            }
            return list;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EventCommissionSettings addEventCommissionSettings(final EventCommissionSettings item) {
        final String insertSQL = "INSERT INTO EVENT_COMMISSION_SETTINGS(customer_id, application_id, minimum_commission, maximum_commission, commission_type_id, commission_payer_id, commission_value, free_commission_value) values (?,?,?,?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(new Object[]{item.getCustomerId(), item.getApplicationid(), item.getMinimumCommission(), item.getMaximumCommission(), item.getCommissionTypeId(), item.getCommissionPayerId(), item.getCommissionValue(), item.getFreeCommissionValue()})
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
    public void updateEventCommissionSettings(final EventCommissionSettings eventCommissionSettings) {
        final String updateSQL = "UPDATE EVENT_COMMISSION_SETTINGS SET MINIMUM_COMMISSION = ?, MAXIMUM_COMMISSION = ?, COMMISSION_TYPE_ID = ?, COMMISSION_PAYER_ID = ?, COMMISSION_VALUE = ?, FREE_COMMISSION_VALUE = ?, updated_at = current_timestamp where ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{eventCommissionSettings.getMinimumCommission(), eventCommissionSettings.getMaximumCommission(), eventCommissionSettings.getCommissionTypeId(), eventCommissionSettings.getCommissionPayerId(), eventCommissionSettings.getCommissionValue(), eventCommissionSettings.getFreeCommissionValue(), eventCommissionSettings.getId()});
        ;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EventCommissionSettings findEventCommissionSettings(long customerId, long applicationId) {
        String sql = "SELECT id, customer_id, application_id, minimum_commission, maximum_commission, commission_type_id, commission_payer_id, commission_value, free_commission_value FROM EVENT_COMMISSION_SETTINGS WHERE customer_id = ? and application_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{customerId, applicationId}, new RowMapper<EventCommissionSettings>() {

                public EventCommissionSettings mapRow(ResultSet rs, int arg1) throws SQLException {
                    EventCommissionSettings c = new EventCommissionSettings();
                    c.setId(rs.getLong(1));
                    c.setCustomerId(rs.getLong(2));
                    c.setApplicationid(rs.getLong(3));
                    c.setMinimumCommission(rs.getDouble(4));
                    c.setMaximumCommission(rs.getLong(5));
                    c.setCommissionTypeId(rs.getInt(6));
                    c.setCommissionPayerId(rs.getInt(7));
                    c.setCommissionValue(rs.getDouble(8));
                    c.setFreeCommissionValue(rs.getDouble(9));
                    return c;
                }

            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EventCommissionSettings findEventCommissionSettings(long id) {
        String sql = "SELECT id, customer_id, application_id, minimum_commission, maximum_commission, commission_type_id, commission_payer_id, commission_value, free_commission_value FROM EVENT_COMMISSION_SETTINGS WHERE customer_id = ? and application_id = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<EventCommissionSettings>() {

                public EventCommissionSettings mapRow(ResultSet rs, int arg1) throws SQLException {
                    EventCommissionSettings c = new EventCommissionSettings();
                    c.setId(rs.getLong(1));
                    c.setCustomerId(rs.getLong(2));
                    c.setApplicationid(rs.getLong(3));
                    c.setMinimumCommission(rs.getDouble(4));
                    c.setMaximumCommission(rs.getLong(5));
                    c.setCommissionTypeId(rs.getInt(6));
                    c.setCommissionPayerId(rs.getInt(7));
                    c.setCommissionValue(rs.getDouble(8));
                    c.setFreeCommissionValue(rs.getDouble(9));
                    return c;
                }

            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateCustomerPaymentGatewayConnectedAccount(final long customerId, final long paymentGatewayId, final String connectedAccountId) {
        final String updateSQL = "UPDATE CUSTOMER_PAYMENT_GATEWAY SET CONNECTED_PAYMENT_GATEWAY_ACCOUNT = ? WHERE CUSTOMER_ID = ? AND PAYMENT_GATEWAY_ID = ?";
        getJdbcTemplate().update(updateSQL, new Object[]{connectedAccountId, customerId, paymentGatewayId});
        ;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ValidationCode registerNewValidationCode(final ValidationCode validationCode) {
        final String insertSQL = "insert into VALIDATION_CODE(code, device, device_type_id, expires_at) values (?,?,?,?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                new ArgumentPreparedStatementSetter(
                        new Object[]{validationCode.getCode(), validationCode.getDevice(), validationCode.getDeviceTypeId(), validationCode.getExpiresAt()})
                        .setValues(ps);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int records = getJdbcTemplate().update(psc, holder);
        if (records == 0) {
            return null;
        }
        validationCode.setId((Long) holder.getKeys().get("id"));
        return validationCode;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public ValidationCode findValidationCodeById(long validationCodeId) {
        final String querySQL = "select id, code, device, device_type_id, try_count, is_used, is_expired, expires_at, created_at from validation_code where id = ?";
        try {
            return getJdbcTemplate().queryForObject(querySQL, new Object[]{validationCodeId},
                    new RowMapper<ValidationCode>() {

                        public ValidationCode mapRow(ResultSet rs, int arg1) throws SQLException {
                            ValidationCode validationCode = new ValidationCode();
                            validationCode.setId(rs.getLong(1));
                            validationCode.setCode(rs.getString(2));
                            validationCode.setDevice(rs.getString(3));
                            validationCode.setDeviceTypeId(rs.getInt(4));
                            validationCode.setTryCount(rs.getInt(5));
                            validationCode.setUsed(rs.getBoolean(6));
                            validationCode.setExpired(rs.getBoolean(7));
                            validationCode.setExpiresAt(rs.getTimestamp(8));
                            validationCode.setCreatedAt(rs.getTimestamp(9));
                            return validationCode;
                        }

                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateValidationCode(ValidationCode validationCode) {
        final String sql = "update validation_code set is_expired = ?, is_used = ?, try_count = ? where id = ?";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql);
                new ArgumentPreparedStatementSetter(new Object[]{validationCode.isExpired(), validationCode.isUsed(), validationCode.getTryCount(), validationCode.getId()}).setValues(ps);
                return ps;
            }
        };
        getJdbcTemplate().update(psc);
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerUser getLastApprovedUserTransactionByEmail(String email)  {
		final String querySQL = "SELECT CU.ID, CU.CUSTOMER_ID, CU.FIRST_NAME, CU.LAST_NAME, CU.PHONE_NUMBER  FROM TRANSACTION T INNER JOIN TRANSACTION_USER_DATA TUD ON T.TX_USER_DATA_ID = TUD.ID INNER JOIN CUSTOMER_USER CU ON TUD.CUSTOMER_USER_ID = CU.ID INNER JOIN USERS U ON CU.USER_ID = U.ID WHERE T.TX_STATUS_ID = ? AND U.EMAIL = ? ORDER BY CU.ID DESC";
		List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[] {Transaction.SUCCESS_TX,email});
		if (!result.isEmpty()) {
			Map<String, Object> m = result.get(0);
			CustomerUser cu = new CustomerUser();
			cu.setId(((Number) m.get("id")).longValue());
			cu.setFirstName((String) m.get("first_name"));
			cu.setLastName((String) m.get("last_name"));
			cu.setPhoneNumber((String) m.get("phone_number"));
			return cu;
		} else {
			return null;
		}		
	}
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public HashMap<String, Object> findUsersEventInfo(Long customerId) {
    	HashMap<String, Object> result = new HashMap<>();
    	String sql_usuarios_online  = "SELECT COUNT(*) as registered_users FROM CUSTOMER_USER CU WHERE CUSTOMER_ID = ? AND EXISTS (SELECT * FROM USER_ACCESS_TOKEN UAT WHERE UAT.CUSTOMER_USER_ID = CU.ID AND UAT.STATUS_ID = 1 AND UAT.CREATED_AT >= date_trunc('day',current_timestamp))";   
    	String sql_usuarios_registracos = "SELECT COUNT(*) as online_users FROM CUSTOMER_USER CU WHERE CUSTOMER_ID = ? AND EXISTS ( SELECT * FROM TRANSACTION T INNER JOIN TRANSACTION_USER_DATA TUD ON T.TX_USER_DATA_ID = TUD.ID AND T.TX_STATUS_ID = 5 AND TUD.CUSTOMER_USER_ID = CU.ID)";   
    	try {
        	int registered_users = getJdbcTemplate().queryForObject(sql_usuarios_registracos, new Object[]{customerId}, new RowMapper<Integer>() {
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {	
					return rs.getInt(1);
				}
			});
        	int online_users = getJdbcTemplate().queryForObject(sql_usuarios_online, new Object[]{customerId}, new RowMapper<Integer>() {
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {	
					return rs.getInt(1);
				}
			});
        	result.put("registered_users", registered_users);
        	result.put("online_users", online_users);
            return result;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EventDescription> getEventDescription(long eventId) {
        final String querySQL = "select el.event_id, el.lang_id, el.label, el.description, el.tag_id, tl.name from event_lang el inner join tag_lang tl on tl.id = el.tag_id inner join event e on el.event_id = e.id where e.id = ?";
        try {
            ArrayList<EventDescription> events = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{eventId});
            for (Map<String, Object> m : result) {
            	EventDescription event = new EventDescription();
            	event.setEventId((Integer) m.get("event_id"));
                event.setEventDescription((String) m.get("description"));
                event.setLangId((Integer) m.get("lang_id"));
                event.setEventLabel((String) m.get("label"));
            	event.setTagId((Integer) m.get("tag_id"));
            	event.setTagDescription((String) m.get("name"));
                events.add(event);
            }
            return events;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<VenueDescription> getVenueDescription(long venueId) {
        final String querySQL = "select vl.venue_id, vl.lang_id, vl.label, vl.description, vl.tag_id, tl.name from venue_lang vl  inner join tag_lang tl on tl.id = vl.tag_id inner join venue v on vl.venue_id = v.id where v.id = ?";
        try {
            ArrayList<VenueDescription> venues = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{venueId});
            for (Map<String, Object> m : result) {
            	VenueDescription venue = new VenueDescription();
            	venue.setVenueId((Integer) m.get("venue_id"));
                venue.setVenueDescription((String) m.get("description"));
                venue.setLangId((Integer) m.get("lang_id"));
                venue.setVenueLabel((String) m.get("label"));
                venue.setTagId((Integer) m.get("tag_id"));
                venue.setTagDescription((String) m.get("name"));
                venues.add(venue);
            }
            return venues;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<GeneralDescription> getGeneralDescription(long customerId) {
        final String querySQL = "select gl.customer_id, gl.lang_id, gl.label, gl.description, gl.tag_id, tl.name from general_lang gl inner join tag_lang tl on tl.id = gl.tag_id inner join customer c on gl.customer_id = c.id where c.id = ?";
        try {
            ArrayList<GeneralDescription> generals = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{customerId});
            for (Map<String, Object> m : result) {
            	GeneralDescription general = new GeneralDescription();
            	general.setCustomerId((Integer) m.get("customer_id"));
            	general.setGeneralDescription((String) m.get("description"));
            	general.setLangId((Integer) m.get("lang_id"));
            	general.setGeneralLabel((String) m.get("label"));
            	general.setTagId((Integer) m.get("tag_id"));
            	general.setTagDescription((String) m.get("name"));
            	generals.add(general);
            }
            return generals;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EventFaqLangDescription> getEventFaqLangDescription(long eventId) {
        final String querySQL = "select el.event_id, el.lang_id, el.label, el.description, el.tag_id, tl.name from event_faq_lang el inner join tag_lang tl on tl.id = el.tag_id inner join event e on el.event_id = e.id where e.id = ?";
        try {
            ArrayList<EventFaqLangDescription> events = new ArrayList<>();
            List<Map<String, Object>> result = getJdbcTemplate().queryForList(querySQL, new Object[]{eventId});
            for (Map<String, Object> m : result) {
            	EventFaqLangDescription event = new EventFaqLangDescription();
            	event.setEventId((Integer) m.get("event_id"));
                event.setDescription((String) m.get("description"));
                event.setLangId((Integer) m.get("lang_id"));
                event.setLabel((String) m.get("label"));
            	event.setTagId((Integer) m.get("tag_id"));
            	event.setTagDescription((String) m.get("name"));
                events.add(event);
            }
            return events;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    

}
