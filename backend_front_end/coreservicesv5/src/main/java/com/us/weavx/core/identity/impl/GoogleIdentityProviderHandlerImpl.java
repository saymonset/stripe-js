package com.us.weavx.core.identity.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.us.weavx.core.exception.ExternalProfileIdentityProviderException;
import com.us.weavx.core.exception.InvalidExternalAccessTokenException;
import com.us.weavx.core.exception.InvalidExternalProfileAccessTokenException;
import com.us.weavx.core.identity.ExternalProfileIdentityProviderHandler;
import com.us.weavx.core.model.ProfileData;
@Component
public class GoogleIdentityProviderHandlerImpl implements ExternalProfileIdentityProviderHandler {

	public String getEmail(String accessToken) throws ExternalProfileIdentityProviderException, InvalidExternalProfileAccessTokenException  {
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier(new NetHttpTransport(), new GsonFactory());
			GoogleIdToken idToken =  GoogleIdToken.parse(new GsonFactory(), accessToken);
			if (idToken != null && verifier.verify(idToken)) {
				//Valido el Token y se puede obtener el email
				Payload payload = idToken.getPayload();
				return payload.getEmail();
			} else {
				throw new InvalidExternalProfileAccessTokenException();
			}
		} catch (RuntimeException e) {
			throw new ExternalProfileIdentityProviderException(e);
		} catch (GeneralSecurityException e) {
			throw new ExternalProfileIdentityProviderException(e);
		} catch (IOException e) {
			throw new ExternalProfileIdentityProviderException(e);
		} 
	}

	public ProfileData getProfileData(String accessToken)
			throws InvalidExternalProfileAccessTokenException, ExternalProfileIdentityProviderException {
		ProfileData extData = null;
		
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier(new NetHttpTransport(), new GsonFactory());
			GoogleIdToken idToken =  GoogleIdToken.parse(new GsonFactory(), accessToken);
			if (idToken != null && verifier.verify(idToken)) {
				//Valido el Token y se puede obtener el email
				Payload payload = idToken.getPayload();
				extData = new ProfileData();
				extData.putProperty(ProfileData.EMAIL, payload.getEmail());
				extData.putProperty(ProfileData.FIRST_NAME, payload.get("name"));
				extData.putProperty(ProfileData.LAST_NAME, payload.get("familyName"));
				extData.putProperty(ProfileData.GENDER, payload.get("gender"));
				extData.putProperty(ProfileData.BIRTHDAY, null);
				extData.putProperty(ProfileData.AGE_RANGE, null);
				extData.putProperty(ProfileData.ID, payload.getSubject());
				extData.putProperty(ProfileData.PICTURE, payload.get("picture"));
				return extData;
			} else {
				throw new InvalidExternalProfileAccessTokenException();
			}
		} catch (RuntimeException e) {
			throw new ExternalProfileIdentityProviderException(e);
		} catch (IOException e) {
			throw new ExternalProfileIdentityProviderException(e);
		} catch (GeneralSecurityException e) {
			throw new ExternalProfileIdentityProviderException(e);
		} 
		
	}

}
