package com.us.weavx.core.util;

import java.util.HashMap;

import com.us.weavx.core.exception.CoreServicesClassLoadingException;

public class CoreServicesClassLoader {
	
	public static final int METHOD = 1;
	public static final int IDENTITY_PROVIDER = 2;
	public static final int PAYMENT_GATEWAY = 3;
	public static final int APPLICATION_CONTROLLER = 4;
	public static final int DONATION_FREQUENCY_HANDLER = 5;
	
	private HashMap<String, Class> methodClasses;
	private HashMap<String, Class> identityProviderClasses;
	private HashMap<String, Class> paymentGwClasses;
	private HashMap<String, Class> applicationControllerClasses;
	private HashMap<String, Class> donationFrequencyControllerClasses;
	
	private static CoreServicesClassLoader instance = null;
	
	private CoreServicesClassLoader() {
		methodClasses = new HashMap<>();
		identityProviderClasses = new HashMap<>();
		paymentGwClasses = new HashMap<>();
		applicationControllerClasses = new HashMap<>();
		donationFrequencyControllerClasses = new HashMap<>();
	}
	
	public static CoreServicesClassLoader getInstance() {
		if (instance == null)  {
			instance = new CoreServicesClassLoader();
		} 
		return instance;
	}
	
	public static CoreServicesClassLoader getFreshInstance() {
		instance = new CoreServicesClassLoader(); 
		return instance;
	}
	
	public Class loadClass(String name, int classType) throws CoreServicesClassLoadingException {
		try {
			Class c = null;
			switch (classType) {
			case METHOD:
				c = methodClasses.get(name);
				if (c == null) {
					c = Class.forName(name);
					methodClasses.put(name, c);
				}
				break;
			case IDENTITY_PROVIDER:
				c = identityProviderClasses.get(name);
				if (c == null) {
					c = Class.forName(name);
					identityProviderClasses.put(name, c);
				}
				break;	
			case PAYMENT_GATEWAY:
				c = paymentGwClasses.get(name);
				if (c == null) {
					c = Class.forName(name);
					paymentGwClasses.put(name, c);
				}
				break;
			case APPLICATION_CONTROLLER:
				c = applicationControllerClasses.get(name);
				if (c == null) {
					c = Class.forName(name);
					applicationControllerClasses.put(name, c);
				}				
				break;
			case DONATION_FREQUENCY_HANDLER:
				c = donationFrequencyControllerClasses.get(name);
				if (c == null) {
					c = Class.forName(name);
					donationFrequencyControllerClasses.put(name, c);
				}				
				break;				
			default:
				throw new CoreServicesClassLoadingException("Unsupported class type: "+classType);
			}
			return c;
		} catch (Exception e) {
			throw new CoreServicesClassLoadingException(e);
		}
	}
}
