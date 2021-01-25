package com.us.weavx.core.util;

import org.apache.commons.lang3.RandomStringUtils;

public class CoreServicesSecurity {
	
	public static String encryptPassword(String plainPassword) {
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(plainPassword);
	}
	
	public static String generateRandomPassword() {
		return RandomStringUtils.random(8,true,true);
	}

}
