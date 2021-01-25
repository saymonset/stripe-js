package com.us.weavx.core.model;

import java.util.HashMap;

public class ProfileData {
	
	public static final String EMAIL = "email";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String BIRTHDAY = "birthday";
	public static final String AGE_RANGE = "age_range";
	public static final String GENDER = "gender";
	public static final String PICTURE = "picture";
	public static final String ID = "id";
	
	
	private HashMap<String, Object> data;

	public ProfileData() {
		data = new HashMap<String, Object>();
	}
	
	public Object getProperty(String propertyName) {
		return data.get(propertyName);
	}
	
	public void putProperty(String propertyName, Object value) {
		data.put(propertyName, value);
	}

	@Override
	public String toString() {
		return "ProfileData [data=" + data + "]";
	}

}
