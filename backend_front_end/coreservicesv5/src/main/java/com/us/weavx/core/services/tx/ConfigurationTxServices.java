package com.us.weavx.core.services.tx;

import com.us.weavx.core.constants.TokenStatus;
import com.us.weavx.core.data.CoreServicesTxDAO;
import com.us.weavx.core.exception.ObjectDoesNotExistsException;
import com.us.weavx.core.exception.UnauthorizedAccessException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AccessKey;
import com.us.weavx.core.model.AccessToken;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.ApplicationParameter;
import com.us.weavx.core.model.City;
import com.us.weavx.core.model.Continent;
import com.us.weavx.core.model.Country;
import com.us.weavx.core.model.CustomerDefaultPaymentGateway;
import com.us.weavx.core.model.CustomerIdentityProvider;
import com.us.weavx.core.model.CustomerPaymentGateway;
import com.us.weavx.core.model.CustomerProperty;
import com.us.weavx.core.model.CustomerSupportedExternalPaymentType;
import com.us.weavx.core.model.CustomerSupportedPaymentType;
import com.us.weavx.core.model.CustomerSystemMessage;
import com.us.weavx.core.model.DiscountType;
import com.us.weavx.core.model.DonationAmount;
import com.us.weavx.core.model.EventDescription;
import com.us.weavx.core.model.EventFaqLangDescription;
import com.us.weavx.core.model.ExternalPaymentType;
import com.us.weavx.core.model.Fund;
import com.us.weavx.core.model.FundDescription;
import com.us.weavx.core.model.FundImage;
import com.us.weavx.core.model.Gender;
import com.us.weavx.core.model.GenderDescription;
import com.us.weavx.core.model.GeneralDescription;
import com.us.weavx.core.model.IdentityProvider;
import com.us.weavx.core.model.Language;
import com.us.weavx.core.model.Method;
import com.us.weavx.core.model.PaymentGateway;
import com.us.weavx.core.model.PaymentType;
import com.us.weavx.core.model.PaymentTypeDescription;
import com.us.weavx.core.model.Property;
import com.us.weavx.core.model.State;
import com.us.weavx.core.model.SystemMessage;
import com.us.weavx.core.model.SystemProperty;
import com.us.weavx.core.model.TransactionCampaing;
import com.us.weavx.core.model.TransactionMedium;
import com.us.weavx.core.model.TransactionSource;
import com.us.weavx.core.model.TransactionStatus;
import com.us.weavx.core.model.VenueDescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("configurationTxServices")
public class ConfigurationTxServices {

