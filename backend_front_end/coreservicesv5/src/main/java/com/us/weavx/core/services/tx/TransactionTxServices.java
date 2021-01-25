package com.us.weavx.core.services.tx;

import com.us.weavx.core.application.ApplicationController;
import com.us.weavx.core.constants.SystemConstants;
import com.us.weavx.core.constants.TokenStatus;
import com.us.weavx.core.data.CoreServicesTxDAO;
import com.us.weavx.core.exception.AlreadyAppliedCouponException;
import com.us.weavx.core.exception.CouponApplierRequiredException;
import com.us.weavx.core.exception.CouponNotValidForCustomerApplication;
import com.us.weavx.core.exception.CouponPromotionNoEnabledException;
import com.us.weavx.core.exception.CouponPromotionNotStartedException;
import com.us.weavx.core.exception.DisabledPaymentTypeException;
import com.us.weavx.core.exception.DonationGeneralException;
import com.us.weavx.core.exception.ExpiredCouponPromotionException;
import com.us.weavx.core.exception.InvalidCouponApplierException;
import com.us.weavx.core.exception.InvalidCouponCodeException;
import com.us.weavx.core.exception.InvalidPaymentTypeException;
import com.us.weavx.core.exception.NoPaymentProfileForPaymentGatewayException;
import com.us.weavx.core.exception.PaymentGatewayException;
import com.us.weavx.core.exception.PaymentGwHandlerException;
import com.us.weavx.core.exception.UnknownCouponPromotionException;
import com.us.weavx.core.exception.UserCouponApplicationsLimitExceededException;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.ApplicationControllerParameterSelector;
import com.us.weavx.core.model.ApplicationControllerResult;
import com.us.weavx.core.model.Assistant;
import com.us.weavx.core.model.BlackListItem;
import com.us.weavx.core.model.BulkTransactionItem;
import com.us.weavx.core.model.BulkTransactionVerificationResultItem;
import com.us.weavx.core.model.Country;
import com.us.weavx.core.model.CreditCardPaymentData;
import com.us.weavx.core.model.CustomerPaymentGateway;
import com.us.weavx.core.model.CustomerProperty;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserCreditCard;
import com.us.weavx.core.model.CustomerUserPaymentProfile;
import com.us.weavx.core.model.CustomerUserPurchaseInfo;
import com.us.weavx.core.model.EmailAgent;
import com.us.weavx.core.model.EmailAgentHist;
import com.us.weavx.core.model.EventCommissionSettings;
import com.us.weavx.core.model.ExternalPaymentData;
import com.us.weavx.core.model.ExternalPaymentType;
import com.us.weavx.core.model.Fund;
import com.us.weavx.core.model.PaymentData;
import com.us.weavx.core.model.PaymentGateway;
import com.us.weavx.core.model.PaymentGatewayInfo;
import com.us.weavx.core.model.PaymentResult;
import com.us.weavx.core.model.PaymentType;
import com.us.weavx.core.model.ScheduledDonation;
import com.us.weavx.core.model.ScheduledDonationFundDetail;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.TransactionDetail;
import com.us.weavx.core.model.TransactionDetailInfo;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.model.User;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.paymentgw.GWTransactionInfo;
import com.us.weavx.core.paymentgw.PaymentGwHandler;
import com.us.weavx.core.paymentgw.PaymentParameterSelector;
import com.us.weavx.core.util.ApplicationParametersManager;
import com.us.weavx.core.util.BlackListDataTypeSelector;
import com.us.weavx.core.util.CommissionTypeSelector;
import com.us.weavx.core.util.CoreServicesClassLoader;
import com.us.weavx.core.util.GeneralUtilities;
import com.us.weavx.core.util.SystemSettingsManager;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("transactionTxServices")
public class TransactionTxServices {

	@Autowired
    private CoreServicesTxDAO dao;

    @Autowired
    private CouponTxServices couponServices;

    @Autowired
    private UserTxServices userServices;

    @Autowired
    private BlackListTxServices blackListServices;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SystemSettingsManager systemSettingsManager;

    @Autowired
    private ApplicationParametersManager appParamManager;
    
