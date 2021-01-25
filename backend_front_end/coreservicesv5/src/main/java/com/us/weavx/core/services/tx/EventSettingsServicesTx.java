package com.us.weavx.core.services.tx;

import com.us.weavx.core.data.EventSettingsServicesTxDAO;
import com.us.weavx.core.model.Asset;
import com.us.weavx.core.model.EventAsset;
import com.us.weavx.core.model.EventSettings;
import com.us.weavx.core.util.ApplicationParametersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("eventSettingsTxServices")
public class EventSettingsServicesTx {

    @Autowired
    private EventSettingsServicesTxDAO dao;
	
	@Autowired
	private ApplicationParametersManager manager;

	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public EventAsset addEventAsset(final EventAsset item) {
		return dao.addEventAsset(item);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public EventAsset findEventAssetById(int id) {
		return dao.findEventAssetById(id);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<EventAsset> findEventAssetsByEventAndLang(long customerId, long applicationId, int langId) {
		return dao.findEventAssetsByEventAndLang(customerId, applicationId, langId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<EventAsset> findEventAssetsByEvent(long customerId, long applicationId) {
		return dao.findEventAssetsByEvent(customerId, applicationId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<Asset> listAllAssets() {
		return dao.listAllAssets();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public EventAsset updateEventAsset(EventAsset item) {
		return dao.updateEventAsset(item);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<EventAsset> addEventAssets(List<EventAsset> list) {
		final List<EventAsset> result = new ArrayList<>();
		list.forEach(t ->  result.add(dao.addEventAsset(t)));
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<EventAsset> updateEventAssets(List<EventAsset> list) {
		final List<EventAsset> result = new ArrayList<>();
		list.forEach(t -> 
			result.add(dao.updateEventAsset(t)));
		return result;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public boolean 	notifyAssetsChangeToEvent(long customerId, long applicationId, String accessToken) {
			RestTemplate restTemplate = new RestTemplate();
			String eventURL = manager.getApplicationParameter(applicationId, "PAYWALL_URL");
			eventURL += ("/sendData/"+accessToken);
			ResponseEntity<String> resp = restTemplate.getForEntity(eventURL, String.class);
			return (resp.getStatusCode().is2xxSuccessful());
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<EventSettings> listAllEventSettingsByName(String name) {
		return dao.listAllEventByName(name);
	}
}

