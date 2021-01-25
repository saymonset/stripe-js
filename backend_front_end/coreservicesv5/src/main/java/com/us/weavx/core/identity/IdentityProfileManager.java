package com.us.weavx.core.identity;

import com.us.weavx.core.exception.CoreServicesClassLoadingException;
import com.us.weavx.core.exception.ExternalProfileIdentityProviderException;
import com.us.weavx.core.exception.IdentityProviderLoadingException;
import com.us.weavx.core.exception.IdentityProviderManagerGeneralException;
import com.us.weavx.core.exception.InvalidExternalProfileAccessTokenException;
import com.us.weavx.core.exception.ObjectDoesNotExistsException;
import com.us.weavx.core.exception.UnknownIdentityProviderException;
import com.us.weavx.core.model.ExternalProfile;
import com.us.weavx.core.model.IdentityProvider;
import com.us.weavx.core.model.ProfileData;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.util.CoreServicesClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class IdentityProfileManager {

    private ExternalProfileIdentityProviderHandler handler = null;

    @Autowired
    private ConfigurationTxServices configurationServices;
    @Autowired
    private ApplicationContext context;


    public void loadManager(int identityProviderId) throws UnknownIdentityProviderException, IdentityProviderManagerGeneralException {
        try {
            this.handler = loadProvider(identityProviderId);
        } catch (UnknownIdentityProviderException e) {
            throw e;
        } catch (IdentityProviderLoadingException e) {
            throw new IdentityProviderManagerGeneralException(e);
        }
    }

    private ExternalProfileIdentityProviderHandler loadProvider(int providerId) throws UnknownIdentityProviderException, IdentityProviderLoadingException {
		try {
			IdentityProvider provider = configurationServices.getIdentityProviderInfo(providerId);
			CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
			Class c = loader.loadClass(provider.getProviderImplementorClass(), CoreServicesClassLoader.IDENTITY_PROVIDER);
			ExternalProfileIdentityProviderHandler providerHandler = (ExternalProfileIdentityProviderHandler) context.getBean(c);
			return providerHandler;
		} catch (ObjectDoesNotExistsException e) {
			throw new UnknownIdentityProviderException(e);
		} catch (CoreServicesClassLoadingException e) {
			throw new IdentityProviderLoadingException(e);
		} catch (RuntimeException e) {
			throw new IdentityProviderLoadingException(e);
		}
	}

	public boolean validateProfile(ExternalProfile externalProfile) throws UnknownIdentityProviderException, IdentityProviderManagerGeneralException, InvalidExternalProfileAccessTokenException {
			try {
				String email = handler.getEmail(externalProfile.getAccesstoken());
				return (email != null);
			} catch (ExternalProfileIdentityProviderException e) {
                throw new IdentityProviderManagerGeneralException(e);
            }
	}

    public String getEmail(ExternalProfile externalProfile) throws UnknownIdentityProviderException, IdentityProviderManagerGeneralException, InvalidExternalProfileAccessTokenException {
		try {
			String email = handler.getEmail(externalProfile.getAccesstoken());
			return email;
		} catch (ExternalProfileIdentityProviderException e) {
            throw new IdentityProviderManagerGeneralException(e);
        }
	}

    public ProfileData getExternalUserData(ExternalProfile externalProfile) throws UnknownIdentityProviderException, IdentityProviderManagerGeneralException, InvalidExternalProfileAccessTokenException {
		try {
			ProfileData extData = handler.getProfileData(externalProfile.getAccesstoken());
			return extData;
		} catch (ExternalProfileIdentityProviderException e) {
			throw new IdentityProviderManagerGeneralException(e);
		}
	}

}
