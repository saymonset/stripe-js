package com.us.weavx.core.identity;

import com.us.weavx.core.exception.ExternalProfileIdentityProviderException;
import com.us.weavx.core.exception.InvalidExternalProfileAccessTokenException;
import com.us.weavx.core.model.ProfileData;

public interface ExternalProfileIdentityProviderHandler {
	
	public String getEmail(String accessToken) throws InvalidExternalProfileAccessTokenException, ExternalProfileIdentityProviderException;
	public ProfileData getProfileData(String accessToken) throws InvalidExternalProfileAccessTokenException, ExternalProfileIdentityProviderException;

}
