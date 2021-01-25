package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.exception.UnknownCustomerSourceException;
import com.us.weavx.core.model.TransactionSource;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
@Component
public class TransactionSourceManager {
	@Autowired
	private ConfigurationTxServices confTxServices;

	private HashMap<Long, List<TransactionSource>> properties;
	
	public TransactionSourceManager() {
		this.properties = new HashMap<>();
	}
		
	private List<TransactionSource> loadCustomerSources(long customerId)  {
		List<TransactionSource> customerSources = confTxServices.listCustomerTransactionSources(customerId);
		this.properties.put(customerId, customerSources);
		return customerSources;
	}
	
	
	public TransactionSource findCustomerSource(long customerId, String key) throws UnknownCustomerSourceException {
		List<TransactionSource> customerSources = properties.get(customerId);
		if (customerSources == null) {
			try {
				customerSources = loadCustomerSources(customerId);
			} catch (Exception e) {
				//Ignore
			}
		}
		if (customerSources != null) {
			TransactionSource tSource = new TransactionSource();
			tSource.setCustomerId(customerId);
			tSource.setKey(key);
			int sourceIndex = customerSources.indexOf(tSource);
			if (sourceIndex != -1) {
				return customerSources.get(sourceIndex);
			} else {
				try {
					customerSources = loadCustomerSources(customerId);
				} catch (Exception e) {
					//Ignore
				}
				sourceIndex = customerSources.indexOf(tSource);
				if (sourceIndex != -1) {
					return customerSources.get(sourceIndex);
				} else {
					throw new UnknownCustomerSourceException();
				}
			}
		} else {
			throw new RuntimeException("Error getting customer sources for customer: "+customerId);
		}
	}
	
	public synchronized TransactionSource  addCustomerSource(TransactionSource tSource) {
		tSource = confTxServices.addTransactionSource(tSource);
		List<TransactionSource> customerSources = this.properties.get(tSource.getCustomerId());
		if (customerSources == null) {
			customerSources = new ArrayList<>();
		}
		customerSources.add(tSource);
		this.properties.put(tSource.getCustomerId(), customerSources);
		return tSource;
	}

}
