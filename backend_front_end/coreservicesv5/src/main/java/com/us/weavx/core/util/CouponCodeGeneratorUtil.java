package com.us.weavx.core.util;

import org.apache.commons.lang3.RandomStringUtils;

public class CouponCodeGeneratorUtil {
	
	public static final String generateCouponCode() {
		return RandomStringUtils.random(10,true,true).toUpperCase();
	}

}
