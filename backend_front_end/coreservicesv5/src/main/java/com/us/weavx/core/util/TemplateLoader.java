package com.us.weavx.core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.us.weavx.core.constants.SystemConstants;
import com.us.weavx.core.exception.UnknownNotificationTemplateException;

public class TemplateLoader {
	
	private static TemplateLoader instance;
	private HashMap<String, String> templates;
	
	private TemplateLoader() {
		this.templates = new HashMap<>();
	}
	
	public static TemplateLoader getInstance() {
		if (instance == null) {
			instance = new TemplateLoader();
		}
		return instance;
	}
	
	public String loadTemplate(String templateUrl) throws UnknownNotificationTemplateException {
		BufferedReader bR = null;
    	StringBuilder content = new StringBuilder();
    	String templateContent = templates.get(templateUrl);
    	if (templateContent == null) {
	    	try {
	    		bR = new BufferedReader(new InputStreamReader(new FileInputStream(SystemConstants.PATH_PREFIX+templateUrl)));
	    		String linea = null;
	    		while ((linea = bR.readLine()) != null) {
	    			content.append(linea).append("\n");
	    		}
	    		templateContent = content.toString();
	    		templates.put(templateUrl, templateContent);
	    		return templateContent;
	    	} catch (Exception e) {
	    		throw new UnknownNotificationTemplateException(e);
	    	} finally {
	    		if (bR != null) {
	    			try {
						bR.close();
					} catch (IOException e) {
						//Ignore
					}
	    		}
	    	}
    	}
    	return templateContent;
	}

}
