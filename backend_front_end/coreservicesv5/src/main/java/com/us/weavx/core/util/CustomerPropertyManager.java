package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.exception.UnknownCustomerPropertyException;
import com.us.weavx.core.model.CustomerProperty;
import com.us.weavx.core.model.CustomerPropertyDesc;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
@Component
public class CustomerPropertyManager {
	@Autowired
	private ConfigurationTxServices confTxServices;

	private HashMap<Long, List<CustomerPropertyDesc>> properties;
	
	public CustomerPropertyManager() {
		this.properties = new HashMap<>();
	}
	
	private CustomerPropertyDesc loadCustomerProperty(long customerId, int langId, String propertyName) throws UnknownCustomerPropertyException {
		CustomerProperty cp = confTxServices.findCustomerProperty(customerId, langId, propertyName);
		if (cp == null) {
			throw new UnknownCustomerPropertyException();
		} else {
			return new CustomerPropertyDesc(customerId,langId,propertyName,cp.getPropertyValue());
		}
	}
	
	
	public String findCustomerProperty(long customerId, int langId, String propertyName) throws UnknownCustomerPropertyException {
		List<CustomerPropertyDesc> customerProperties = properties.get(customerId);
		if (customerProperties == null) {
			customerProperties = new ArrayList<>();
			CustomerPropertyDesc prop = loadCustomerProperty(customerId, langId, propertyName);
			customerProperties.add(prop);
			properties.put(customerId, customerProperties);
			return prop.getPropertyValue();
		} else {
			CustomerPropertyDesc cp = new CustomerPropertyDesc(customerId,langId,propertyName,null);
			int propIndex = customerProperties.indexOf(cp);
			if (propIndex == -1) {
				cp = loadCustomerProperty(customerId, langId, propertyName);
				customerProperties.add(cp);
				properties.put(customerId, customerProperties);
				return cp.getPropertyValue();
			} else {
				return customerProperties.get(propIndex).getPropertyValue();
			}
			
		}
	}

}
