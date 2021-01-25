package com.us.weavx.core.services.tx;

import com.us.weavx.core.data.CouponServicesTxDAO;
import com.us.weavx.core.exception.AlreadyAppliedCouponException;
import com.us.weavx.core.exception.ApplierRequiredForNotGenericPromotionException;
import com.us.weavx.core.exception.CouponApplierRequiredException;
import com.us.weavx.core.exception.CouponNotValidForCustomerApplication;
import com.us.weavx.core.exception.CouponPromotionNoEnabledException;
import com.us.weavx.core.exception.CouponPromotionNotStartedException;
import com.us.weavx.core.exception.ExpiredCouponPromotionException;
import com.us.weavx.core.exception.InvalidCouponApplierException;
import com.us.weavx.core.exception.InvalidCouponCodeException;
import com.us.weavx.core.exception.NotCouponPromotionOwnerException;
import com.us.weavx.core.exception.PromotionMaxCouponsExceededException;
import com.us.weavx.core.exception.UnknownCouponPromotionException;
import com.us.weavx.core.exception.UserCouponApplicationsLimitExceededException;
import com.us.weavx.core.model.Coupon;
import com.us.weavx.core.model.CouponLog;
import com.us.weavx.core.model.CouponPromotion;
import com.us.weavx.core.model.CouponPromotionCustomerAppRestriction;
import com.us.weavx.core.model.TransactionCouponApplication;
import com.us.weavx.core.util.CouponCodeGeneratorUtil;
import com.us.weavx.core.util.CouponEventSelector;
import com.us.weavx.core.util.CouponStatusSelector;
import com.us.weavx.core.util.DiscountTypeSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service("couponTxServices")
public class CouponTxServices {

    @Autowired
    private CouponServicesTxDAO dao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Coupon generateCoupon(long couponPromotionId, String applier, String userAgent, String ipAddress, boolean activateCoupon, long customerId) throws UnknownCouponPromotionException, PromotionMaxCouponsExceededException, ExpiredCouponPromotionException, CouponPromotionNoEnabledException, ApplierRequiredForNotGenericPromotionException, NotCouponPromotionOwnerException {
        CouponPromotion promotion = dao.findCouponPromotionById(couponPromotionId);
        //Se valida que la promoci�n exista
        if (promotion == null) {
            throw new UnknownCouponPromotionException();
        }
        //Se verifica que la promocion sea del cliente
        if (promotion.getCreatedByCustomerId() != customerId) {
            throw new NotCouponPromotionOwnerException();
        }
        //Se verifica que la promocion este activa
        if (!promotion.isEnabled()) {
            throw new CouponPromotionNoEnabledException();
        }
        //Se verifica que la promoci�n est� vigente
        if (promotion.getValidTo().before(new Timestamp(System.currentTimeMillis()))) {
            throw new ExpiredCouponPromotionException();
        }
        //Se verifica si la cantidad de cupones de la promoci�n se ha excedido
        if (promotion.getCurrentCoupons() >= promotion.getMaxCoupons()) {
            throw new PromotionMaxCouponsExceededException();
        }
        //Si no es una promocion generica debe especificarse un aplicador.
        if (!promotion.isGeneric() && applier == null) {
            throw new ApplierRequiredForNotGenericPromotionException();
        }
        //Se procede a generar el nuevo cupon
        String newCouponCode = CouponCodeGeneratorUtil.generateCouponCode();
        Coupon newCoupon = new Coupon();
        newCoupon.setCode(newCouponCode);
        newCoupon.setCouponPromotionid(couponPromotionId);
        newCoupon.setApplier(applier);
        newCoupon.setCouponStatusId(CouponStatusSelector.GENERATED);
        newCoupon = dao.addCoupon(newCoupon);
        //Se incrementa la cantidad de current cupons
        promotion.setCurrentCoupons(promotion.getCurrentCoupons() + 1);
        dao.updateCouponPromotion(promotion);
        //Se procede a registrar en el Log la generaci�n del nuevo cupon.
        registerNewCouponEvent(newCoupon, CouponEventSelector.GENERATION, applier, userAgent, ipAddress);
        if (activateCoupon) {
            newCoupon.setCouponStatusId(CouponStatusSelector.ACTIVE);
            dao.updateCoupon(newCoupon);
            //Se procede a registrar en el log la activaci�n del nuevo cupon
            registerNewCouponEvent(newCoupon, CouponEventSelector.ACTIVATION, applier, userAgent, ipAddress);
        }
        return newCoupon;
    }

    private CouponLog addCouponLog(CouponLog item) {
        return dao.addCouponLog(item);
    }