    @Autowired
    private CoreServicesTxDAO dao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void registerNewAccessKey(AccessKey key) {
        dao.registerNewAccessKey(key);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public boolean validateAccessKey(AccessKey keys) {
        return dao.validateAccessKey(keys);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public HashMap<String, Object> findDeveloperAppCustomerInfo(String accessKey, String accessSecret, String devKey, String devSecret) throws UnauthorizedAccessException {
        return dao.findDeveloperAppCustomerInfo(accessKey, accessSecret, devKey, devSecret);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int registerNewAccessToken(AccessToken token, long duration) {
        return dao.registerNewAccessToken(token, duration);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void changeAccessTokenStatus(AccessToken token) throws ObjectDoesNotExistsException {
        dao.changeAccessTokenStatus(token);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void renewAccessToken(String accessToken, int minutesToExtend) throws ObjectDoesNotExistsException {
        AccessInfo aInfo = dao.getAccessInfo(accessToken);
    	AccessToken token = aInfo.getAccessToken();
    	if (token.getStatus() != TokenStatus.EXPIRED) {
    		throw new ObjectDoesNotExistsException();
    	} else {
    		LocalDateTime expiration = token.getExpirationDate().toLocalDateTime();
    		expiration = expiration.plusMinutes(minutesToExtend);
    		token.setExpirationDate(Timestamp.valueOf(expiration));
    		token.setStatus(TokenStatus.ACTIVE);
    		dao.renewAccessToken(token);
    	}
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public AccessInfo getAccessInfo(String accessToken) throws ObjectDoesNotExistsException {
        return dao.getAccessInfo(accessToken);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Method getMethodInfoByName(String methodName) throws ObjectDoesNotExistsException {
        return dao.getMethodInfoByName(methodName);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public IdentityProvider getIdentityProviderInfo(int identityProviderId) throws ObjectDoesNotExistsException {
        return dao.getIdentityProviderInfo(identityProviderId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public ArrayList<IdentityProvider> listAllIdentityProviders() {
        return dao.listAllIdentityProviders();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerIdentityProvider> getCustomerIdentityProviders(long customerId) {
        return dao.getCustomerIdentityProviders(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Property> listAllProperties() {
        return dao.listAllProperties();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<SystemMessage> listAllSystemMessages() {
        return dao.listAllSystemMessages();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Language> listAllLanguages() {
        return dao.listAllLanguages();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Gender> getCustomerGenders(long customerId) {
        return dao.getCustomerGenders(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<GenderDescription> getCustomerGendersDescription(long customerId) {
        return dao.getCustomerGendersDescription(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Country> listAllCountries() {
        return dao.listAllCountries();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<State> listStatesByCountry(int countryId) {
        return dao.listStatesByCountry(countryId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<State> listAllStates() {
        return dao.listAllStates();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<City> listAllCities() {
        return dao.listAllCities();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<City> listCitiesByState(int stateId) {
        return dao.listCitiesByState(stateId);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Property createProperty(Property property) {
        Property prop = dao.getPropertyByName(property.getName());
        if (prop != null) {
            return prop;
        } else {
            return dao.addProperty(property);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Property updateProperty(Property property) {
        Property prop = dao.getPropertyByName(property.getName());
        if (prop != null) {
            prop = dao.updateProperty(property);
            return prop;
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerProperty createCustomerProperty(CustomerProperty customerProperty) {
        CustomerProperty prop = dao.addCustomerProperty(customerProperty);
        return prop;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public CustomerProperty findCustomerProperty(long customerId, int langId, String propertyName) {
        return dao.findCustomerProperty(customerId, langId, propertyName);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerProperty> getCustomerProperties(long customerId) {
        return dao.getCustomerProperties(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerSystemMessage> getCustomerSystemMessages(long customerId) {
        return dao.getCustomerSystemMessages(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerPaymentGateway> getCustomerPaymentGatewayInfo(long customerId) {
        return dao.getCustomerPaymentGateways(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Application findApplicationInfo(long applicationId) {
        return dao.findApplicationInfo(applicationId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerSupportedPaymentType> getCustomerSupportedPaymentTypes(long customerId) {
        return dao.getCustomerSupportedPaymentTypes(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerDefaultPaymentGateway> getCustomerDefaultPaymentGateways(long customerId) {
        return dao.getCustomerDefaultPaymentGateways(customerId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CustomerDefaultPaymentGateway updateCustomerDefaultPaymentGateway(CustomerDefaultPaymentGateway custDefaultPGw) {
        return dao.updateCustomerDefaultPaymentGatewayByPaymentType(custDefaultPGw);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<PaymentType> listAllPaymentTypes() {
        return dao.listAllPaymentTypes();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<PaymentTypeDescription> listAllPaymentTypeDescriptions() {
        return dao.listAllPaymentTypeDescriptions();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Application> listCustomerApplications(long customerId) {
        return dao.getCustomerApplications(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionStatus> listTransactionStatus() {
        return dao.listTransactionStatus();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<PaymentGateway> listAllPaymentGateways() {
        return dao.listAllPaymentGateways();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionSource addTransactionSource(final TransactionSource transactionSource) {
        return dao.addTransactionSource(transactionSource);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionSource> listCustomerTransactionSources(long customerId) {
        return dao.listCustomerSources(customerId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionCampaing addTransactionCampaing(final TransactionCampaing transactionCampaing) {
        return dao.addTransactionCampaing(transactionCampaing);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionCampaing> listCustomerTransactionCampaings(long customerId) {
        return dao.listTransactionCampaings(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Fund> getCustomerFunds(long customerId) {
        return dao.getCustomerFunds(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Fund findFundById(int fundId) {
        return dao.findFundById(fundId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<FundDescription> getCustomerFundDescriptions(long customerId) {
        return dao.getCustomerFundsDescription(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<FundImage> getCustomerFundImages(long customerId) {
        return dao.getCustomerFundImages(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<DonationAmount> getCustomerDonationAmounts(long customerId) {
        return dao.getCustomerDonationAmounts(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<SystemProperty> getSystemProperties() {
        return dao.getSystemProperties();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ApplicationParameter> findAllApplicationParameters(long applicationId) {
        return dao.findAllApplicationParameters(applicationId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public TransactionMedium addTransactionMedium(final TransactionMedium transactionMedium) {
        return dao.addTransactionMedium(transactionMedium);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<TransactionMedium> listCustomerMediums(long customerId) {
        return dao.listCustomerMediums(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<Continent> listAllContinents() {
        return dao.listAllContinents();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<ExternalPaymentType> listAllExternalPaymentTypes() {
        return dao.listAllExternalPaymentTypes();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<CustomerSupportedExternalPaymentType> listCustomerSupportedExternalPaymentTypes(long customerId) {
        return dao.listCustomerSupportedExternalPaymentTypes(customerId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<DiscountType> listAllDiscountTypes() {
        return dao.listAllDiscountTypes();
    }

    public void connectPaymentGatewayAccount(long customerId, long paymentGatewayId, String newConnectedAccountId) {
        dao.updateCustomerPaymentGatewayConnectedAccount(customerId, paymentGatewayId, newConnectedAccountId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EventDescription> getEventDescriptions(long eventId) {
        return dao.getEventDescription(eventId);
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<VenueDescription> getVenueDescriptions(long venueId) {
        return dao.getVenueDescription(venueId);
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<GeneralDescription> getGeneralDescriptions(long customerId) {
        return dao.getGeneralDescription(customerId);
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public List<EventFaqLangDescription> getEventFaqLanfDescriptions(long eventId) {
        return dao.getEventFaqLangDescription(eventId);
    }
}