    @Autowired
    private UtilTxServices utilTxServices;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionUserData createTransactionUserData(TransactionUserData transactionUserData) {
        return dao.addTransactionUserData(transactionUserData);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PaymentData createPaymentData(PaymentData paymentData) throws InvalidPaymentTypeException, DisabledPaymentTypeException {
        PaymentType pT = dao.findPaymentType(paymentData.getPaymentTypeId());
        if (pT == null) {
            throw new InvalidPaymentTypeException();
        } else {
            if (!pT.isEnabled()) {
                throw new DisabledPaymentTypeException();
            }
        }
        return dao.addPaymentData(paymentData);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerProperty createCustomerProperty(CustomerProperty customerProperty) {
        CustomerProperty prop = dao.addCustomerProperty(customerProperty);
        return prop;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CreditCardPaymentData createCreditCardPaymentData(CreditCardPaymentData paymentData) {
        return dao.addCreditCardPaymentData(paymentData);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PaymentResult proccessCreditCardTransaction(Transaction transaction, List<TransactionDetail> transactionDetails, PaymentGatewayInfo paymentGwInfo, TransactionUserData userData, String userToken, int langId, String couponSerial, String userAgent, String ipAddress) {
    	return proccessCreditCardTransaction(transaction, transactionDetails, paymentGwInfo, userData, userToken, langId, couponSerial, userAgent, ipAddress, new ArrayList<Assistant>());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PaymentResult proccessCreditCardTransaction(Transaction transaction, List<TransactionDetail> transactionDetails, PaymentGatewayInfo paymentGwInfo, TransactionUserData userData, String userToken, int langId, String couponSerial, String userAgent, String ipAddress, List<Assistant> assistants) {
        //Cargar info de gw de pago
        PaymentGateway pGw = dao.getPaymentGateway(paymentGwInfo.getPaymentGatewayId());
        if (pGw == null) {
            return new PaymentResult(PaymentResult.INVALID_PAYMENT_GATEWAY, "Invalid Payment Gw: " + paymentGwInfo.getPaymentGatewayId(), null);
        }
        //Cargar los parametros del gw de pago de acuerdo al cliente
        CustomerPaymentGateway gw = dao.getCustomerPaymentGatewayByPaymentGatewayId(transaction.getCustomerId(), paymentGwInfo.getPaymentGatewayId());
        if (gw == null) {
            return new PaymentResult(PaymentResult.INVALID_PAYMENT_GATEWAY, "The payment gateway: " + paymentGwInfo.getPaymentGatewayId() + " is not configured for the customer.", null);
        }
        HashMap<String, Object> pGwParameter = (HashMap<String, Object>) paymentGwInfo.getPaymentGwParameters();
        pGwParameter.put(PaymentParameterSelector.AUTH_KEY_1, gw.getAuthKey1());
        pGwParameter.put(PaymentParameterSelector.AUTH_KEY_2, gw.getAuthKey2());
        pGwParameter.put(PaymentParameterSelector.AUTH_KEY_3, gw.getAuthKey3());
        pGwParameter.put(PaymentParameterSelector.AUTH_KEY_4, gw.getAuthKey4());
        pGwParameter.put(PaymentParameterSelector.PAYMENT_MODE, gw.getPaymentModeId());
        pGwParameter.put(PaymentParameterSelector.USER_DATA, userData);
        //Verificacion de cuenta gateway conectada (Stripe Connect)
        if (gw.getConnectedAccountId() != null) {
            pGwParameter.put(PaymentParameterSelector.CONNECTED_ACCOUNT_ID, gw.getConnectedAccountId());
        }
        EventCommissionSettings eventCommissionSettings = dao.findEventCommissionSettings(transaction.getCustomerId(), transaction.getApplicationId());
        //Verificación de la existencia de informacion de comision
        if (eventCommissionSettings != null) {
            //Se calcula la comision
            Double commission = null;
            if (eventCommissionSettings.getCommissionTypeId() == CommissionTypeSelector.ABSOLUTE) {
                commission = eventCommissionSettings.getCommissionValue();
            } else {
                commission = transaction.getAmount() * eventCommissionSettings.getCommissionValue();
                if (commission < eventCommissionSettings.getMinimumCommission()) {
                    commission = eventCommissionSettings.getMinimumCommission();
                } else if (commission > eventCommissionSettings.getMaximumCommission()) {
                    commission = eventCommissionSettings.getMaximumCommission();
                }
            }
            transaction.setCommission(commission);
            pGwParameter.put(PaymentParameterSelector.EVENT_COMMISSION_PAYER, eventCommissionSettings.getCommissionPayerId());
            pGwParameter.put(PaymentParameterSelector.EVENT_COMMISSION_SETTINGS, commission);
        } else {
            transaction.setCommission(0d);
        }
        paymentGwInfo.setPaymentGwParameters(pGwParameter);
        //Verificar si el Gw de pago se encuentra activo
        if (!pGw.isEnabled()) {
            return new PaymentResult(PaymentResult.DISABLED_PAYMENT_GATEWAY, "The payment gateway: " + paymentGwInfo.getPaymentGatewayId() + " is disabled.", null);
        }
        //Cargar driver de pago
        CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
        PaymentGwHandler pGwHandler = null;
        try {
            Class c = loader.loadClass(pGw.getImplementorClass(), CoreServicesClassLoader.PAYMENT_GATEWAY);
            pGwHandler = (PaymentGwHandler) context.getBean(c);
        } catch (Exception e) {
            return new PaymentResult(PaymentResult.INVALID_PAYMENT_GATEWAY, "Missing payment gateway handler.", null);
        }
        //Se valida si se tiene un cupon de descuento para actualizar el monto de la transaccion
        transaction.setDiscount(0);
        if (couponSerial != null) {
            try {
                transaction.setDiscount(couponServices.validateCoupon(couponSerial, transaction.getAmount(), userData.getEmail(), transaction.getCustomerId(), transaction.getApplicationId(), userAgent, ipAddress));
            } catch (InvalidCouponCodeException | AlreadyAppliedCouponException | UnknownCouponPromotionException
                    | CouponPromotionNoEnabledException | ExpiredCouponPromotionException
                    | CouponPromotionNotStartedException | CouponApplierRequiredException
                    | InvalidCouponApplierException | CouponNotValidForCustomerApplication
                    | UserCouponApplicationsLimitExceededException e) {
                return new PaymentResult(PaymentResult.COUPON_NOT_VALID, "Error validating coupon: " + e, null);
            }

        }
        //Register User Data
        //Obtener el nombre del pais
        if (userData.getCountry() != null) {
            Country country = dao.getCountry(userData.getCountry());
            if (country != null) {
                userData.setCountryText(country.getName());
            }
        }
        if (userData.getCountryText() == null || userData.getCountryText().trim().equals("")) {
            //Hay que obtener el country desde el usuario
            if (userData.getCustomerUserId() != null) {
                CustomerUser cu = dao.findCustomerUserById(userData.getCustomerUserId());
                if (cu != null) {
                    Country country = dao.getCountry(cu.getCountryId());
                    if (country != null) {
                        userData.setCountryText(country.getName());
                    }
                }
            }
        }
        userData = dao.addTransactionUserData(userData);
        //Register PaymentData
        PaymentData pD = new PaymentData();
        pD.setPaymentTypeId(PaymentType.CREDIT_CARD);
        pD = dao.addPaymentData(pD);
        //Register creditCardPayData
        List<Object> prePaymentResult;
        try {
            prePaymentResult = pGwHandler.prePayment(paymentGwInfo.getPaymentGwParameters());
        } catch (PaymentGwHandlerException e1) {
            return new PaymentResult(PaymentResult.PREPAYMENT_ERROR, "Error executing prepayment method: " + e1.getMessage(), null);
        }
        CreditCardPaymentData ccPd = (CreditCardPaymentData) prePaymentResult.get(0);
        ccPd.setId(pD.getId());

        ccPd = dao.addCreditCardPaymentData(ccPd);
        //Se anaden a la transaccion los id
        transaction.setPaymentGwId(paymentGwInfo.getPaymentGatewayId());
        transaction.setTransactionPaymentDataId(pD.getId());
        transaction.setTransactionUserDataId(userData.getId());
        transaction.setTransactionId(GeneralUtilities.generateTransactionId());
        transaction.setIpAddress(ipAddress);
		transaction.setUserAgent(userAgent);
        transaction = dao.addTransaction(transaction);
        //Se anaden los detalles de la transaccion
        dao.addTransactionDetails(transactionDetails, transaction.getId());
        //Se procede a validar si se trata de una transaccion fraudulenta
        try {
            Integer rejectedCount = (Integer) paymentGwInfo.getPaymentGwParameters().get("rejected_tx_count");
            if (rejectedCount != null && isFraudulentTransaction(ipAddress, userData.getEmail(), rejectedCount)) {
                PaymentResult p = new PaymentResult();
                p.setResult(PaymentResult.FRAUD_TRANSACTION);
                p.setResultMessage("AntiFraud system: Max rejected transactions attempts exceeded.");
                transaction.setTransactionStatus(Transaction.IN_BLACKLIST);
                dao.updateTransactionStatus(transaction);
                return p;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //Se invoca el handler de la aplicacion para validar lo que el programador considere pertinente
        Application app = dao.findApplicationInfo(transaction.getApplicationId());
        Class cAppController = null;
        ApplicationController appController = null;
        HashMap<String, Object> appControllerParameters = new HashMap<>();
        Map<String, Object> buyInitResultParams = null;
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION, transaction);
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA, userData);
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_DETAILS, transactionDetails);
        appControllerParameters.put(ApplicationControllerParameterSelector.PAYMENT_GW_INFO, paymentGwInfo);
        appControllerParameters.put(ApplicationControllerParameterSelector.USER_ACCESS_TOKEN, userToken);
        appControllerParameters.put(ApplicationControllerParameterSelector.LANG, langId);
        appControllerParameters.put(ApplicationControllerParameterSelector.IP_ADDRESS, ipAddress);
        if (app.getApplicationControllerClass() != null) {
            try {
                cAppController = loader.loadClass(app.getApplicationControllerClass(), CoreServicesClassLoader.APPLICATION_CONTROLLER);
                //appController = (ApplicationController) cAppController.newInstance();
                appController = (ApplicationController) context.getBean(cAppController);
                ApplicationControllerResult resultTmp = appController.onInitBuy(appControllerParameters);
                if (resultTmp != null && resultTmp.getResult() == ApplicationControllerResult.ABORT_TRANSACTION) {
                    PaymentResult p = new PaymentResult();
                    p.setResultMessage(resultTmp.getResultMessage());
                    if (resultTmp.getResultMessage().toLowerCase().indexOf("blacklist") != -1) {
                        transaction.setTransactionStatus(Transaction.IN_BLACKLIST);
                        p.setResult(PaymentResult.FRAUD_TRANSACTION);
                    } else {
                        transaction.setTransactionStatus(Transaction.ABORTED);
                        p.setResult(PaymentResult.ABORTED_TRANSACTION);
                    }
                    if (p.getAuthorizationInfo() != null) {
                        p.getAuthorizationInfo().put("transactionId", transaction.getId());
                    }
                    dao.updateTransactionStatus(transaction);
                    return p;
                }
                buyInitResultParams = resultTmp.getResultParameters();
            } catch (Exception e1) {
                PaymentResult p = new PaymentResult();
                p.setResult(PaymentResult.OTHER);
                p.setResultMessage("ApplicationController <onBuyInit>: " + e1);
                if (p.getAuthorizationInfo() != null) {
                    p.getAuthorizationInfo().put("transactionId", transaction.getId());
                }
                return p;
            }
        }
        //Se procede a ejecutar la transaccion con el gateway de pago.
        transaction.setTransactionStatus(Transaction.PAYMENT_IN_PROGRESS);
        dao.updateTransactionStatus(transaction);
        //Se verifica si se trata de una compra autenticada
        CustomerUserPaymentProfile custPaymentGwProfile = null;
        if (userData.getCustomerUserId() != null) {
            String savePaymentData = appParamManager.getApplicationParameter(transaction.getApplicationId(), "SAVE_PAYMENT_DATA");
            boolean allowSavePaymentData = (savePaymentData == null) ? true : new Boolean(savePaymentData).booleanValue();
            boolean mustSavePaymentData = (pGwParameter.containsKey("savePaymentData") && (Boolean) pGwParameter.get("savePaymentData"));
            //Se verifica si ya el usuario posee un perfil de pago en el gateway
            custPaymentGwProfile = dao.findCustomerPaymentProfile(userData.getCustomerUserId(), paymentGwInfo.getPaymentGatewayId());
            if (allowSavePaymentData && mustSavePaymentData) {
                if (custPaymentGwProfile == null) {
                    //Se crea el perfil en el gateway de pago
                    try {
                        String customerExtGwId = pGwHandler.createCustomerProfile(pGwParameter);
                        custPaymentGwProfile = new CustomerUserPaymentProfile();
                        custPaymentGwProfile.setCustomerId(transaction.getCustomerId());
                        custPaymentGwProfile.setCustomerUserId(userData.getCustomerUserId());
                        custPaymentGwProfile.setPaymentGwId(paymentGwInfo.getPaymentGatewayId());
                        custPaymentGwProfile.setPaymentGwCustomerId(customerExtGwId);
                        custPaymentGwProfile = dao.addCustomerUserPaymentProfile(custPaymentGwProfile);
                        //la primera transaccion luego de registrar el cliente sera ONE_CLIC pues se usara el instumento por default del cliente.
                        paymentGwInfo.getPaymentGwParameters().put(PaymentParameterSelector.ONE_CLIC, "yes");
                    } catch (Exception e) {
                        custPaymentGwProfile = null;
                    }
                }
            }
        }
        if (custPaymentGwProfile != null) {
            paymentGwInfo.getPaymentGwParameters().put(PaymentParameterSelector.CUSTOMER_PROFILE, custPaymentGwProfile.getPaymentGwCustomerId());
        }
        //Mejora por cupones
        BigDecimal amount = new BigDecimal(transaction.getAmount());
        BigDecimal discount = new BigDecimal(transaction.getDiscount());
        double purchaseWithDiscountAmount = amount.subtract(discount).doubleValue();
        PaymentResult p = pGwHandler.makePayment(purchaseWithDiscountAmount, paymentGwInfo.getPaymentGwParameters());
        if (p != null && p.getAuthorizationInfo() != null) {
            p.getAuthorizationInfo().put(SystemConstants.TRANSACTION, transaction.getId());
        }
        switch (p.getResult()) {
            case PaymentResult.APPROVED:
            	//Se registra el usuario en MailChimp
                String mailChimpListId =  appParamManager.getApplicationParameter(transaction.getApplicationId(), "MAILCHIMP_LIST_ID"); 
                if(mailChimpListId != null && mailChimpListId.length() > 2)
                	utilTxServices.registrarUsuaruioEnMailChimp(transaction.getApplicationId() ,userData);
            	//Se debe aplicar el cupon
                if (couponSerial != null && transaction.getDiscount() > 0) {
                    try {
                        couponServices.applyCoupon(couponSerial, transaction.getAmount(), userData.getEmail(), transaction.getCustomerId(), transaction.getApplicationId(), transaction.getId(), userAgent, ipAddress);
                    } catch (Exception e1) {
                        //Se ignora el error pues ya le fue cobrado al cliente en su tdc el resto de la compra.
                    }
                }
                transaction.setTransactionStatus(Transaction.CHARGED);
                break;
            case PaymentResult.DENIED:
                transaction.setTransactionStatus(Transaction.PAYMENT_DENIED);
                break;
            case PaymentResult.NETWORK_ERROR:
                transaction.setTransactionStatus(Transaction.PAYMENT_ERROR);
                break;
            case PaymentResult.NO_RESPONSE:
                transaction.setTransactionStatus(Transaction.PAYMENT_ERROR);
                break;
            default:
                transaction.setTransactionStatus(Transaction.FAILED_TX);
        }
        if (p.getAuthorizationInfo() != null) {
            p.getAuthorizationInfo().put("transactionId", transaction.getId());
            p.getAuthorizationInfo().put("tx_id", transaction.getTransactionId());
        }
        dao.updateTransactionStatus(transaction);
        if (transaction.getTransactionStatus() == Transaction.PAYMENT_DENIED) {
            return p;
        }
        if (transaction.getTransactionStatus() != Transaction.PAYMENT_ERROR && transaction.getTransactionStatus() != Transaction.FAILED_TX) {
            //Se ejcuta el metodo postPayment y de acuerdo al resultado se hacen las actualizaciones en base de datos que tengan lugar
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(PaymentParameterSelector.TRANSACTION, transaction);
            parameters.put(PaymentParameterSelector.CREDIT_CARD_PAYMENT_DATA, ccPd);
            parameters.put(PaymentParameterSelector.USER_DATA, userData);
            parameters.put(PaymentParameterSelector.AUTHORIZATION_INFO, p.getAuthorizationInfo());
            List<Object> postPaymentResult;
            try {
                postPaymentResult = pGwHandler.postPayment(parameters);
            } catch (PaymentGwHandlerException e) {
                p.setResult(PaymentResult.POSTPAYMENT_ERROR);
                p.setResultMessage(p.getResultMessage() + "->Error executing postPayment method: " + e.getMessage());
                return p;
            }
            Iterator<Object> iterPostPaymentResult = postPaymentResult.iterator();
            while (iterPostPaymentResult.hasNext()) {
                Object tmp = iterPostPaymentResult.next();
                if (tmp instanceof Transaction) {
                    dao.updateTransactionGwInfo((Transaction) tmp);
                    continue;
                } else if (tmp instanceof CreditCardPaymentData) {
                    dao.updateCreditCardPaymentData((CreditCardPaymentData) tmp);
                }
            }
            if (app.getApplicationControllerClass() != null && appController != null) {
                try {
                    //Se actualiza el estatus del token a PENDING TO VALIDATE
                    UserAccessToken uAToken = userServices.validateUserAccessToken(userToken);
                    if (uAToken.getStatus() != TokenStatus.PENDING_VALIDATION) {
                        uAToken.setStatus(TokenStatus.PENDING_VALIDATION);
                        userServices.updateUserAccessTokenStatus(uAToken);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    appControllerParameters.put(ApplicationControllerParameterSelector.BUY_INIT_RESULT, buyInitResultParams);
                    appControllerParameters.put(ApplicationControllerParameterSelector.PAYMENT_RESULT, p);
                    appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_ASSISTANTS, assistants);
                    ApplicationControllerResult tmp = appController.onFinishBuy(appControllerParameters);
                    if (tmp != null && tmp.getResult() == ApplicationControllerResult.ERROR) {
                        p.setResult(PaymentResult.NOT_PROVISIONED);
                        p.setResultMessage(tmp.getResultMessage());
                        transaction.setTransactionStatus(Transaction.NOT_PROVISIONED);
                        dao.updateTransactionStatus(transaction);
                        return p;
                    } else {
                        //Se actualiza el estatus de la transaccion a SUCCESS
                        if (transaction.getTransactionStatus() == Transaction.CHARGED) {
                            transaction.setTransactionStatus(Transaction.SUCCESS_TX);
                            dao.updateTransactionStatus(transaction);
                        }
                    }
                } catch (Exception e) {
                    p.setResult(PaymentResult.POSTPAYMENT_ERROR);
                    p.setResultMessage("ApplicationController <onBuyFinish>: " + e);
                    return p;
                }
            } else {
                //Se actualiza el estatus de la transaccion a SUCCESS
                if (transaction.getTransactionStatus() == Transaction.CHARGED) {
                    transaction.setTransactionStatus(Transaction.SUCCESS_TX);
                    dao.updateTransactionStatus(transaction);
                }
            }
        }
        return p;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PaymentResult proccessFreeTransaction(Transaction transaction, List<TransactionDetail> transactionDetails, TransactionUserData userData, String userToken, int langId, String couponSerial, String userAgent, String ipAddress) {
        //Se valida si se tiene un cupon de descuento para actualizar el monto de la transaccion
        transaction.setDiscount(0);
        if (transaction.getAmount() > 0) {
            if (couponSerial != null) {
                try {
                    transaction.setDiscount(couponServices.validateCoupon(couponSerial, transaction.getAmount(), userData.getEmail(), transaction.getCustomerId(), transaction.getApplicationId(), userAgent, ipAddress));
                } catch (InvalidCouponCodeException | AlreadyAppliedCouponException | UnknownCouponPromotionException
                        | CouponPromotionNoEnabledException | ExpiredCouponPromotionException
                        | CouponPromotionNotStartedException | CouponApplierRequiredException
                        | InvalidCouponApplierException | CouponNotValidForCustomerApplication
                        | UserCouponApplicationsLimitExceededException e) {
                    return new PaymentResult(PaymentResult.COUPON_NOT_VALID, "Error validating coupon: " + e, null);
                }
            }
        }
        //Register User Data
        //Obtener el nombre del pais
        if (userData.getCountry() != null) {
            Country country = dao.getCountry(userData.getCountry());
            if (country != null) {
                userData.setCountryText(country.getName());
            }
        }
        if (userData.getCountryText() == null || userData.getCountryText().trim().equals("")) {
            //Hay que obtener el country desde el usuario
            if (userData.getCustomerUserId() != null) {
                CustomerUser cu = dao.findCustomerUserById(userData.getCustomerUserId());
                if (cu != null) {
                    Country country = dao.getCountry(cu.getCountryId());
                    if (country != null) {
                        userData.setCountryText(country.getName());
                    }
                }
            }
        }
        userData = dao.addTransactionUserData(userData);
        //Register PaymentData
        PaymentData pD = new PaymentData();
        pD.setPaymentTypeId(PaymentType.FREE);
        pD = dao.addPaymentData(pD);
        //Se anaden a la transaccion los id
        transaction.setPaymentGwId(null);
        transaction.setTransactionPaymentDataId(pD.getId());
        transaction.setTransactionUserDataId(userData.getId());
        transaction.setTransactionId(GeneralUtilities.generateTransactionId());
        transaction.setIpAddress(ipAddress);
		transaction.setUserAgent(userAgent);
        transaction = dao.addTransaction(transaction);
        //Se anaden los detalles de la transaccion
        dao.addTransactionDetails(transactionDetails, transaction.getId());
        //Se invoca el handler de la aplicacion para validar lo que el programador considere pertinente
        Application app = dao.findApplicationInfo(transaction.getApplicationId());
        Class cAppController = null;
        ApplicationController appController = null;
        HashMap<String, Object> appControllerParameters = new HashMap<>();
        Map<String, Object> buyInitResultParams = null;
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION, transaction);
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA, userData);
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_DETAILS, transactionDetails);
        appControllerParameters.put(ApplicationControllerParameterSelector.USER_ACCESS_TOKEN, userToken);
        appControllerParameters.put(ApplicationControllerParameterSelector.LANG, langId);
        appControllerParameters.put(ApplicationControllerParameterSelector.IP_ADDRESS, ipAddress);
        if (app.getApplicationControllerClass() != null) {
            try {
                CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
                cAppController = loader.loadClass(app.getApplicationControllerClass(), CoreServicesClassLoader.APPLICATION_CONTROLLER);
                //appController = (ApplicationController) cAppController.newInstance();
                appController = (ApplicationController) context.getBean(cAppController);
                ApplicationControllerResult resultTmp = appController.onInitBuy(appControllerParameters);
                if (resultTmp != null && resultTmp.getResult() == ApplicationControllerResult.ABORT_TRANSACTION) {
                    PaymentResult p = new PaymentResult();
                    p.setResult(PaymentResult.ABORTED_TRANSACTION);
                    p.setResultMessage(resultTmp.getResultMessage());
                    transaction.setTransactionStatus(Transaction.ABORTED);
                    if (p.getAuthorizationInfo() != null) {
                        p.getAuthorizationInfo().put("transactionId", transaction.getId());
                    }
                    return p;
                }
                buyInitResultParams = resultTmp.getResultParameters();
            } catch (Exception e1) {
                PaymentResult p = new PaymentResult();
                p.setResult(PaymentResult.OTHER);
                p.setResultMessage("ApplicationController <onBuyInit>: " + e1);
                if (p.getAuthorizationInfo() != null) {
                    p.getAuthorizationInfo().put("transactionId", transaction.getId());
                }
                return p;
            }
        }
        //Se valida que si hay un cupon realmente el monto total de la compra sea 0
        //Mejora por cupones
        BigDecimal amount = new BigDecimal(transaction.getAmount());
        BigDecimal discount = new BigDecimal(transaction.getDiscount());
        double purchaseWithDiscountAmount = amount.subtract(discount).doubleValue();
        if (purchaseWithDiscountAmount > 0) {
            PaymentResult p = new PaymentResult();
            p.setResult(PaymentResult.NOT_FREE_TRANSACTION);
            p.setResultMessage("Need to pay: " + purchaseWithDiscountAmount);
            p.setAuthorizationInfo(null);
            return p;
        }
        PaymentResult p = new PaymentResult();
        p.setResult(PaymentResult.APPROVED);
        p.setResultMessage("Free transaction.");
        HashMap<String, Object> authorizationInfo = new HashMap<>();
        authorizationInfo.put("tx_id", transaction.getTransactionId());
        p.setAuthorizationInfo(authorizationInfo);
        
        //Se registra el usuario en MailChimp
        String mailChimpListId =  appParamManager.getApplicationParameter(transaction.getApplicationId(), "MAILCHIMP_LIST_ID"); 
        if(mailChimpListId != null && mailChimpListId.length() > 2)
        	utilTxServices.registrarUsuaruioEnMailChimp(transaction.getApplicationId() ,userData);
        
        //Se debe aplicar el cupon
        if (transaction.getAmount() > 0 && couponSerial != null) {
            try {
                couponServices.applyCoupon(couponSerial, transaction.getAmount(), userData.getEmail(), transaction.getCustomerId(), transaction.getApplicationId(), transaction.getId(), userAgent, ipAddress);
            } catch (Exception e1) {
                //Se indica error al cliente pues el cupon no pudo ser cobrado solo si la transaccion tiene un monto mayor a 0
                if (transaction.getAmount() > 0) {
                    PaymentResult pR = new PaymentResult();
                    pR.setResult(PaymentResult.COUPON_NOT_VALID);
                    pR.setResultMessage("Coupon can't be applied:" + e1);
                    pR.setAuthorizationInfo(null);
                    return pR;
                }
            }
        }
        if (app.getApplicationControllerClass() != null && appController != null) {
            try {
                //Se actualiza el estatus del token a PENDING TO VALIDATE
                UserAccessToken uAToken = userServices.validateUserAccessToken(userToken);
                if (uAToken.getStatus() != TokenStatus.PENDING_VALIDATION) {
                    uAToken.setStatus(TokenStatus.PENDING_VALIDATION);
                    userServices.updateUserAccessTokenStatus(uAToken);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                appControllerParameters.put(ApplicationControllerParameterSelector.BUY_INIT_RESULT, buyInitResultParams);
                appControllerParameters.put(ApplicationControllerParameterSelector.PAYMENT_RESULT, p);
                ApplicationControllerResult tmp = appController.onFinishBuy(appControllerParameters);
                if (tmp != null && tmp.getResult() == ApplicationControllerResult.ERROR) {
                    p.setResult(PaymentResult.NOT_PROVISIONED);
                    p.setResultMessage(tmp.getResultMessage());
                    transaction.setTransactionStatus(Transaction.NOT_PROVISIONED);
                    dao.updateTransactionStatus(transaction);
                    return p;
                } else {
                    //Se actualiza el estatus de la transaccion a SUCCESS
                    transaction.setTransactionStatus(Transaction.SUCCESS_TX);
                    dao.updateTransactionStatus(transaction);
                }
            } catch (Exception e) {
                p.setResult(PaymentResult.POSTPAYMENT_ERROR);
                p.setResultMessage("ApplicationController <onBuyFinish>: " + e);
                return p;
            }
        } else {
            //Se actualiza el estatus de la transaccion a SUCCESS
            transaction.setTransactionStatus(Transaction.SUCCESS_TX);
            dao.updateTransactionStatus(transaction);
        }
        return p;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PaymentResult proccessExternalTransaction(Transaction transaction, List<TransactionDetail> transactionDetails, TransactionUserData userData, String userToken, int langId, ExternalPaymentData extPaymentData, String couponSerial, String userAgent, String ipAddress) {
        //Se valida si se tiene un cupon de descuento para actualizar el monto de la transaccion
        transaction.setDiscount(0);
        EventCommissionSettings eventCommissionSettings = dao.findEventCommissionSettings(transaction.getCustomerId(), transaction.getApplicationId());
        //Verificación de la existencia de informacion de comision
        if (eventCommissionSettings != null) {
            //Se calcula la comision
            Double commission = null;
            if (transaction.getAmount() == 0) {
                commission = eventCommissionSettings.getFreeCommissionValue();
            } else {
                if (eventCommissionSettings.getCommissionTypeId() == CommissionTypeSelector.ABSOLUTE) {
                    commission = eventCommissionSettings.getCommissionValue();
                } else {
                    commission = transaction.getAmount() * eventCommissionSettings.getCommissionValue();
                    if (commission < eventCommissionSettings.getMinimumCommission()) {
                        commission = eventCommissionSettings.getMinimumCommission();
                    } else if (commission > eventCommissionSettings.getMaximumCommission()) {
                        commission = eventCommissionSettings.getMaximumCommission();
                    }
                }
            }
//			if (eventCommissionSettings.getCommissionPayerId() == CommissionPayerSelector.USER) {
//				//SI la comision es pagada por el usuario debe sumarse al total de la compra.
//				transaction.setAmount(transaction.getAmount()+commission);
//			}
            transaction.setCommission(commission);
        } else {
            transaction.setCommission(0d);
        }
        if (transaction.getAmount() > 0) {
            if (couponSerial != null) {
                try {
                    transaction.setDiscount(couponServices.validateCoupon(couponSerial, transaction.getAmount(), userData.getEmail(), transaction.getCustomerId(), transaction.getApplicationId(), userAgent, ipAddress));
                } catch (InvalidCouponCodeException | AlreadyAppliedCouponException | UnknownCouponPromotionException
                        | CouponPromotionNoEnabledException | ExpiredCouponPromotionException
                        | CouponPromotionNotStartedException | CouponApplierRequiredException
                        | InvalidCouponApplierException | CouponNotValidForCustomerApplication
                        | UserCouponApplicationsLimitExceededException e) {
                    return new PaymentResult(PaymentResult.COUPON_NOT_VALID, "Error validating coupon: " + e, null);
                }
            }
        }
        //Register User Data
        //Obtener el nombre del pais
        if (userData.getCountry() != null) {
            Country country = dao.getCountry(userData.getCountry());
            if (country != null) {
                userData.setCountryText(country.getName());
            }
        }
        if (userData.getCountryText() == null || userData.getCountryText().trim().equals("")) {
            //Hay que obtener el country desde el usuario
            if (userData.getCustomerUserId() != null) {
                CustomerUser cu = dao.findCustomerUserById(userData.getCustomerUserId());
                if (cu != null) {
                    Country country = dao.getCountry(cu.getCountryId());
                    if (country != null) {
                        userData.setCountryText(country.getName());
                    }
                }
            }
        }
        userData = dao.addTransactionUserData(userData);
        //Register PaymentData
        PaymentData pD = new PaymentData();
        pD.setPaymentTypeId(PaymentType.FREE);
        pD = dao.addPaymentData(pD);
        //Se anaden a la transaccion los id
        transaction.setPaymentGwId(null);
        transaction.setTransactionPaymentDataId(pD.getId());
        transaction.setTransactionUserDataId(userData.getId());
        transaction.setTransactionId(GeneralUtilities.generateTransactionId());
        transaction.setIpAddress(ipAddress);
		transaction.setUserAgent(userAgent);
        transaction = dao.addTransaction(transaction);
        //Se anaden los detalles de la transaccion
        dao.addTransactionDetails(transactionDetails, transaction.getId());
        //Se invoca el handler de la aplicacion para validar lo que el programador considere pertinente
        Application app = dao.findApplicationInfo(transaction.getApplicationId());
        Class cAppController = null;
        ApplicationController appController = null;
        HashMap<String, Object> appControllerParameters = new HashMap<>();
        Map<String, Object> buyInitResultParams = null;
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION, transaction);
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA, userData);
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_DETAILS, transactionDetails);
        appControllerParameters.put(ApplicationControllerParameterSelector.USER_ACCESS_TOKEN, userToken);
        appControllerParameters.put(ApplicationControllerParameterSelector.LANG, langId);
        appControllerParameters.put(ApplicationControllerParameterSelector.IP_ADDRESS, ipAddress);
        if (app.getApplicationControllerClass() != null) {
            try {
                CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
                cAppController = loader.loadClass(app.getApplicationControllerClass(), CoreServicesClassLoader.APPLICATION_CONTROLLER);
                //appController = (ApplicationController) cAppController.newInstance();
                appController = (ApplicationController) context.getBean(cAppController);
                ApplicationControllerResult resultTmp = appController.onInitBuy(appControllerParameters);
                if (resultTmp != null && resultTmp.getResult() == ApplicationControllerResult.ABORT_TRANSACTION) {
                    PaymentResult p = new PaymentResult();
                    p.setResult(PaymentResult.ABORTED_TRANSACTION);
                    p.setResultMessage(resultTmp.getResultMessage());
                    transaction.setTransactionStatus(Transaction.ABORTED);
                    if (p.getAuthorizationInfo() != null) {
                        p.getAuthorizationInfo().put("transactionId", transaction.getId());
                    }
                    return p;
                }
                buyInitResultParams = resultTmp.getResultParameters();
            } catch (Exception e1) {
                PaymentResult p = new PaymentResult();
                p.setResult(PaymentResult.OTHER);
                p.setResultMessage("ApplicationController <onBuyInit>: " + e1);
                if (p.getAuthorizationInfo() != null) {
                    p.getAuthorizationInfo().put("transactionId", transaction.getId());
                }
                return p;
            }
        }
        //Se valida que si hay un cupon realmente el monto total de la compra sea 0
        //Mejora por cupones
        BigDecimal amount = new BigDecimal(transaction.getAmount());
        BigDecimal discount = new BigDecimal(transaction.getDiscount());
        double purchaseWithDiscountAmount = amount.subtract(discount).doubleValue();
        PaymentResult p = new PaymentResult();
        p.setResult(PaymentResult.APPROVED);
        p.setResultMessage("Free transaction");
        HashMap<String, Object> authorizationInfo = new HashMap<>();
        authorizationInfo.put("tx_id", transaction.getTransactionId());
        p.setAuthorizationInfo(authorizationInfo);
        ExternalPaymentType extPayType = null;
        if (purchaseWithDiscountAmount > 0) {
            try {
                extPayType = dao.findExternalPaymentType(extPaymentData.getExternalPaymentDataId());
            } catch (Exception e) {
                PaymentResult pR = new PaymentResult();
                pR.setResult(PaymentResult.EXTERNAL_PAYMENT_METHOD_NOT_VALID);
                pR.setResultMessage("External Payment Method not valid:" + extPaymentData);
                pR.setAuthorizationInfo(null);
                return pR;
            }
            p.setResultMessage("External transaction.");
            transaction.setAuthGw1(extPayType.getName() + ":" + extPaymentData.getExternalPaymentDataTxt());
            dao.updateTransactionGwInfo(transaction);
        }
        //Se debe aplicar el cupon
        if (couponSerial != null && transaction.getDiscount() > 0) {
            try {
                couponServices.applyCoupon(couponSerial, transaction.getAmount(), userData.getEmail(), transaction.getCustomerId(), transaction.getApplicationId(), transaction.getId(), userAgent, ipAddress);
            } catch (Exception e1) {
                //Se indica error al cliente pues el cupon no pudo ser cobrado solo si la transaccion tiene un monto mayor a 0
                if (purchaseWithDiscountAmount > 0) {
                    //Se registra que una porcion del pago es con el medio externo sino es una freepurchase.
                    p.setResult(PaymentResult.COUPON_NOT_VALID);
                    p.setResultMessage("Coupon can't be applied:" + couponSerial);
                    p.setAuthorizationInfo(null);
                    return p;
                }

            }
        }
        
        //Se registra el usuario en MailChimp
        String mailChimpListId =  appParamManager.getApplicationParameter(transaction.getApplicationId(), "MAILCHIMP_LIST_ID"); 
        if(mailChimpListId != null && mailChimpListId.length() > 2)
        	utilTxServices.registrarUsuaruioEnMailChimp(transaction.getApplicationId() ,userData);

        if (app.getApplicationControllerClass() != null && appController != null) {
            try {
                //Se actualiza el estatus del token a PENDING TO VALIDATE
                UserAccessToken uAToken = userServices.validateUserAccessToken(userToken);
                if (uAToken.getStatus() != TokenStatus.PENDING_VALIDATION) {
                    uAToken.setStatus(TokenStatus.PENDING_VALIDATION);
                    userServices.updateUserAccessTokenStatus(uAToken);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                appControllerParameters.put(ApplicationControllerParameterSelector.BUY_INIT_RESULT, buyInitResultParams);
                appControllerParameters.put(ApplicationControllerParameterSelector.PAYMENT_RESULT, p);
                ApplicationControllerResult tmp = appController.onFinishBuy(appControllerParameters);
                if (tmp != null && tmp.getResult() == ApplicationControllerResult.ERROR) {
                    p.setResult(PaymentResult.NOT_PROVISIONED);
                    p.setResultMessage(tmp.getResultMessage());
                    transaction.setTransactionStatus(Transaction.NOT_PROVISIONED);
                    dao.updateTransactionStatus(transaction);
                    return p;
                } else {
                    //Se actualiza el estatus de la transaccion a SUCCESS
                    transaction.setTransactionStatus(Transaction.SUCCESS_TX);
                    dao.updateTransactionStatus(transaction);
                }
            } catch (Exception e) {
                p.setResult(PaymentResult.POSTPAYMENT_ERROR);
                p.setResultMessage("ApplicationController <onBuyFinish>: " + e);
                return p;
            }
        } else {
            //Se actualiza el estatus de la transaccion a SUCCESS
            transaction.setTransactionStatus(Transaction.SUCCESS_TX);
            dao.updateTransactionStatus(transaction);
        }
        return p;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public PaymentResult proccessAdminTransaction(Transaction transaction, List<TransactionDetail> transactionDetails, TransactionUserData userData, String userToken, int langId) {

        //Register User Data
        //Obtener el nombre del pais
        if (userData.getCountry() != null) {
            Country country = dao.getCountry(userData.getCountry());
            if (country != null) {
                userData.setCountryText(country.getName());
            }
        }
        userData = dao.addTransactionUserData(userData);
        //Register PaymentData
        PaymentData pD = new PaymentData();
        pD.setPaymentTypeId(PaymentType.FREE);
        pD = dao.addPaymentData(pD);
        //Se anaden a la transaccion los id
        transaction.setPaymentGwId(null);
        transaction.setTransactionPaymentDataId(pD.getId());
        transaction.setTransactionUserDataId(userData.getId());
        transaction.setTransactionId(GeneralUtilities.generateTransactionId());
        transaction = dao.addTransaction(transaction);
        //Se anaden los detalles de la transaccion
        dao.addTransactionDetails(transactionDetails, transaction.getId());
        //Se invoca el handler de la aplicacion para validar lo que el programador considere pertinente
        Application app = dao.findApplicationInfo(transaction.getApplicationId());
        Class cAppController = null;
        ApplicationController appController = null;
        HashMap<String, Object> appControllerParameters = new HashMap<>();
        Map<String, Object> buyInitResultParams = null;
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION, transaction);
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA, userData);
        appControllerParameters.put(ApplicationControllerParameterSelector.TRANSACTION_DETAILS, transactionDetails);
        appControllerParameters.put(ApplicationControllerParameterSelector.USER_ACCESS_TOKEN, userToken);
        appControllerParameters.put(ApplicationControllerParameterSelector.LANG, langId);
        if (app.getApplicationControllerClass() != null) {
            try {
                CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
                cAppController = loader.loadClass(app.getApplicationControllerClass(), CoreServicesClassLoader.APPLICATION_CONTROLLER);
                //appController = (ApplicationController) cAppController.newInstance();
                appController = (ApplicationController) context.getBean(cAppController);
                ApplicationControllerResult resultTmp = appController.onInitBuy(appControllerParameters);
                if (resultTmp != null && resultTmp.getResult() == ApplicationControllerResult.ABORT_TRANSACTION) {
                    PaymentResult p = new PaymentResult();
                    p.setResult(PaymentResult.ABORTED_TRANSACTION);
                    p.setResultMessage(resultTmp.getResultMessage());
                    transaction.setTransactionStatus(Transaction.ABORTED);
                    if (p.getAuthorizationInfo() != null) {
                        p.getAuthorizationInfo().put("transactionId", transaction.getId());
                    }
                    return p;
                }
                buyInitResultParams = resultTmp.getResultParameters();
            } catch (Exception e1) {
                PaymentResult p = new PaymentResult();
                p.setResult(PaymentResult.OTHER);
                p.setResultMessage("ApplicationController <onBuyInit>: " + e1);
                if (p.getAuthorizationInfo() != null) {
                    p.getAuthorizationInfo().put("transactionId", transaction.getId());
                }
                return p;
            }
        }
        
        String mailChimpListId =  appParamManager.getApplicationParameter(transaction.getApplicationId(), "MAILCHIMP_LIST_ID"); 
        if(mailChimpListId != null && mailChimpListId.length() > 2)
        	utilTxServices.registrarUsuaruioEnMailChimp(transaction.getApplicationId() ,userData);
        
        PaymentResult p = new PaymentResult();
        p.setResult(PaymentResult.APPROVED);
        p.setResultMessage("Admin transaction.");
        HashMap<String, Object> authorizationinfo = new HashMap<>();
        authorizationinfo.put("tx_id", transaction.getTransactionId());
        p.setAuthorizationInfo(authorizationinfo);
        if (app.getApplicationControllerClass() != null && appController != null) {
            try {
                //Se actualiza el estatus del token a PENDING TO VALIDATE
                UserAccessToken uAToken = userServices.validateUserAccessToken(userToken);
                if (uAToken.getStatus() != TokenStatus.PENDING_VALIDATION) {
                    uAToken.setStatus(TokenStatus.PENDING_VALIDATION);
                    userServices.updateUserAccessTokenStatus(uAToken);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                appControllerParameters.put(ApplicationControllerParameterSelector.BUY_INIT_RESULT, buyInitResultParams);
                appControllerParameters.put(ApplicationControllerParameterSelector.PAYMENT_RESULT, p);
                ApplicationControllerResult tmp = appController.onFinishBuy(appControllerParameters);
                if (tmp != null && tmp.getResult() == ApplicationControllerResult.ERROR) {
                    p.setResult(PaymentResult.NOT_PROVISIONED);
                    p.setResultMessage(tmp.getResultMessage());
                    transaction.setTransactionStatus(Transaction.NOT_PROVISIONED);
                    dao.updateTransactionStatus(transaction);
                    return p;
                } else {
                    //Se actualiza el estatus de la transaccion a SUCCESS
                    transaction.setTransactionStatus(Transaction.SUCCESS_TX);
                    dao.updateTransactionStatus(transaction);
                }
            } catch (Exception e) {
                p.setResult(PaymentResult.POSTPAYMENT_ERROR);
                p.setResultMessage("ApplicationController <onBuyFinish>: " + e);
                return p;
            }
        } else {
            //Se actualiza el estatus de la transaccion a SUCCESS
            transaction.setTransactionStatus(Transaction.SUCCESS_TX);
            dao.updateTransactionStatus(transaction);
        }
        return p;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setSettledTransactions(List<GWTransactionInfo> settledTransactions, long customerId) {
        dao.setSettledTransactions(settledTransactions, customerId);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<GWTransactionInfo> getSettledTransactions(long customerId, int paymentGatewayId, Long fromMillis, Long toMillis) {
        try {
            PaymentGateway pGw = dao.getPaymentGateway(paymentGatewayId);
            CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
            Class c = loader.loadClass(pGw.getImplementorClass(), CoreServicesClassLoader.PAYMENT_GATEWAY);
            PaymentGwHandler handler = (PaymentGwHandler) context.getBean(c);
            CustomerPaymentGateway customerPaymentGateway = dao.getCustomerPaymentGatewayByPaymentGatewayId(customerId, paymentGatewayId);
            HashMap<String, Object> gwParameters = new HashMap<String, Object>();
            gwParameters.put(PaymentParameterSelector.AUTH_KEY_1, customerPaymentGateway.getAuthKey1());
            gwParameters.put(PaymentParameterSelector.AUTH_KEY_2, customerPaymentGateway.getAuthKey2());
            gwParameters.put(PaymentParameterSelector.AUTH_KEY_3, customerPaymentGateway.getAuthKey3());
            gwParameters.put(PaymentParameterSelector.AUTH_KEY_4, customerPaymentGateway.getAuthKey4());
            gwParameters.put(PaymentParameterSelector.PAYMENT_MODE, customerPaymentGateway.getPaymentModeId());
            return handler.listGwTransactions(fromMillis, toMillis, gwParameters);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Transaction> findCustomerUserSuccessTransactionsByApplication(long customerId, long customerUserId, long applicationId) {
        return dao.findCustomerUserSuccessTransactionsByApplication(customerId, customerUserId, applicationId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Transaction findTransactionById(long transactionId) {
        return dao.findTransactionById(transactionId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerUserPurchaseInfo> findCustomerPurchasaeInfoForSuccessLiveEventPurchases(long customerId, long applicationId) {
        return dao.findCustomerPurchasaeInfoForSuccessLiveEventPurchases(customerId, applicationId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerUserCreditCard> listCustomerUserCardsByPaymentGateway(long customerUserId, long customerId, int paymentGwId) throws PaymentGatewayException, NoPaymentProfileForPaymentGatewayException {
        try {
            //Verificar si el usuario tiene un perfil de pago creado
            CustomerUserPaymentProfile customerProfile = dao.findCustomerPaymentProfile(customerUserId, paymentGwId);
            if (customerProfile == null) {
                throw new NoPaymentProfileForPaymentGatewayException();
            }
            //Cargar el driver del gw
            PaymentGateway pGw = dao.getPaymentGateway(paymentGwId);
            if (pGw == null) {
                throw new PaymentGatewayException("Invalid payment gateway.");
            }
            //Cargar los parametros del gw de pago de acuerdo al cliente
            CustomerPaymentGateway gw = dao.getCustomerPaymentGatewayByPaymentGatewayId(customerId, paymentGwId);
            if (gw == null) {
                throw new PaymentGatewayException("Customer payment gateway info not found.");
            }
            HashMap<String, Object> pGwParameter = new HashMap<>();
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_1, gw.getAuthKey1());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_2, gw.getAuthKey2());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_3, gw.getAuthKey3());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_4, gw.getAuthKey4());
            pGwParameter.put(PaymentParameterSelector.PAYMENT_MODE, gw.getPaymentModeId());
            //Verificar si el Gw de pago se encuentra activo
            if (!pGw.isEnabled()) {
                throw new PaymentGatewayException("Payment gw is not enabled for customer.");
            }
            //Cargar driver de pago
            CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
            PaymentGwHandler pGwHandler = null;
            try {
                Class c = loader.loadClass(pGw.getImplementorClass(), CoreServicesClassLoader.PAYMENT_GATEWAY);
                pGwHandler = (PaymentGwHandler) context.getBean(c);
            } catch (Exception e) {
                throw new PaymentGatewayException("Payment gw driver can't be loaded.");
            }
            pGwParameter.put(PaymentParameterSelector.CUSTOMER_PROFILE, customerProfile.getPaymentGwCustomerId());
            return pGwHandler.listCustomerUserCards(pGwParameter);
        } catch (RuntimeException e) {
            throw new PaymentGatewayException(e);
        } catch (PaymentGwHandlerException e) {
            throw new PaymentGatewayException(e);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EmailAgent> listAllEmailAgents() {
        return dao.listAllEmailAgents();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EmailAgent findEmailAgentById(int emailAgentId) {
        try {
            return dao.findEmailAgentById(emailAgentId);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateEmailAgent(EmailAgent agent) {
        dao.updateEmailAgent(agent);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EmailAgentHist registerNewEmailAgentHist(EmailAgentHist emailAgentHist) {
        return dao.registerNewEmailAgentHist(emailAgentHist);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionDetailInfo> findCustomerUserTransactions(long customerUserId, long applicationId, Timestamp from, Timestamp to) {
        return dao.findCustomerUserTransactions(customerUserId, applicationId, from, to);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void resendPurchaseReceipt(long transactionId, long customerUserId, long customerId, long applicationId, int langId) {
        resendPurchaseReceipt(transactionId, customerUserId, customerId, applicationId, langId, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void resendPurchaseReceipt(long transactionId, long customerUserId, long customerId, long applicationId, int langId, String adminEmail) {
        try {
            CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
            Application app = dao.findApplicationInfo(applicationId);
            CustomerUser cu = dao.findCustomerUserById(customerUserId);
            User u = dao.findUserById(cu.getUserId());
            Class c = loader.loadClass(app.getApplicationControllerClass(), CoreServicesClassLoader.APPLICATION_CONTROLLER);
            ApplicationController appController = (ApplicationController) context.getBean(c);
            HashMap<String, Object> parameters = new HashMap<>();
            TransactionUserData tud = new TransactionUserData();
            tud.setName(cu.getFirstName());
            tud.setLastname(cu.getLastName() == null ? "" : cu.getLastName());
            tud.setEmail(u.getEmail());
            tud.setCustomerUserId(customerUserId);
            parameters.put(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA, tud);
            parameters.put(ApplicationControllerParameterSelector.CUSTOMER_ID, customerId);
            parameters.put(ApplicationControllerParameterSelector.APPLICATION_ID, applicationId);
            parameters.put(ApplicationControllerParameterSelector.TRANSACTION_ID, transactionId);
            parameters.put(ApplicationControllerParameterSelector.LANG, langId);
            parameters.put(ApplicationControllerParameterSelector.ADMIN_EMAIL, adminEmail);
            appController.sendPurchaseReceipt(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionDetailInfo> findCustomerUserTransactionInfo(long transactionId) {
        return dao.findCustomerUserTransactionInfo(transactionId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionDetailInfo> findCustomerUserTransactionsAll(long customerUserId) {
        return dao.findCustomerUserTransactionsAll(customerUserId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerUserCreditCard addCustomerUserCreditCardInPaymentGateway(long customerUserId, long customerId, int paymentGwId, HashMap<String, Object> parameters) throws PaymentGatewayException {
        try {
            //Cargar el driver del gw
            PaymentGateway pGw = dao.getPaymentGateway(paymentGwId);
            if (pGw == null) {
                throw new PaymentGatewayException("Invalid payment gateway.");
            }
            //Cargar los parametros del gw de pago de acuerdo al cliente
            CustomerPaymentGateway gw = dao.getCustomerPaymentGatewayByPaymentGatewayId(customerId, paymentGwId);
            if (gw == null) {
                throw new PaymentGatewayException("Customer payment gateway info not found.");
            }
            HashMap<String, Object> pGwParameter = new HashMap<>();
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_1, gw.getAuthKey1());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_2, gw.getAuthKey2());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_3, gw.getAuthKey3());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_4, gw.getAuthKey4());
            pGwParameter.put(PaymentParameterSelector.PAYMENT_MODE, gw.getPaymentModeId());
            //Verificar si el Gw de pago se encuentra activo
            if (!pGw.isEnabled()) {
                throw new PaymentGatewayException("Payment gw is not enabled for customer.");
            }
            //Cargar driver de pago
            CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
            PaymentGwHandler pGwHandler = null;
            try {
                Class c = loader.loadClass(pGw.getImplementorClass(), CoreServicesClassLoader.PAYMENT_GATEWAY);
                pGwHandler = (PaymentGwHandler) context.getBean(c);
            } catch (Exception e) {
                throw new PaymentGatewayException("Payment gw driver can't be loaded.");
            }

            //Verificar si el usuario tiene un perfil de pago creado
            CustomerUserPaymentProfile customerProfile = dao.findCustomerPaymentProfile(customerUserId, paymentGwId);
            if (customerProfile == null) {
                //Hay que crear el perfil en Stripe
                CustomerUser cu = dao.findCustomerUserById(customerUserId);
                User u = dao.findUserById(cu.getUserId());
                TransactionUserData tud = new TransactionUserData();
                tud.setName(cu.getFirstName());
                tud.setLastname(cu.getLastName());
                tud.setEmail(u.getEmail());
                HashMap<String, Object> createParameters = new HashMap<>();
                createParameters.put(PaymentParameterSelector.USER_DATA, tud);
                createParameters.putAll(pGwParameter);
                createParameters.putAll(parameters);
                String customerPGwId = pGwHandler.createCustomerProfile(createParameters);
                //Se a�ade en la base de datos
                customerProfile = new CustomerUserPaymentProfile();
                customerProfile.setCustomerId(customerId);
                customerProfile.setCustomerUserId(customerUserId);
                customerProfile.setPaymentGwCustomerId(customerPGwId);
                customerProfile.setPaymentGwId(paymentGwId);
                customerProfile = dao.addCustomerUserPaymentProfile(customerProfile);
                List<CustomerUserCreditCard> listCards;
                try {
                    listCards = this.listCustomerUserCardsByPaymentGateway(customerUserId, customerId, paymentGwId);
                } catch (NoPaymentProfileForPaymentGatewayException e) {
                    throw new PaymentGatewayException(e);
                }
                return listCards.get(0);
            } else {
                CustomerUser cu2 = dao.findCustomerUserById(customerUserId);
                User u2 = dao.findUserById(cu2.getUserId());
                TransactionUserData tud2 = new TransactionUserData();
                tud2.setName(cu2.getFirstName());
                tud2.setLastname(cu2.getLastName());
                tud2.setEmail(u2.getEmail());
                tud2.setCountry(cu2.getCountryId());
                tud2.setStateText(cu2.getStateText());
                tud2.setCityText(cu2.getCityText());
                tud2.setAddress(cu2.getAddress());
                tud2.setPostcode(cu2.getPostalCode());
                HashMap<String, Object> createParameters = new HashMap<>();
                pGwParameter.put(PaymentParameterSelector.USER_DATA, tud2);

                pGwParameter.put(PaymentParameterSelector.CUSTOMER_PROFILE, customerProfile.getPaymentGwCustomerId());
                pGwParameter.putAll(parameters);
                return pGwHandler.addCreditCard(pGwParameter);
            }
        } catch (RuntimeException e) {
            throw new PaymentGatewayException(e);
        } catch (PaymentGwHandlerException e) {
            throw new PaymentGatewayException(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeCustomerUserCreditCardInPaymentGateway(long customerUserId, long customerId, int paymentGwId, HashMap<String, Object> parameters) throws PaymentGatewayException, NoPaymentProfileForPaymentGatewayException {
        try {
            //Verificar si el usuario tiene un perfil de pago creado
            CustomerUserPaymentProfile customerProfile = dao.findCustomerPaymentProfile(customerUserId, paymentGwId);
            if (customerProfile == null) {
                throw new NoPaymentProfileForPaymentGatewayException();
            }
            //Cargar el driver del gw
            PaymentGateway pGw = dao.getPaymentGateway(paymentGwId);
            if (pGw == null) {
                throw new PaymentGatewayException("Invalid payment gateway.");
            }
            //Cargar los parametros del gw de pago de acuerdo al cliente
            CustomerPaymentGateway gw = dao.getCustomerPaymentGatewayByPaymentGatewayId(customerId, paymentGwId);
            if (gw == null) {
                throw new PaymentGatewayException("Customer payment gateway info not found.");
            }
            HashMap<String, Object> pGwParameter = new HashMap<>();
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_1, gw.getAuthKey1());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_2, gw.getAuthKey2());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_3, gw.getAuthKey3());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_4, gw.getAuthKey4());
            pGwParameter.put(PaymentParameterSelector.PAYMENT_MODE, gw.getPaymentModeId());
            //Verificar si el Gw de pago se encuentra activo
            if (!pGw.isEnabled()) {
                throw new PaymentGatewayException("Payment gw is not enabled for customer.");
            }
            //Cargar driver de pago
            CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
            PaymentGwHandler pGwHandler = null;
            try {
                Class c = loader.loadClass(pGw.getImplementorClass(), CoreServicesClassLoader.PAYMENT_GATEWAY);
                pGwHandler = (PaymentGwHandler) context.getBean(c);
            } catch (Exception e) {
                throw new PaymentGatewayException("Payment gw driver can't be loaded.");
            }
            pGwParameter.put(PaymentParameterSelector.CUSTOMER_PROFILE, customerProfile.getPaymentGwCustomerId());
            pGwParameter.putAll(parameters);
            pGwHandler.removeCreditCard(pGwParameter);
        } catch (RuntimeException e) {
            throw new PaymentGatewayException(e);
        } catch (PaymentGwHandlerException e) {
            throw new PaymentGatewayException(e);
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setDefaultCustomerUserCreditCardInPaymentGateway(long customerUserId, long customerId, int paymentGwId, HashMap<String, Object> parameters) throws PaymentGatewayException, NoPaymentProfileForPaymentGatewayException {
        try {
            //Verificar si el usuario tiene un perfil de pago creado
            CustomerUserPaymentProfile customerProfile = dao.findCustomerPaymentProfile(customerUserId, paymentGwId);
            if (customerProfile == null) {
                throw new NoPaymentProfileForPaymentGatewayException();
            }
            //Cargar el driver del gw
            PaymentGateway pGw = dao.getPaymentGateway(paymentGwId);
            if (pGw == null) {
                throw new PaymentGatewayException("Invalid payment gateway.");
            }
            //Cargar los parametros del gw de pago de acuerdo al cliente
            CustomerPaymentGateway gw = dao.getCustomerPaymentGatewayByPaymentGatewayId(customerId, paymentGwId);
            if (gw == null) {
                throw new PaymentGatewayException("Customer payment gateway info not found.");
            }
            HashMap<String, Object> pGwParameter = new HashMap<>();
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_1, gw.getAuthKey1());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_2, gw.getAuthKey2());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_3, gw.getAuthKey3());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_4, gw.getAuthKey4());
            pGwParameter.put(PaymentParameterSelector.PAYMENT_MODE, gw.getPaymentModeId());
            //Verificar si el Gw de pago se encuentra activo
            if (!pGw.isEnabled()) {
                throw new PaymentGatewayException("Payment gw is not enabled for customer.");
            }
            //Cargar driver de pago
            CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
            PaymentGwHandler pGwHandler = null;
            try {
                Class c = loader.loadClass(pGw.getImplementorClass(), CoreServicesClassLoader.PAYMENT_GATEWAY);
                pGwHandler = (PaymentGwHandler) context.getBean(c);
            } catch (Exception e) {
                throw new PaymentGatewayException("Payment gw driver can't be loaded.");
            }
            pGwParameter.put(PaymentParameterSelector.CUSTOMER_PROFILE, customerProfile.getPaymentGwCustomerId());
            pGwParameter.putAll(parameters);
            pGwHandler.setDefaultCreditCard(pGwParameter);
        } catch (RuntimeException e) {
            throw new PaymentGatewayException(e);
        } catch (PaymentGwHandlerException e) {
            throw new PaymentGatewayException(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ScheduledDonation addScheduledDonation(long customerId, long applicationId, int langId, int paymentGwId, ScheduledDonation scheduledDonation, List<ScheduledDonationFundDetail> funds, HashMap<String, Object> parameters) throws DonationGeneralException, PaymentGatewayException, NoPaymentProfileForPaymentGatewayException {
        try {
            //Verificar si el usuario tiene un perfil de pago creado
            CustomerUserPaymentProfile customerProfile = dao.findCustomerPaymentProfile(scheduledDonation.getCustomer_user_id(), paymentGwId);
            if (customerProfile == null) {
                throw new NoPaymentProfileForPaymentGatewayException();
            }
            //Cargar el driver del gw
            PaymentGateway pGw = dao.getPaymentGateway(paymentGwId);
            if (pGw == null) {
                throw new PaymentGatewayException("Invalid payment gateway.");
            }
            //Cargar los parametros del gw de pago de acuerdo al cliente
            CustomerPaymentGateway gw = dao.getCustomerPaymentGatewayByPaymentGatewayId(customerId, paymentGwId);
            if (gw == null) {
                throw new PaymentGatewayException("Customer payment gateway info not found.");
            }
            HashMap<String, Object> pGwParameter = new HashMap<>();
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_1, gw.getAuthKey1());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_2, gw.getAuthKey2());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_3, gw.getAuthKey3());
            pGwParameter.put(PaymentParameterSelector.AUTH_KEY_4, gw.getAuthKey4());
            pGwParameter.put(PaymentParameterSelector.PAYMENT_MODE, gw.getPaymentModeId());
            //Verificar si el Gw de pago se encuentra activo
            if (!pGw.isEnabled()) {
                throw new PaymentGatewayException("Payment gw is not enabled for customer.");
            }
            //Cargar driver de pago
            CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
            PaymentGwHandler pGwHandler = null;
            try {
                Class c = loader.loadClass(pGw.getImplementorClass(), CoreServicesClassLoader.PAYMENT_GATEWAY);
                pGwHandler = (PaymentGwHandler) context.getBean(c);
            } catch (Exception e) {
                throw new PaymentGatewayException("Payment gw driver can't be loaded.");
            }

            pGwParameter.put(PaymentParameterSelector.CUSTOMER_PROFILE, customerProfile.getPaymentGwCustomerId());
            pGwParameter.putAll(parameters);
            pGwHandler.createCustomerSubscription(pGwParameter);
        } catch (RuntimeException e) {
            throw new PaymentGatewayException(e);
        } catch (PaymentGwHandlerException e) {
            throw new PaymentGatewayException(e);
        }
        return scheduledDonation;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    boolean isFraudulentTransaction(String ipAddress, String email, int rejectTxCount) {
        String blackListEnabledStr = systemSettingsManager.getSystemProperty("BLACKLIST_ENABLED");
        blackListEnabledStr = (blackListEnabledStr == null) ? "false" : blackListEnabledStr;
        boolean blackListEnabled = Boolean.parseBoolean(blackListEnabledStr);
        if (blackListEnabled) {
            String allowedAttemptsStr = systemSettingsManager.getSystemProperty("BLACKLIST_MAX_DENIED_ATTEMPTS");
            int allowedAttempts = (allowedAttemptsStr == null) ? 5 : Integer.parseInt(allowedAttemptsStr);
            if (rejectTxCount >= allowedAttempts) {
                //Se debe proceder a bloquear todo
                try {
                    BlackListItem emailToBlock = new BlackListItem();
                    emailToBlock.setDataTypeId(BlackListDataTypeSelector.EMAIL);
                    emailToBlock.setData(email);
                    try {
                        BlackListItem checkEmail = blackListServices.findBlackListItemByDataTypeIdAndData(BlackListDataTypeSelector.EMAIL, email);
                        if (checkEmail == null) {
                            blackListServices.addBlacklistItem(emailToBlock);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (ipAddress != null) {
                    try {
                        BlackListItem ipToBlock = new BlackListItem();
                        ipToBlock.setDataTypeId(BlackListDataTypeSelector.IP);
                        ipToBlock.setData(ipAddress);
                        try {
                            BlackListItem checkIP = blackListServices.findBlackListItemByDataTypeIdAndData(BlackListDataTypeSelector.IP, ipAddress);
                            if (checkIP == null) {
                                blackListServices.addBlacklistItem(ipToBlock);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Collection<BulkTransactionVerificationResultItem> validateBulkTransactionData(Collection<BulkTransactionItem> items) {
        List<ExternalPaymentType> allExternalPaymentTypes = dao.listAllExternalPaymentTypes();
        List<String> validPaymentTypes = new ArrayList<>();
        allExternalPaymentTypes.forEach(externalPaymentType -> validPaymentTypes.add(externalPaymentType.getName().toUpperCase()));
        validPaymentTypes.add("COMPLIMENTARY");
        List<BulkTransactionVerificationResultItem> result = new ArrayList<>();
        items.forEach(item -> {
            BulkTransactionVerificationResultItem tmp = new BulkTransactionVerificationResultItem();
            tmp.setItem(item);
            //Validacion del email
            if (!EmailValidator.getInstance().isValid(item.getEmail())) {
                tmp.setValid(false);
                tmp.setMessage("Invalid email address");
            } else if (!validPaymentTypes.contains(item.getPaymentType().toUpperCase())) {
                tmp.setValid(false);
                tmp.setMessage("Invalid payment type");
            } else {
                tmp.setValid(true);
                tmp.setMessage("Valid");
            }
            result.add(tmp);
        });
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Fund> listAllTransactionFundsByTransactionId(long transactionid) {
        return dao.listAllTransactionFundsByTransactionId(transactionid);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateTransactionStatus(Transaction t) {
        dao.updateTransactionStatus(t);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EventCommissionSettings addEventCommisssionSettings(EventCommissionSettings eventCommissionSettings) {
        return dao.addEventCommissionSettings(eventCommissionSettings);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateEventCommissionSettings(EventCommissionSettings eventCommissionSettings) {
        dao.updateEventCommissionSettings(eventCommissionSettings);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EventCommissionSettings findEventCommissionSettings(int id) {
        try {
            return dao.findEventCommissionSettings(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public EventCommissionSettings findEventCommissionSettings(long customer_id, long application_id) {
        try {
            return dao.findEventCommissionSettings(customer_id, application_id);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerUser getLastApprovedUserTransactionByEmail(String email)  {
		return dao.getLastApprovedUserTransactionByEmail(email);
	}

}
