package com.us.weavx.core.util;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGeneratorUtil {
	
	public static final String generateToken() {
		String accessToken = RandomStringUtils.random(128,true,true);
		return accessToken;
	}

}
