package com.us.weavx.core;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;

@Controller
public class CoreServicesTest {
	
	
	@RequestMapping(value = "/api/requesttest", method=RequestMethod.POST)
	@ResponseBody
	public Response proccessRequest(Request req) {
		Response res = new Response();
		res.setReturnCode(0);
		res.setReturnMessage("HOLA MUNDO");
		return res;
	}
	
}
