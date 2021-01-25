package com.us.weavx.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.ApplicationParameter;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
@Component
public class ApplicationParametersManager {
	@Autowired
	private ConfigurationTxServices confServices;
	
	private HashMap<Long, HashMap<String, String>> settings;
	
	public ApplicationParametersManager() {
		settings = new HashMap<>();
	}
	
	private HashMap<String, String> loadApplicationParameters(long applicationId) {
		List<ApplicationParameter> parameters = confServices.findAllApplicationParameters(applicationId);
		HashMap<String, String> result = new HashMap<>();
		for (ApplicationParameter param: parameters) {
			result.put(param.getName(), param.getValue());
		}
		return result;
	}
	
	public String getApplicationParameter(long applicationId, String parameter) {
		if (settings.get(applicationId) == null) {
			//Cargar los parametros de la aplicacion
			settings.put(applicationId, loadApplicationParameters(applicationId));
		}
		return settings.get(applicationId).get(parameter);
	}
	
	public Map<String, String> getAllApplicationParameters(long applicationId) {
		if (settings.get(applicationId)==null) {
			settings.put(applicationId, loadApplicationParameters(applicationId));
		}
		return settings.get(applicationId);
	}
	
	public void reoladApplicationParameters(long applicationId) {
		settings.put(applicationId, loadApplicationParameters(applicationId));
	}
}
