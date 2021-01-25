package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ApplierRequiredForNotGenericPromotionException;
import com.us.weavx.core.exception.CouponPromotionNoEnabledException;
import com.us.weavx.core.exception.ExpiredCouponPromotionException;
import com.us.weavx.core.exception.NotCouponPromotionOwnerException;
import com.us.weavx.core.exception.PromotionMaxCouponsExceededException;
import com.us.weavx.core.exception.UnknownCouponPromotionException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Coupon;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.CouponTxServices;
import com.us.weavx.core.util.CouponReturnMessagesManager;
@Component
public class GenerateCouponMethodImpl implements ServiceMethod {

	@Autowired
	private CouponTxServices couponServices;
	
	public Response executeMethod(Request request) {
		Response res = new Response();
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long couponPromotionId = ((Number) params.get("couponPromotionId")).longValue();
			boolean activateCoupon = (Boolean) params.get("activateCoupon");
			String applier = (String) params.get("applier");
			String userAgent = (String) params.get("userAgent");
			String ipAddress = (String) params.get("ipAddress");
			Coupon c = couponServices.generateCoupon(couponPromotionId, applier, userAgent, ipAddress, activateCoupon, customerId);
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("coupon", c);
			res.setResult(result);
		} catch (RuntimeException e) {
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+":"+e);
		} catch (UnknownCouponPromotionException e) {
			res.setReturnCode(CouponReturnMessagesManager.UNKNOWN_COUPON_PROMOTION);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.UNKNOWN_COUPON_PROMOTION));
		} catch (PromotionMaxCouponsExceededException e) {
			res.setReturnCode(CouponReturnMessagesManager.COUPON_PROMOTION_MAX_COUPONS_EXCEEDED);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.COUPON_PROMOTION_MAX_COUPONS_EXCEEDED));
		} catch (ExpiredCouponPromotionException e) {
			res.setReturnCode(CouponReturnMessagesManager.EXPIRED_COUPON_PROMOTION);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.EXPIRED_COUPON_PROMOTION));
		} catch (CouponPromotionNoEnabledException e) {
			res.setReturnCode(CouponReturnMessagesManager.COUPON_PROMOTION_NOT_ENABLED);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.COUPON_PROMOTION_NOT_ENABLED));			
		} catch (ApplierRequiredForNotGenericPromotionException e) {
			res.setReturnCode(CouponReturnMessagesManager.COUPON_APPLIER_REQUIRED);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.COUPON_APPLIER_REQUIRED));
		} catch (NotCouponPromotionOwnerException e) {
			res.setReturnCode(CouponReturnMessagesManager.NOT_COUPON_PROMOTION_OWNER);
			res.setReturnMessage(CouponReturnMessagesManager.findReturnMessageByCode(CouponReturnMessagesManager.NOT_COUPON_PROMOTION_OWNER));			
		} 
		return res;
	}

	
	

}
