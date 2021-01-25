package com.us.weavx.core;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.constants.SystemConstants;
import com.us.weavx.core.constants.TokenStatus;
import com.us.weavx.core.exception.ObjectDoesNotExistsException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AccessToken;
import com.us.weavx.core.model.Method;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.util.CoreServicesClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

@Controller
@RequestMapping("/rest")
@CrossOrigin(origins = "*")
public class CoreServices {
	
	@Autowired 
	private ApplicationContext context;
	@Autowired
	private ConfigurationTxServices configurationServices;

	
	@RequestMapping(value = "/api/request", method=RequestMethod.POST)
	@ResponseBody
	public Response proccessRequest(@RequestBody Request req) {
		
		//Lo primero es validar el accessToken salvo para el metodo getAccessToken
		try {
 			String method = req.getMethodName();
			if (!method.equals(SystemConstants.REQUEST_ACCESS_TOKEN_METHOD_NAME) && !method.equals(SystemConstants.FIND_EVENT_SETTINGS_BYNAME)) {
				//Validar aplicacion y access token
				AccessInfo aInfo = null;
				try {
					aInfo = configurationServices.getAccessInfo(req.getAccessToken());
				} catch (ObjectDoesNotExistsException e) {
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.INVALID_ACCESS_TOKEN_CODE);
					res.setReturnMessage(ServiceReturnMessages.INVALID_ACCESS_TOKEN);
					return res;
				}
				AccessToken aToken = aInfo.getAccessToken();
				HashMap<String, Object> params = req.getParameters();
				params = (params == null)?new HashMap<>():params;
				params.put("accessInfo", aInfo);
				req.setParameters(params);
				if (aToken.getStatus() == TokenStatus.ACTIVE) {
					if (aToken.getExpirationDate().before(Timestamp.valueOf(LocalDateTime.now()))) {
						//Token expirado
						aToken.setStatus(TokenStatus.EXPIRED);
						configurationServices.changeAccessTokenStatus(aToken);
						Response res = new Response();
						res.setReturnCode(ServiceReturnMessages.EXPIRED_TOKEN_CODE);
						res.setReturnMessage(ServiceReturnMessages.EXPIRED_TOKEN);
						return res;
					}
				} else if (aToken.getStatus() == TokenStatus.EXPIRED) {
					//El token ha expirado
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.EXPIRED_TOKEN_CODE);
					res.setReturnMessage(ServiceReturnMessages.EXPIRED_TOKEN);
					return res;					
				} else {
					//El token se encuentra bloqueado
					Response res = new Response();
					res.setReturnCode(ServiceReturnMessages.BLOCKED_TOKEN_CODE);
					res.setReturnMessage(ServiceReturnMessages.BLOCKED_TOKEN);
					return res;
				}
			}
			//Se obtiene la informacion del metodo a ejecutar
			Method methodInfo = configurationServices.getMethodInfoByName(method);
			//Se carga la clase implementadora
			CoreServicesClassLoader loader = CoreServicesClassLoader.getInstance();
			Class c = loader.loadClass(methodInfo.getImplementorClass(), CoreServicesClassLoader.METHOD);
			ServiceMethod sM = (ServiceMethod) context.getBean(c);
			return sM.executeMethod(req);
		} catch (Exception e) {
			Response res = new Response();
			res.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			res.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" - "+e.getMessage());
			return res;
		}
		
	}
	
}
