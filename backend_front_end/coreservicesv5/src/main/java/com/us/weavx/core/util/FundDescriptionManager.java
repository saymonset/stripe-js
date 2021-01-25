package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.FundDescription;
import com.us.weavx.core.model.FundDescriptionKey;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
@Component
public class FundDescriptionManager {
	@Autowired
	private ConfigurationTxServices configurationServices;
	
	private Map<Long, Map<FundDescriptionKey, FundDescription>> fundDescriptions;
	
	public FundDescriptionManager() {
		fundDescriptions = new HashMap<>();
	}
		
	private void loadFundDescriptions(long customerId) {
		List<FundDescription> customerFunds = configurationServices.getCustomerFundDescriptions(customerId);
		Map<FundDescriptionKey, FundDescription> customerMap = new HashMap<>();
		for (FundDescription item : customerFunds) {
			customerMap.put(new FundDescriptionKey(item.getFundId(),item.getLangId()), item);
		}
		fundDescriptions.put(customerId, customerMap);
	}
	
	public FundDescription getFundDescription(long customerId, FundDescriptionKey key) {
		Map<FundDescriptionKey, FundDescription> customerMap = fundDescriptions.get(customerId);
		if (customerMap == null) {
			loadFundDescriptions(customerId);
		}
		customerMap = fundDescriptions.get(customerId);
		if (customerMap == null) {
			return null;
		}
		FundDescription myFund = customerMap.get(key);
		return myFund;
	}
	
	public List<FundDescription> getAllFundDescriptions(long customerId) {
		Map<FundDescriptionKey, FundDescription> customerMap = fundDescriptions.get(customerId);
		if (customerMap == null) {
			loadFundDescriptions(customerId);
		}
		return new ArrayList<>(fundDescriptions.get(customerId).values());
	}
	
	public void reloadFundDescriptions(long customerId) {
		loadFundDescriptions(customerId);
	}
}
