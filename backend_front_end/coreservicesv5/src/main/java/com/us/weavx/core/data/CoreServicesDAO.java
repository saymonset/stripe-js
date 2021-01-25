package com.us.weavx.core.data;

import com.us.weavx.core.exception.CoreServicesDAOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CoreServicesDAO {
	
	private DataSource ds;
	private Context ctx;
	
	public CoreServicesDAO() throws CoreServicesDAOException {
		try {
            ctx = new InitialContext();
            Context envContext = (Context) ctx.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/servicesDBDS");
        } catch (Exception e) {
			throw new CoreServicesDAOException(e);
		}
	}
	
	/*public HashMap<String, Object> findDeveloperAppCustomerInfo(String accessKey, String accessSecret, String devKey, String devSecret) throws CoreServicesDAOException, ObjectDoesNotExistsException, UnauthorizedAccessException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			String sql = "select ak.id, ak.key, ak.customer_id, ak.is_admin, dk.dev_key, a.id, a.name, a.description, a.short_token_duration, a.long_token_duration, a.short_user_token_duration, a.long_user_token_duration, a.created_by from access_keys ak, developer_key dk, application a where ak.application_id = a.id and (a.created_by = dk.developer_id or exists (select developer_id from developer_app where app_id = a.id and developer_id = dk.developer_id)) and ak.key = ? and ak.secret = ? and dk.dev_key = ? and dk.dev_secret = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, accessKey);
			pSt.setString(2, accessSecret);
			pSt.setString(3, devKey);
			pSt.setString(4, devSecret);
			rs = pSt.executeQuery();
			if (rs.next()) {
				AccessKey accessKeyTmp = new AccessKey(rs.getInt(1),accessKey,accessSecret,rs.getLong(6),rs.getBoolean(5),rs.getLong(3));
				Application appTmp = new Application(rs.getLong(6),rs.getString(7),rs.getString(8),rs.getLong(9),rs.getLong(10),rs.getLong(11), rs.getLong(12), rs.getLong(13),null);
				result.put("application", appTmp);
				result.put("accessKey", accessKeyTmp);
				return result;
			} else {
				throw new UnauthorizedAccessException();
			}
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	
/*	public void registerNewAccessToken(AccessToken token, long duration) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		try {
			String sql = "insert into access_token(token, access_key_id, expire_at) values (?,?,current_timestamp + interval '"+duration+" minutes')";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, token.getToken());
			pSt.setLong(2, token.getAccessKeyId());
			int result = pSt.executeUpdate();
			if (result <= 0) {
				throw new CoreServicesDAOException(DAOErrorMessages.NO_RECORDS_UPDATED_ERROR);
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public void changeAccessTokenStatus(AccessToken token)  throws CoreServicesDAOException, ObjectDoesNotExistsException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		try {
			String sql = "update access_token set status_id = ? where id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setInt(1, token.getStatus());
			pSt.setLong(2, token.getId());
			int result = pSt.executeUpdate();
			if (result <= 0) {
				throw new ObjectDoesNotExistsException();
			}
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		}
	}*/
	
	
/*	public AccessInfo getAccessInfo(String accessToken) throws CoreServicesDAOException, ObjectDoesNotExistsException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select at.id, at.token, at.created_at, at.expire_at, at.status_id, at.access_key_id, ak.application_id, ak.customer_id from access_token at, access_keys ak where at.access_key_id = ak.id and token = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, accessToken);
			rs = pSt.executeQuery();
			if (rs.next()) {
				AccessToken tokenInfo = new AccessToken(rs.getLong("id"),rs.getString("token"),rs.getTimestamp("expire_at"),rs.getInt("status_id"),rs.getTimestamp("created_at"),rs.getLong("access_key_id"));
				AccessInfo info = new AccessInfo(tokenInfo,rs.getLong("customer_id"),rs.getLong("application_id"));
				return info;
			} else {
				throw new ObjectDoesNotExistsException();
			}
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/

	
	/*public AccessToken getTokenInfo(String accessToken) throws CoreServicesDAOException, ObjectDoesNotExistsException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id, token, created_at, expire_at, status_id, access_key_id from access_token where token = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, accessToken);
			rs = pSt.executeQuery();
			if (rs.next()) {
				AccessToken tokenInfo = new AccessToken(rs.getLong("id"),rs.getString("token"),rs.getTimestamp("expire_at"),rs.getInt("status_id"),rs.getTimestamp("created_at"),rs.getLong("access_key_id"));
				return tokenInfo;
			} else {
				throw new ObjectDoesNotExistsException();
			}
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public Method getMethodInfoByName(String methodName) throws CoreServicesDAOException, ObjectDoesNotExistsException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id, name, description, implementor_class, method_type_id, module_id from method where name = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, methodName);
			rs = pSt.executeQuery();
			if (rs.next()) {
				Method method = new Method(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("implementor_class"),rs.getInt("method_type_id"),rs.getInt("module_id"));
				return method;
			} else {
				throw new ObjectDoesNotExistsException();
			}
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	
	/*public IdentityProvider getIdentityProviderInfo(int identityProviderId) throws CoreServicesDAOException, ObjectDoesNotExistsException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id, provider_name, provider_implementor_class from identity_provider where id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setInt(1, identityProviderId);
			rs = pSt.executeQuery();
			if (rs.next()) {
				IdentityProvider provider = new IdentityProvider(rs.getInt("id"),rs.getString("provider_name"),rs.getString("provider_implementor_class"));
				return provider;
			} else {
				throw new ObjectDoesNotExistsException();
			}
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public ArrayList<IdentityProvider> listAllIdentityProviders() throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id, provider_name, provider_implementor_class from identity_provider";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			rs = pSt.executeQuery();
			ArrayList<IdentityProvider> result = new ArrayList<IdentityProvider>();
			while (rs.next()) {
				IdentityProvider provTmp = new IdentityProvider(rs.getInt("id"),rs.getString("provider_name"),"****");
				result.add(provTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public ArrayList<CustomerIdentityProvider> getCustomerIdentityProviders(long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select customer_id, provider_id, provider_key, provider_secret from customer_identity_provider where customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, customerId);
			rs = pSt.executeQuery();
			ArrayList<CustomerIdentityProvider> result = new ArrayList<CustomerIdentityProvider>();
			while (rs.next()) {
				CustomerIdentityProvider provTmp = new CustomerIdentityProvider(rs.getLong("customer_id"),rs.getInt("provider_id"),rs.getString("provider_key"),rs.getString("provider_secret"));
				result.add(provTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	
	/*public ArrayList<Property> listAllProperties() throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id, name from property";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			rs = pSt.executeQuery();
			ArrayList<Property> result = new ArrayList<Property>();
			while (rs.next()) {
				Property propTmp = new Property(rs.getInt("id"),rs.getString("name"));
				result.add(propTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
//	public ArrayList<Language> listAllLanguages() throws CoreServicesDAOException {
//		Connection cnx = null;
//		PreparedStatement pSt = null;
//		ResultSet rs = null;
//		try {
//			String sql = "select id, locale, description, is_default from lang";
//			cnx = ds.getConnection();
//			pSt = cnx.prepareStatement(sql);
//			rs = pSt.executeQuery();
//			ArrayList<Language> result = new ArrayList<Language>();
//			while (rs.next()) {
//				Language langTmp = new Language(rs.getInt("id"),rs.getString("locale"),rs.getString("description"),rs.getString("is_default"));
//				result.add(langTmp);
//			} 
//			return result;
//		} catch (RuntimeException e) {
//			throw new CoreServicesDAOException(e);
//		} catch (SQLException e) {
//			throw new CoreServicesDAOException(e);
//		} finally {
//			closeResource(rs);
//			closeResource(pSt);
//			closeResource(cnx);
//		}
//	}
	
	/*public ArrayList<CustomerProperty> getCustomerProperties(long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select customer_id, property_id, lang_id, property_value from customer_property_lang where customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, customerId);
			rs = pSt.executeQuery();
			ArrayList<CustomerProperty> result = new ArrayList<CustomerProperty>();
			while (rs.next()) {
				CustomerProperty propTmp = new CustomerProperty(rs.getLong("customer_id"),rs.getInt("property_id"),rs.getInt("lang_id"),rs.getString("property_value"));
				result.add(propTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
*/

	/*public ArrayList<FundDescription> getCustomerFundsDescription(long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select fl.fund_id, fl.lang_id, fl.label, fl.description from fund_lang fl inner join fund f on fl.fund_id = f.id where f.customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, customerId);
			rs = pSt.executeQuery();
			ArrayList<FundDescription> result = new ArrayList<FundDescription>();
			while (rs.next()) {
				FundDescription propTmp = new FundDescription(rs.getInt("fund_id"),rs.getInt("lang_id"),rs.getString("label"), rs.getString("description"));
				result.add(propTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}	*/
	
	/*public ArrayList<FundImage> getCustomerFundsImages(long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select fi.id, fund_id, image_url, lang_id from fund_image fi inner join fund f on fi.fund_id = f.id where f.customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, customerId);
			rs = pSt.executeQuery();
			ArrayList<FundImage> result = new ArrayList<FundImage>();
			while (rs.next()) {
				FundImage propTmp = new FundImage(rs.getInt("id"),rs.getInt("fund_id"),rs.getString("image_url"),rs.getInt("lang_id"));
				result.add(propTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public ArrayList<DonationAmount> getCustomerDonationAmounts(long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id, customer_id, amount from donation_amount where customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, customerId);
			rs = pSt.executeQuery();
			ArrayList<DonationAmount> result = new ArrayList<DonationAmount>();
			while (rs.next()) {
				DonationAmount propTmp = new DonationAmount(rs.getLong("id"),rs.getLong("customer_id"),rs.getDouble("amount"));
				result.add(propTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public ArrayList<Gender> getCustomerGenders(long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select g.id, g.name from gender g, customer_gender cg where cg.gender_id = g.id and cg.customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, customerId);
			rs = pSt.executeQuery();
			ArrayList<Gender> result = new ArrayList<Gender>();
			while (rs.next()) {
				Gender genderTmp = new Gender(rs.getLong("id"),rs.getString("name"));
				result.add(genderTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public ArrayList<GenderDescription> getCustomerGendersDescription(long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select customer_id, gender_id, lang_id, label, description from gender_customer_lang where customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, customerId);
			rs = pSt.executeQuery();
			ArrayList<GenderDescription> result = new ArrayList<GenderDescription>();
			while (rs.next()) {
				GenderDescription propTmp = new GenderDescription(customerId,rs.getInt("gender_id"),rs.getInt("lang_id"),rs.getString("label"),rs.getString("description"));
				result.add(propTmp);
			} 
			return result;
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}	*/
	
/*	public long registerNewApplication(Application app) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "insert into application(name, description, short_token_duration, long_token_duration, created_by) values (?,?,?,?,?)";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pSt.setString(1, app.getName());
			pSt.setString(2, app.getDescription());
			pSt.setLong(3, app.getShortTokenDuration());
			pSt.setLong(4, app.getLongTokenDuration());
			pSt.setLong(5, app.getDeveloperId());
			int result = pSt.executeUpdate();
			if (result <= 0) {
				throw new CoreServicesDAOException(DAOErrorMessages.NO_RECORDS_UPDATED_ERROR);
			}
			rs = pSt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong("id");
			} else {
				throw new Exception("App Id not generated.");
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public long registerNewDeveloper(Developer developer) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "insert into developer(name, email, password) values (?,?,?)";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pSt.setString(1, developer.getName());
			pSt.setString(2, developer.getEmail());
			pSt.setString(3, developer.getPassword());
			int result = pSt.executeUpdate();
			if (result <= 0) {
				throw new CoreServicesDAOException(DAOErrorMessages.NO_RECORDS_UPDATED_ERROR);
			}
			rs = pSt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong("id");
			} else {
				throw new Exception("Developer Id not generated.");
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
/*	public boolean existsDeveloperKey(String key) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select dev_key from developer_key where dev_key = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, key);
			rs = pSt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
	/*public void registerNewDeveloperKey(DeveloperKey key) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		try {
			String sql = "insert into developer_key(dev_key, dev_secret, productionEnabled, developer_id) values (?,?,?,?)";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, key.getDevKey());
			pSt.setString(2, key.getDevSecret());
			pSt.setBoolean(3, key.isProductionEnabled());
			pSt.setLong(4, key.getDeveloperId());
			int result = pSt.executeUpdate();
			if (result <= 0) {
				throw new CoreServicesDAOException(DAOErrorMessages.NO_RECORDS_UPDATED_ERROR);
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(pSt);
			closeResource(cnx);
		}
	}*/
	
/*	public boolean existsUser(String email) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id from users where email = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, email);
			rs = pSt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
	
	public boolean existsCustomerUser(String email, long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select cu.id from users u, customer_user cu where u.id = cu.user_id and cu.customer_id = ? and u.email = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, customerId);
			pSt.setString(2, email);
			rs = pSt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
	
	public User findUserByEmail(String email) throws CoreServicesDAOException, ObjectDoesNotExistsException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		User result = null;
		try {
			String sql = "select id, email, created_at from users where email = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, email);
			rs = pSt.executeQuery();
			if (rs.next()) {
				result = new User(rs.getLong("id"),rs.getString("email"),rs.getTimestamp("created_at"));
				return result;
			} else {
				throw new ObjectDoesNotExistsException();
			}
		} catch (RuntimeException e) {
			throw new CoreServicesDAOException(e);
		} catch (SQLException e) {
			throw new CoreServicesDAOException(e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}

	public User registerNewUser(User user) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "insert into users(email) values (?)";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pSt.setString(1, user.getEmail());
			int result = pSt.executeUpdate();
			if (result <= 0) {
				throw new CoreServicesDAOException(DAOErrorMessages.NO_RECORDS_UPDATED_ERROR);
			}
			rs = pSt.getGeneratedKeys();
			if (rs.next()) {
				user.setId(rs.getLong("id"));
				user.setCreatedAt(rs.getTimestamp("created_at"));
				return user;
			} else {
				throw new Exception("User Id not generated.");
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
	
	
	public CustomerUser findCustomerUser(long userId, long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id, customer_id, user_id, first_name, last_name, gender_id, birth_date, profile_image, country_id, state_id, state_text, city_id, city_text, address, postalCode  from customer_user where user_id  = ? and customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, userId);
			pSt.setLong(2, customerId);
			rs = pSt.executeQuery();
			if (rs.next()) {
				CustomerUser user = new CustomerUser(rs.getLong("id"),customerId,userId,rs.getString("first_name"),rs.getString("last_name"),"****",rs.getInt("gender_id"),rs.getDate("birth_date"),rs.getString("profile_image"),rs.getInt("country_id"),rs.getInt("state_id"),rs.getString("state_text"), rs.getInt("city_id"), rs.getString("city_text"), rs.getString("address"), rs.getString("postalCode"));
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
	
	public CustomerUser authenticateUser(String email, String password, long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select cu.id, cu.customer_id, cu.user_id, cu.first_name, cu.last_name, cu.gender_id, cu.birth_date, cu.profile_image, cu.country_id, cu.state_id, cu.state_text, cu.city_id, cu.city_text, cu.address, cu.postalCode  from customer_user cu, users u where cu.user_id = u.id and cu.password = ? and u.email  = ? and cu.customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, password);
			pSt.setString(2, email);
			pSt.setLong(3, customerId);
			rs = pSt.executeQuery();
			if (rs.next()) {
				CustomerUser user = new CustomerUser(rs.getLong("id"),customerId,rs.getLong("user_id"),rs.getString("first_name"),rs.getString("last_name"),"****",rs.getInt("gender_id"),rs.getDate("birth_date"),rs.getString("profile_image"),rs.getInt("country_id"),rs.getInt("state_id"),rs.getString("state_text"), rs.getInt("city_id"),rs.getString("city_text"), rs.getString("address"),rs.getString("postalCode"));
				return user;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
	
	public boolean existsCustomerUser(long userId, long customerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select id from customer_user where user_id  = ? and customer_id = ?";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, userId);
			pSt.setLong(2, customerId);
			rs = pSt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
	
	public CustomerUser registerNewCustomerUser(CustomerUser user) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "insert into customer_user(customer_id, user_id, first_name, last_name, password, gender_id, birth_date, profile_image, country_id, state_id, city_id, state_text, city_text, address, postalCode) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			cnx = ds.getConnection();
			pSt = cnx.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pSt.setLong(1, user.getCustomerId());
			pSt.setLong(2, user.getUserId());
			pSt.setString(3, user.getFirstName());
			pSt.setString(4, user.getLastName());
			pSt.setString(5, user.getPassword());
			if (user.getGenderId() != Gender.UNDEFINED_GENDER)
				pSt.setInt(6, user.getGenderId());
			else
				pSt.setNull(6, java.sql.Types.BIGINT);
			if (user.getBirthDate() != null)
				pSt.setDate(7, new java.sql.Date(user.getBirthDate().getTime()));
			else 
				pSt.setDate(7, null);
			pSt.setString(8, user.getProfileImage());
			if (user.getCountryId() != Country.UNDEFINED_COUNTRY)
				pSt.setInt(9, user.getCountryId());
			else 
				pSt.setNull(9, java.sql.Types.INTEGER);
			if (user.getStatedId() != State.UNDEFINED_STATE)
				pSt.setInt(10, user.getStatedId());
			else	
				pSt.setNull(10, java.sql.Types.INTEGER);
			if (user.getCityId() != City.UNDEFINED_CITY) 
				pSt.setInt(11, user.getCityId());
			else 
				pSt.setNull(11, java.sql.Types.INTEGER);
			pSt.setString(12, user.getStateText());
			pSt.setString(13, user.getCityText());
			pSt.setString(14, user.getAddress());
			pSt.setString(15, user.getPostalCode());
			int result = pSt.executeUpdate();
			if (result <= 0) {
				throw new CoreServicesDAOException(DAOErrorMessages.NO_RECORDS_UPDATED_ERROR);
			}
			rs = pSt.getGeneratedKeys();
			if (rs.next()) {
				user.setId(rs.getLong("id"));
				return user;
			} else {
				throw new Exception("Customer User Id not generated.");
			}
		} catch (Exception e) {
			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
		} finally {
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
*/	
//	public ExternalProfile findUserExternalProfile(int identityProviderId, long userId) throws CoreServicesDAOException {
//		Connection cnx = null;
//		PreparedStatement pSt = null;
//		ResultSet rs = null;
//		try {
//			String sql = "select id, id_identity_provider, user_id, access_token from external_profile where id_identity_provider = ? and user_id = ?";
//			cnx = ds.getConnection();
//			pSt = cnx.prepareStatement(sql);
//			pSt.setInt(1,identityProviderId);
//			pSt.setLong(2, userId);
//			rs = pSt.executeQuery();
//			if (rs.next()) {
//				ExternalProfile prof = new ExternalProfile(rs.getLong("id"),identityProviderId,userId,rs.getString("access_token"));
//				return prof;
//			} else {
//				return null;
//			}
//		} catch (Exception e) {
//			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
//		} finally {
//			closeResource(rs);
//			closeResource(pSt);
//			closeResource(cnx);
//		}
//	}	
//
//	public ExternalProfile registerNewExternalUser(ExternalProfile user) throws CoreServicesDAOException {
//		Connection cnx = null;
//		PreparedStatement pSt = null;
//		ResultSet rs = null;
//		try {
//			String sql = "insert into external_profile(id_identity_provider, user_id, access_token) values (?,?,?)";
//			cnx = ds.getConnection();
//			pSt = cnx.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
//			pSt.setInt(1, user.getIdentityProviderId());
//			pSt.setLong(2,  user.getUserId());
//			pSt.setString(3, user.getAccesstoken());
//			int result = pSt.executeUpdate();
//			if (result <= 0) {
//				throw new CoreServicesDAOException(DAOErrorMessages.NO_RECORDS_UPDATED_ERROR);
//			}
//			rs = pSt.getGeneratedKeys();
//			if (rs.next()) {
//				user.setId(rs.getLong("id"));
//				return user;
//			} else {
//				throw new Exception("External profile Id not generated.");
//			}
//		} catch (Exception e) {
//			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
//		} finally {
//			closeResource(rs);
//			closeResource(pSt);
//			closeResource(cnx);
//		}
//	}	
//	
//	public void updateExternalUserProfileToken(ExternalProfile user) throws CoreServicesDAOException {
//		Connection cnx = null;
//		PreparedStatement pSt = null;
//		try {
//			String sql = "update external_profile set access_token = ? where id = ?";
//			cnx = ds.getConnection();
//			pSt = cnx.prepareStatement(sql);
//			pSt.setString(1, user.getAccesstoken());
//			pSt.setLong(2,  user.getId());
//			int result = pSt.executeUpdate();
//			if (result <= 0) {
//				throw new CoreServicesDAOException(DAOErrorMessages.NO_RECORDS_UPDATED_ERROR);
//			}
//		} catch (Exception e) {
//			throw new CoreServicesDAOException(DAOErrorMessages.GENERAL_ERROR+"|"+e);
//		} finally {
//			closeResource(pSt);
//			closeResource(cnx);
//		}
//	}	

	private void closeResource(Object resource) {
		try {
			if (resource != null) {
				if (resource instanceof Connection) {
					((Connection) resource).close();
				} else if (resource instanceof Statement) {
					((Statement) resource).close();
				} else if (resource instanceof ResultSet){
					((ResultSet) resource).close();
				} 
			}
		} catch (Exception e) {
			//Ignore
		}
	}

}
