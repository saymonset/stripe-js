package com.us.weavx.core.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.AlreadyAppliedCouponException;
import com.us.weavx.core.exception.CouponApplierRequiredException;
import com.us.weavx.core.exception.CouponNotValidForCustomerApplication;
import com.us.weavx.core.exception.CouponPromotionNoEnabledException;
import com.us.weavx.core.exception.CouponPromotionNotStartedException;
import com.us.weavx.core.exception.ExpiredCouponPromotionException;
import com.us.weavx.core.exception.InvalidCouponApplierException;
import com.us.weavx.core.exception.InvalidCouponCodeException;
import com.us.weavx.core.exception.UnknownCouponPromotionException;
import com.us.weavx.core.exception.UserCouponApplicationsLimitExceededException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.CouponTxServices;
import com.us.weavx.core.util.CouponReturnMessagesManager;
@Component
public class ValidateCouponMethodImpl implements ServiceMethod {
	@Autowired
	private CouponTxServices couponServices;

	public Response executeMethod(Request request) {
		Response res = new Response();
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			String couponCode = (String) params.get("couponCode");
			double purchaseAmount = ((Number) params.get("purchaseAmount")).doubleValue();
			String userEmail = (String) params.get("applier");
			String userAgent = (String) params.get("userAgent");
			String ipAddress = (String) params.get("ipAddress");
			double discount = couponServices.validateCoupon(couponCode, purchaseAmount, userEmail, customerId, applicationId, userAgent, ipAddress);
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("cuponDiscount", discount);
			res.setReturnDate(LocalDateTime.now().toString());
			res.setResult(result);
		} catch (RuntimeException e) {
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
		} catch (InvalidCouponCodeException e) {
			res.setReturnCode(CouponReturnMessagesManager.INVALID_COUPON_CODE);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.INVALID_COUPON_CODE));
		} catch (AlreadyAppliedCouponException e) {
			res.setReturnCode(CouponReturnMessagesManager.ALREADY_APPLIED_COUPON);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.ALREADY_APPLIED_COUPON));
		} catch (UnknownCouponPromotionException e) {
			res.setReturnCode(CouponReturnMessagesManager.UNKNOWN_COUPON_PROMOTION);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.UNKNOWN_COUPON_PROMOTION));
		} catch (CouponPromotionNoEnabledException e) {
			res.setReturnCode(CouponReturnMessagesManager.COUPON_PROMOTION_NOT_ENABLED);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.COUPON_PROMOTION_NOT_ENABLED));
		} catch (ExpiredCouponPromotionException e) {
			res.setReturnCode(CouponReturnMessagesManager.EXPIRED_COUPON_PROMOTION);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.EXPIRED_COUPON_PROMOTION));
		} catch (CouponPromotionNotStartedException e) {
			res.setReturnCode(CouponReturnMessagesManager.COUPON_PROMOTION_NOT_STARTED);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.COUPON_PROMOTION_NOT_STARTED));
		} catch (CouponApplierRequiredException e) {
			res.setReturnCode(CouponReturnMessagesManager.COUPON_APPLIER_REQUIRED);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.COUPON_APPLIER_REQUIRED));
		} catch (InvalidCouponApplierException e) {
			res.setReturnCode(CouponReturnMessagesManager.INVALID_COUPON_APPLIER);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.INVALID_COUPON_APPLIER));
		} catch (CouponNotValidForCustomerApplication e) {
			res.setReturnCode(CouponReturnMessagesManager.COUPON_NOT_VALID_FOR_CUSTOMER_APPLICATION);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.COUPON_NOT_VALID_FOR_CUSTOMER_APPLICATION));
		} catch (UserCouponApplicationsLimitExceededException e) {
			res.setReturnCode(CouponReturnMessagesManager.COUPON_USER_APPLICATIONS_LIMIT_EXCEEDED);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.COUPON_USER_APPLICATIONS_LIMIT_EXCEEDED));	
		}
		return res;
	}

	
	

}
