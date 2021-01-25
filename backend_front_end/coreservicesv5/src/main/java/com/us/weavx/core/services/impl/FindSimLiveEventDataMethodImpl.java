package com.us.weavx.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Asset;
import com.us.weavx.core.model.EventAsset;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventSettingsServicesTx;
import com.us.weavx.core.util.GeneralUtilities;
@Component
public class FindSimLiveEventDataMethodImpl implements ServiceMethod {
	@Autowired
	private EventSettingsServicesTx eventSettingServices;
	
	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			List<EventAsset> assets = eventSettingServices.findEventAssetsByEvent(aInfo.getCustomerId(), aInfo.getApplicationId());
			JSONObject playerParams = null;
			if(assets != null && !assets.isEmpty()) {
				Optional<EventAsset> asset = assets.stream().filter(t -> t.getAssetId()==Asset.URL_PLAYER).findFirst();
				if (asset.isPresent()) {
					EventAsset playerAsset = asset.get();
					if (playerAsset.getAssetParams() != null) {
						playerParams = new JSONObject(playerAsset.getAssetParams());
						playerParams = GeneralUtilities.findOffset(playerParams);
					}
				}
			}
			HashMap<String, Object> result = new HashMap<>();
			Response resp = new Response();
			if (playerParams != null) {
				result.put("sim-live-event", true);
				result.put("sim-live-event-data", playerParams.toMap());
			} else {
				result.put("sim-live-event", false);
			}
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			resp.setResult(result);
			return resp;
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());
			return resp;
		}
	}
}
