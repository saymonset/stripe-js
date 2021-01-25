package com.us.weavx.core.util;

import org.apache.commons.lang3.RandomStringUtils;

public class OTPGeneratorUtil {
	
	public static final String generateOTP() {
		return RandomStringUtils.random(6,false,true);
	}

}
