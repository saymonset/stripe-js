package com.us.weavx.core.util;

import org.springframework.stereotype.Component;

@Component
public class CouponReturnMessagesManager {

	public static final int SUCCESS = 0;
	public static final int INVALID_COUPON_CODE = 1;
	public static final int ALREADY_APPLIED_COUPON = 2;
	public static final int UNKNOWN_COUPON_PROMOTION = 3;
	public static final int COUPON_PROMOTION_NOT_ENABLED = 4;
	public static final int EXPIRED_COUPON_PROMOTION = 5;
	public static final int COUPON_PROMOTION_NOT_STARTED = 6;
	public static final int COUPON_APPLIER_REQUIRED = 7;
	public static final int INVALID_COUPON_APPLIER = 8;
	public static final int COUPON_NOT_VALID_FOR_CUSTOMER_APPLICATION = 9;
	public static final int COUPON_USER_APPLICATIONS_LIMIT_EXCEEDED = 10;
	public static final int COUPON_PROMOTION_MAX_COUPONS_EXCEEDED = 11;
	public static final int NOT_COUPON_PROMOTION_OWNER = 12;
	
	public static final String [] returnMessages = {
			"Success",
			"Invalid coupon code",
			"Already applied coupon",
			"Unknown coupon promotion",
			"Coupon promotion not enabled",
			"Expired coupon promotion",
			"Coupon promotion not started",
			"Coupon applier required",
			"Invalid coupon applier",
			"Coupon not valid for customer application",
			"Coupon user applications has been exceeded",
			"Coupon promotion max coupons exceeded",
			"Not coupon promotion owner"
	};

	public static String findReturnMessageByCode(int code) {
		return returnMessages[code];
	}
}