    private CouponLog registerNewCouponEvent(Coupon coupon, int couponEventId, String userEmail, String userAgent, String ipAddress) {
        CouponLog log = new CouponLog();
        log.setCouponId(coupon.getId());
        log.setCouponEventId(couponEventId);
        log.setUserEmail(userEmail);
        log.setUserAgent(userAgent);
        log.setIpAddress(ipAddress);
        log.setCouponStatusId(coupon.getCouponStatusId());
        return this.addCouponLog(log);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public double validateCoupon(String couponCode, double purchaseAmount, String applier, long customerId, long applicationId, String userAgent, String ipAddress) throws InvalidCouponCodeException, AlreadyAppliedCouponException, UnknownCouponPromotionException, CouponPromotionNoEnabledException, ExpiredCouponPromotionException, CouponPromotionNotStartedException, CouponApplierRequiredException, InvalidCouponApplierException, CouponNotValidForCustomerApplication, UserCouponApplicationsLimitExceededException {
        //Buscar cupon dado el c�digo.
        Coupon c = dao.findCouponByCode(couponCode);
        if (c == null) {
            throw new InvalidCouponCodeException();
        }
        //Se registra el intento de validacion del cupon
        registerNewCouponEvent(c, CouponEventSelector.VALIDATION, applier, userAgent, ipAddress);
        //Se valida que el coupon no se encuentre aplicado
        if (c.getCouponStatusId() == CouponStatusSelector.APPLIED) {
            throw new AlreadyAppliedCouponException();
        }
        //Se busca la informaci�n de la promoci�n asociada al cupon
        CouponPromotion promotion = dao.findCouponPromotionById(c.getCouponPromotionid());
        if (promotion == null) {
            throw new UnknownCouponPromotionException();
        }
        //Se verifica que la promocion este activa
        if (!promotion.isEnabled()) {
            throw new CouponPromotionNoEnabledException();
        }
        //Se verifica que la promocion haya iniciado
        if (promotion.getValidFrom().after(new Timestamp(System.currentTimeMillis()))) {
            throw new CouponPromotionNotStartedException();
        }
        //Se verifica que la promoci�n no este vencida
        if (promotion.getValidTo().before(new Timestamp(System.currentTimeMillis()))) {
            throw new ExpiredCouponPromotionException();
        }
        //Si la promoci�n no es generica debe haberse indicado un aplicador (applier)
        if (!promotion.isGeneric() && applier == null) {
            throw new CouponApplierRequiredException();
        }
        //Se verifica en caso de haber un aplicador configurado para el cupon que coincida con el aplicador de la validacion
        if (c.getApplier() != null && !c.getApplier().equals(applier)) {
            throw new InvalidCouponApplierException();
        }
        //Se obtiene los customer_id para los que aplica la promoci�n
        List<CouponPromotionCustomerAppRestriction> coupPromCustAppRestList = dao.findCouponPromotionCustomerAppRestrictionByCouponPromotionId(c.getCouponPromotionid());
        CouponPromotionCustomerAppRestriction currentCustApp = new CouponPromotionCustomerAppRestriction();
        currentCustApp.setCouponPromotionId(c.getCouponPromotionid());
        currentCustApp.setCustomerId(customerId);
        currentCustApp.setApplicationId(applicationId);
        if (!coupPromCustAppRestList.contains(currentCustApp)) {
            throw new CouponNotValidForCustomerApplication();
        }
        //Se verifica que el limite de aplicacion por usuario no se haya excedido
        Long userCouponApplications = dao.findCouponApplicationsByUserEmail(c.getId(), applier);
        userCouponApplications = (userCouponApplications == null) ? 0 : userCouponApplications;
        if (userCouponApplications == promotion.getUserApplicationLimit()) {
            throw new UserCouponApplicationsLimitExceededException();
        }
        //Se procede al calculo del descuento
        BigDecimal couponDiscount = new BigDecimal(0);
        switch (promotion.getDiscountTypeId()) {
            case DiscountTypeSelector.PERCENT_DISCOUNT:
                couponDiscount = new BigDecimal(purchaseAmount).multiply(new BigDecimal(promotion.getDiscountAmount()).divide(new BigDecimal(100)));
                break;
            default:
                couponDiscount = (promotion.getDiscountAmount() > purchaseAmount) ? new BigDecimal(purchaseAmount) : new BigDecimal(promotion.getDiscountAmount());
        }
        return couponDiscount.doubleValue();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void applyCoupon(String couponCode, double purchaseAmount, String applier, long customerId, long applicationId, long transactionId, String userAgent, String ipAddress) throws InvalidCouponCodeException, AlreadyAppliedCouponException, UnknownCouponPromotionException, CouponPromotionNoEnabledException, ExpiredCouponPromotionException, CouponPromotionNotStartedException, CouponApplierRequiredException, InvalidCouponApplierException, CouponNotValidForCustomerApplication, UserCouponApplicationsLimitExceededException {
        Coupon c = dao.findCouponByCode(couponCode);
        if (c == null) {
            throw new InvalidCouponCodeException();
        }
        validateCoupon(couponCode, purchaseAmount, applier, customerId, applicationId, userAgent, ipAddress);
        //Cupon valido
        c.setAppliedTimes(c.getAppliedTimes() + 1);
        CouponPromotion promotion = dao.findCouponPromotionById(c.getCouponPromotionid());
        //Si el maximo de aplicaciones del cupon se ha alcanzado debe cambiarse el estatus a APPLIED
        if (c.getAppliedTimes() == promotion.getApplicationLimit()) {
            c.setCouponStatusId(CouponStatusSelector.APPLIED);
        }
        //Se registra la aplicacion del cupon para la transaccion
        TransactionCouponApplication txCoupApp = new TransactionCouponApplication();
        txCoupApp.setCouponId(c.getId());
        txCoupApp.setApplier(applier);
        txCoupApp.setTransactionId(transactionId);
        dao.addTransactionCoupon(txCoupApp);
        dao.updateCoupon(c);
        //Se registra la aplicaci�n del cupon
        registerNewCouponEvent(c, CouponEventSelector.APPLICATION, applier, userAgent, ipAddress);
    }

}
