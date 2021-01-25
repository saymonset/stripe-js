package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.EventAsset;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventSettingsServicesTx;

@Component
public class AddEventAssetsMethodImpl implements ServiceMethod {

	@Autowired
	private EventSettingsServicesTx eventSettingsServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationid = aInfo.getApplicationId();
			ArrayList<HashMap<String, Object>> eventAssetsParam = (ArrayList<HashMap<String, Object>>) params.get("eventAssets");
			final List<EventAsset> eventAssets = new ArrayList<>();
			eventAssetsParam.forEach(t -> {
				EventAsset eventAsset = new EventAsset();
				eventAsset.setCustomerId(customerId);
				eventAsset.setApplicationid(applicationid);
				eventAsset.setAssetId(((Number) t.get("assetId")).intValue());
				eventAsset.setAssetValue((String) t.get("assetValue"));
				eventAsset.setLangId(((Number) t.get("langId")).intValue());
				HashMap<String, Object> assetParams = (HashMap<String, Object>) t.get("assetParams");
				if (assetParams != null) {
					eventAsset.setAssetParams(new JSONObject(assetParams).toString());
				}
				eventAssets.add(eventAsset);
			});
			List<EventAsset> eventAssetsUpdatedList = eventSettingsServices.addEventAssets(eventAssets);
			try {
				eventSettingsServices.notifyAssetsChangeToEvent(customerId, applicationid, aInfo.getAccessToken().getToken());
			} catch (Exception e) {
				//Ignore
			}
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			res.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("eventAssets", eventAssetsUpdatedList);
			res.setResult(result);
			return res;
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" e: "+e.getMessage());
			return res;
		}
	}

	
	

}
