package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.exception.UnknownCustomerMediumException;
import com.us.weavx.core.model.TransactionMedium;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
@Component
public class TransactionMediumManager {
	@Autowired
	private ConfigurationTxServices confTxServices;
	
	private HashMap<Long, List<TransactionMedium>> properties;
	
	public TransactionMediumManager() {
		this.properties = new HashMap<>();
	}
		
	private List<TransactionMedium> loadCustomerMediums(long customerId)  {
		List<TransactionMedium> customerMediums = confTxServices.listCustomerMediums(customerId);
		this.properties.put(customerId, customerMediums);
		return customerMediums;
	}
	
	
	public TransactionMedium findCustomerMedium(long customerId, String key) throws UnknownCustomerMediumException {
		List<TransactionMedium> customerMediums = properties.get(customerId);
		if (customerMediums == null) {
			try {
				customerMediums = loadCustomerMediums(customerId);
			} catch (Exception e) {
				//Ignore
			}
		}
		if (customerMediums != null) {
			TransactionMedium tMedium = new TransactionMedium();
			tMedium.setCustomer_id(customerId);
			tMedium.setKey(key);
			int sourceIndex = customerMediums.indexOf(tMedium);
			if (sourceIndex != -1) {
				return customerMediums.get(sourceIndex);
			} else {
				try {
					customerMediums = loadCustomerMediums(customerId);
				} catch (Exception e) {
					//Ignore
				}
				sourceIndex = customerMediums.indexOf(tMedium);
				if (sourceIndex != -1) {
					return customerMediums.get(sourceIndex);
				} else {
					throw new UnknownCustomerMediumException();
				}
			}
		} else {
			throw new RuntimeException("Error getting customer mediums for customer: "+customerId);
		}
	}
	
	public synchronized TransactionMedium addCustomerMedium(TransactionMedium tMedium) {
		tMedium = confTxServices.addTransactionMedium(tMedium);
		List<TransactionMedium> customerMediums = this.properties.get(tMedium.getCustomer_id());
		if (customerMediums == null) {
			customerMediums = new ArrayList<>();
		}
		customerMediums.add(tMedium);
		this.properties.put(tMedium.getCustomer_id(), customerMediums);
		return tMedium;
	}

}
