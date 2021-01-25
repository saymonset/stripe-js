package com.us.weavx.core.cli.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.us.weavx.core.constants.DAOErrorMessages;
import com.us.weavx.core.exception.CoreServicesDAOException;
import com.us.weavx.core.model.AccessKey;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.Developer;
import com.us.weavx.core.model.DeveloperKey;

public class CLICoreServicesDAO {
	
	public CLICoreServicesDAO() throws CoreServicesDAOException {
	
	}
	
	public boolean existsAccessKey(String key) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select key from access_keys where key = ?";
			cnx = this.getConnection();
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
	}
	
	public void registerNewAccessKey(AccessKey key) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		try {
			String sql = "insert into access_keys(key,secret,customer_id,application_id,is_admin) values (?,?,?,?,?)";
			cnx = this.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setString(1, key.getKey());
			pSt.setString(2, key.getSecret());
			pSt.setLong(3, key.getCustomerId());
			pSt.setLong(4, key.getApplicationId());
			pSt.setBoolean(5, key.isAdmin());
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
	}
	
	public long registerNewApplication(Application app) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "insert into application(name, description, short_token_duration, long_token_duration, created_by) values (?,?,?,?,?)";
			cnx = this.getConnection();
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
	}
	
	public void associateApplication(long appId, long developerId) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		try {
			String sql = "insert into developer_app(app_id, developer_id) values (?,?)";
			cnx = this.getConnection();
			pSt = cnx.prepareStatement(sql);
			pSt.setLong(1, appId);
			pSt.setLong(2, developerId);
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
	}
	
	public long registerNewDeveloper(Developer developer) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "insert into developer(name, email, password) values (?,?,?)";
			cnx = this.getConnection();
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
			closeResource(rs);
			closeResource(pSt);
			closeResource(cnx);
		}
	}
	
	public boolean existsDeveloperKey(String key) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		ResultSet rs = null;
		try {
			String sql = "select dev_key from developer_key where dev_key = ?";
			cnx = this.getConnection();
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
	}
	
	public void registerNewDeveloperKey(DeveloperKey key) throws CoreServicesDAOException {
		Connection cnx = null;
		PreparedStatement pSt = null;
		try {
			String sql = "insert into developer_key(dev_key, dev_secret, productionEnabled, developer_id) values (?,?,?,?)";
			cnx = this.getConnection();
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
	}
	
	private Connection getConnection() throws Exception {
		try {
			Class.forName("org.postgresql.Driver");
			Connection cnx = DriverManager.getConnection("jdbc:postgresql://34.225.112.26:5432/services_db_v2","services","d3v3l0p3r");
			//Connection cnx = DriverManager.getConnection("jdbc:postgresql://54.85.208.139:5432/services_db_v2","services","d3v3l0p3r");
			return cnx;
		} catch (Exception e) {
			throw e;
		}
	}
	
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
