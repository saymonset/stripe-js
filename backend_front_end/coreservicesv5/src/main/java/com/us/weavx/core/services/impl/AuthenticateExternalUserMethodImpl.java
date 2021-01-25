package com.us.weavx.core.services.impl;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.IdentityProviderManagerGeneralException;
import com.us.weavx.core.exception.InvalidExternalProfileAccessTokenException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.exception.UnknownCustomerUserException;
import com.us.weavx.core.exception.UnknownIdentityProviderException;
import com.us.weavx.core.exception.UserAccessTokenGenerationException;
import com.us.weavx.core.identity.IdentityProfileManager;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.ExternalProfile;
import com.us.weavx.core.model.ProfileData;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.User;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.util.GeneralUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AuthenticateExternalUserMethodImpl implements ServiceMethod {
    @Autowired
    private ConfigurationTxServices configurationTxServices;
    @Autowired
    private UserTxServices userServices;
    @Autowired
    private IdentityProfileManager profileManager;

    public Response executeMethod(Request request) {
        try {
            HashMap<String, Object> params = request.getParameters();
            AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
            long customerId = aInfo.getCustomerId();
            String ipAddress = (String) params.get("ipAddress");
            if (ipAddress == null) {
                Response res = new Response();
                res.setReturnCode(ServiceReturnMessages.IP_ADDRESS_REQUIRED_CODE);
				res.setReturnMessage(ServiceReturnMessages.IP_ADDRESS_REQUIRED);
				return res;
			}
            String userAgent = (String) params.get("userAgent");
            if (userAgent == null) {
                Response res = new Response();
                res.setReturnCode(ServiceReturnMessages.USER_AGENT_REQUIRED_CODE);
                res.setReturnMessage(ServiceReturnMessages.USER_AGENT_REQUIRED);
                return res;
            }
            //Context
            HashMap<String, Object> externalProfileParam = (HashMap<String, Object>) params.get("external_user_profile");
            ExternalProfile profile = new ExternalProfile();
            profile.setIdentityProviderId((Integer) externalProfileParam.get("identityProviderId"));
            profile.setAccesstoken((String) externalProfileParam.get("accessToken"));
            profileManager.loadManager(profile.getIdentityProviderId());
            if (profileManager.validateProfile(profile)) {
                String email = profileManager.getEmail(profile).toLowerCase();
                //Se verifica si existe el usuario
                User user;
                user = userServices.findUserByEmail(email);
                if (user == null) {
                    //No existe el usuario por ende se crea
                    user = new User(email);
                    user = userServices.registerNewUser(user);
                }
                profile.setUserId(user.getId());
                //Ahora se verifica si existe el usuario para el cliente asociado al token
                CustomerUser custUser = userServices.findCustomerUserByUserId(user.getId(), customerId);
                if (custUser == null) {
                    //Se obtiene la informacion que sea posible del autenticador externo y se registra
                    ProfileData userExtData = profileManager.getExternalUserData(profile);
                    custUser = new CustomerUser();
                    custUser.setUserId(user.getId());
                    custUser.setCustomerId(customerId);
                    String tmp = (String) userExtData.getProperty(ProfileData.FIRST_NAME);
                    custUser.setFirstName((tmp == null) ? "" : tmp);
                    tmp = (String) userExtData.getProperty(ProfileData.LAST_NAME);
                    custUser.setLastName((tmp == null) ? "" : tmp);
                    custUser.setBirthDate(GeneralUtilities.parseDate((String) userExtData.getProperty(ProfileData.BIRTHDAY)));
                    custUser.setProfileImage((String) userExtData.getProperty(ProfileData.PICTURE));
                    custUser = userServices.registerNewCustomerUser(custUser, aInfo.getApplicationId(), ipAddress, userAgent).getCustomerUser().getCustUser();
                }
				//Se veifica si ya el usuario tiene un perfil externo para el proveedor de identidad indicado
				ExternalProfile prof = userServices.findUserExternalProfile(profile.getIdentityProviderId(), profile.getUserId());
				if (prof == null) {
					userServices.registerNewExternalUser(profile);
				} else {
					profile.setId(prof.getId());
					userServices.updateExternalUserProfileToken(profile);
				}
				//Se genera un user_access_token
				AuthenticatedUserInfo userInfo;
				try {
					userInfo = userServices.authenticateExternalUser(custUser.getId(), aInfo.getCustomerId(), aInfo.getApplicationId(), ipAddress, userAgent);
				} catch (UnknownCustomerUserException e) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER_CODE);
					res.setReturnMessage(ServiceReturnMessages.UNKNOWN_CUSTOMER_USER);
					return res;
				} catch (UserAccessTokenGenerationException e) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.ERROR_GENERATING_USER_TOKEN_CODE);
					res.setReturnMessage(ServiceReturnMessages.ERROR_GENERATING_USER_TOKEN);
					return res;
				} catch (MaximumCustomerUserSessionsForAppExceededException e) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED_CODE);
					res.setReturnMessage(ServiceReturnMessages.MAXIMUM_SESSIONS_EXCEEDED);
					HashMap<String, Object> result = new HashMap<>();
					result.put("userSessions", e.getCurrentSessions());
					res.setResult(result);
					return res;
				}
				Response res = new Response();
				HashMap<String, Object> result = new HashMap<>();
				result.put("customer_user", custUser);
				result.put("email", user.getEmail());
				result.put("user_access_token", userInfo.getUserAccessToken().getToken());
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				res.setResult(result);
				return res;
			} else {
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.INVALID_EXTERNAL_PROFILE_CODE);
				res.setReturnMessage(ServiceReturnMessages.INVALID_EXTERNAL_PROFILE);
				return res;
			}
		} catch (RuntimeException | IdentityProviderManagerGeneralException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
			return res;
		} catch (UnknownIdentityProviderException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.UNKNOWN_IDENTITY_PROVIDER_CODE);
			res.setReturnMessage(ServiceReturnMessages.UNKNOWN_IDENTITY_PROVIDER);
			return res;
		} catch (InvalidExternalProfileAccessTokenException e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.INVALID_EXTERNAL_PROFILE_CODE);
			res.setReturnMessage(ServiceReturnMessages.INVALID_EXTERNAL_PROFILE);
			return res;
		}
	}


}
