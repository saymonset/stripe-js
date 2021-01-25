package com.us.weavx.core.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.us.weavx.core.constants.DAOErrorMessages;
import com.us.weavx.core.exception.CoreServicesDAOException;

public class Application {
	
	private long id;
	private String name;
	private String description;
	private long shortTokenDuration;
	private long longTokenDuration;
	private long shortUserTokenDuration;
	private long longUserTokenDuration;
	private long developerId;
	private String applicationControllerClass;
	
	
	public Application(long id, String name, String description, long shortTokenDuration, long longTokenDuration,
			long shortUserTokenDuration, long longUserTokenDuration, long developerId,
			String applicationControllerClass) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.shortTokenDuration = shortTokenDuration;
		this.longTokenDuration = longTokenDuration;
		this.shortUserTokenDuration = shortUserTokenDuration;
		this.longUserTokenDuration = longUserTokenDuration;
		this.developerId = developerId;
		this.applicationControllerClass = applicationControllerClass;
	}
	
	
	
	public Application(String name, String description, long shortTokenDuration, long longTokenDuration,
			long shortUserTokenDuration, long longUserTokenDuration, long developerId,
			String applicationControllerClass) {
		super();
		this.name = name;
		this.description = description;
		this.shortTokenDuration = shortTokenDuration;
		this.longTokenDuration = longTokenDuration;
		this.shortUserTokenDuration = shortUserTokenDuration;
		this.longUserTokenDuration = longUserTokenDuration;
		this.developerId = developerId;
		this.applicationControllerClass = applicationControllerClass;
	}



	public Application() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getShortTokenDuration() {
		return shortTokenDuration;
	}
	public void setShortTokenDuration(long shortTokenDuration) {
		this.shortTokenDuration = shortTokenDuration;
	}
	public long getLongTokenDuration() {
		return longTokenDuration;
	}
	public void setLongTokenDuration(long longTokenDuration) {
		this.longTokenDuration = longTokenDuration;
	}
	public long getShortUserTokenDuration() {
		return shortUserTokenDuration;
	}
	public void setShortUserTokenDuration(long shortUserTokenDuration) {
		this.shortUserTokenDuration = shortUserTokenDuration;
	}
	public long getLongUserTokenDuration() {
		return longUserTokenDuration;
	}
	public void setLongUserTokenDuration(long longUserTokenDuration) {
		this.longUserTokenDuration = longUserTokenDuration;
	}
	public long getDeveloperId() {
		return developerId;
	}
	public void setDeveloperId(long developerId) {
		this.developerId = developerId;
	}
	public String getApplicationControllerClass() {
		return applicationControllerClass;
	}
	public void setApplicationControllerClass(String applicationControllerClass) {
		this.applicationControllerClass = applicationControllerClass;
	}



	@Override
	public String toString() {
		return "Application [id=" + id + ", name=" + name + ", description=" + description + ", shortTokenDuration="
				+ shortTokenDuration + ", longTokenDuration=" + longTokenDuration + ", shortUserTokenDuration="
				+ shortUserTokenDuration + ", longUserTokenDuration=" + longUserTokenDuration + ", developerId="
				+ developerId + ", applicationControllerClass=" + applicationControllerClass + "]";
	}

	


	
	
	

}
