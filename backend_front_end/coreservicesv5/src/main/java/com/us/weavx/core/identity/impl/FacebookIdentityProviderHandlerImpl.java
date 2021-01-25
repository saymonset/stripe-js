package com.us.weavx.core.identity.impl;

import org.springframework.stereotype.Component;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import com.us.weavx.core.exception.ExternalProfileIdentityProviderException;
import com.us.weavx.core.exception.InvalidExternalProfileAccessTokenException;
import com.us.weavx.core.identity.ExternalProfileIdentityProviderHandler;
import com.us.weavx.core.model.ProfileData;
@Component
public class FacebookIdentityProviderHandlerImpl implements ExternalProfileIdentityProviderHandler {

	public String getEmail(String accessToken) throws ExternalProfileIdentityProviderException {
		User u = null;
		String email = null;
		try {
			FacebookClient client = new DefaultFacebookClient(accessToken, Version.LATEST);
			u = client.fetchObject("/me", User.class,Parameter.with("fields", "email"));
			if (u != null) {
				email = u.getEmail();
				return email;
			} else {
				throw new ExternalProfileIdentityProviderException();
			}
		} catch (Exception e) {
			throw new ExternalProfileIdentityProviderException(e);
		}
	}

	public ProfileData getProfileData(String accessToken)
			throws InvalidExternalProfileAccessTokenException, ExternalProfileIdentityProviderException {
		User u = null;
		ProfileData extData = null;
		try {
			FacebookClient client = new DefaultFacebookClient(accessToken, Version.LATEST);
			u = client.fetchObject("/me", User.class,Parameter.with("fields", "email, first_name, last_name, birthday, gender, picture, age_range"));
			if (u != null) {
				extData = new ProfileData();
				extData.putProperty(ProfileData.EMAIL, u.getEmail());
				extData.putProperty(ProfileData.FIRST_NAME, u.getFirstName());
				extData.putProperty(ProfileData.LAST_NAME, u.getLastName());
				extData.putProperty(ProfileData.GENDER, u.getGender());
				extData.putProperty(ProfileData.BIRTHDAY, u.getBirthday());
				extData.putProperty(ProfileData.AGE_RANGE, u.getAgeRange().getMin()+"-"+u.getAgeRange().getMax());
				extData.putProperty(ProfileData.ID, u.getId());
				extData.putProperty(ProfileData.PICTURE, u.getPicture().getUrl());
				return extData;
			} else {
				throw new ExternalProfileIdentityProviderException();
			}
		} catch (Exception e) {
			throw new ExternalProfileIdentityProviderException(e);
		}
	}

}
