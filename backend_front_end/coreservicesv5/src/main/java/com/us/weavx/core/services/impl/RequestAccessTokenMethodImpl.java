package com.us.weavx.core.services.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.UnauthorizedAccessException;
import com.us.weavx.core.model.AccessKey;
import com.us.weavx.core.model.AccessToken;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.util.TokenGeneratorUtil;
@Component
public class RequestAccessTokenMethodImpl implements ServiceMethod {
	
	@Autowired
	private ConfigurationTxServices configurationServices;

	public Response executeMethod(Request request) {
		try {
			HashMap<String, Object> params = request.getParameters();
			//Se obtiene la info de AccessKey y de DeveloperKey
			String accessKey = null;
			String accessSecret = null;
			String devKey = null;
			String devSecret = null;
			HashMap<String, Object> tmp = (HashMap<String, Object>) params.get("accessKey");
			accessKey = (String) tmp.get("key");
			accessSecret = (String) tmp.get("secret");
			accessSecret = org.apache.commons.codec.digest.DigestUtils.sha256Hex(accessSecret);
			tmp = (HashMap<String, Object>) params.get("developerKey");
			devKey = (String) tmp.get("key");
			devSecret = (String) tmp.get("secret");
			devSecret = org.apache.commons.codec.digest.DigestUtils.sha256Hex(devSecret);
			//Se valida ahora que el developer tenga acceso a la app para ello se verifica si el developer_id tiene permiso sobre la aplicacion asociada al access_key
			Application app;
			AccessKey aKey = null;
			try {
				HashMap<String, Object> appDeveloperCustomerInfo = configurationServices.findDeveloperAppCustomerInfo(accessKey, accessSecret, devKey, devSecret);
				app = (Application) appDeveloperCustomerInfo.get("application");
				aKey = (AccessKey) appDeveloperCustomerInfo.get("accessKey");
			} catch (UnauthorizedAccessException e) {
				//Acceso denegado
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.ACCESS_DENIED_CODE);
				res.setReturnMessage(ServiceReturnMessages.ACCESS_DENIED_MESSAGE);
				return res;
			}
			//Se genera un nuevo token
			AccessToken token = null;
			for (int i = 0; i < 10; i++) {
				String accessToken = TokenGeneratorUtil.generateToken();
				token = new AccessToken(accessToken,aKey.getId());
				//Se debe registar el nuevo accessToken
				if (configurationServices.registerNewAccessToken(token, app.getShortTokenDuration()) == 0) {
					token = null;
				} else {
					break;
				}
			}
			if (token != null) {
				//Se retorna el nuevo token
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
				res.setReturnMessage(ServiceReturnMessages.SUCCESS);
				HashMap<String, Object> result = new HashMap<>();
				result.put("accessToken", token.getToken());
				res.setResult(result);
				return res;
			} else {
				//No fue posible generarlo
				Response res = new Response();
				res.setReturnCode(ServiceReturnMessages.ERROR_GENERATING_TOKEN_CODE);
				res.setReturnMessage(ServiceReturnMessages.ERROR_GENERATING_CODE);
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR);
			return res;
		}
	}

}