package com.us.weavx.core.services;

import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;

public interface ServiceMethod {
	
	public Response executeMethod(Request request);

}
