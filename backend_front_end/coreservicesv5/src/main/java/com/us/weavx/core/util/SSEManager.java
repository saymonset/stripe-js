package com.us.weavx.core.util;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SSEManager {

	private static SSEManager instance = null;
	private final String URL_SSE = "https://sse.harvestful.org/SSE/";
	
	public static SSEManager getInstance() {
		if (instance == null) {
			instance = new SSEManager();
		}
		return instance;
	}
	
	private SSEManager() {
		
	}
	
	public boolean closeSSE(String userAccessToken) {		
		try {
			String eventURL = URL_SSE+"logout/"+userAccessToken;
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> resp = restTemplate.getForEntity(eventURL, String.class);
			return (resp.getStatusCode().is2xxSuccessful());
		} catch (Exception e) {
			return false;
		}
	}

}
