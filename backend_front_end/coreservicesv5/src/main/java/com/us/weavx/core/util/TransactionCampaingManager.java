package com.us.weavx.core.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.TransactionCampaing;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
@Component
public class TransactionCampaingManager {
	@Autowired
	private ConfigurationTxServices confTxServices;
	
	private HashMap<Long, List<TransactionCampaing>> properties;
	
	public TransactionCampaingManager() {
		this.properties = new HashMap<>();
	}
	
	
	private List<TransactionCampaing> loadCustomerCampaings(long customerId)  {
		List<TransactionCampaing> customerCampaings = confTxServices.listCustomerTransactionCampaings(customerId);
		this.properties.put(customerId, customerCampaings);
		return customerCampaings;
	}
	
	
	public TransactionCampaing findCustomerCampaing(long customerId, String key) throws UnknownCustomerCampaingException {
		List<TransactionCampaing> customerCampaings = properties.get(customerId);
		if (customerCampaings == null) {
			try {
				customerCampaings = loadCustomerCampaings(customerId);
			} catch (Exception e) {
				//Ignore
			}
		}
		if (customerCampaings != null) {
			TransactionCampaing tCampaing = new TransactionCampaing();
			tCampaing.setCustomerId(customerId);
			tCampaing.setKey(key);
			int sourceIndex = customerCampaings.indexOf(tCampaing);
			if (sourceIndex != -1) {
				return customerCampaings.get(sourceIndex);
			} else {
				try {
					customerCampaings = loadCustomerCampaings(customerId);
				} catch (Exception e) {
					//Ignore
				}
				sourceIndex = customerCampaings.indexOf(tCampaing);
				if (sourceIndex != -1) {
					return customerCampaings.get(sourceIndex);
				} else {
					throw new UnknownCustomerCampaingException();
				}
			}
		} else {
			throw new RuntimeException("Error getting customer mediums for customer: "+customerId);
		}
	}
	
	public synchronized TransactionCampaing addCustomerCampaing(TransactionCampaing tCampaing) {
		Calendar cal = Calendar.getInstance();
		if (tCampaing.getValidFrom() == null) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.YEAR, 2017);
			tCampaing.setValidFrom(new Timestamp(cal.getTimeInMillis()));	
		}
		if (tCampaing.getValidTo() == null) {
			cal.set(Calendar.DAY_OF_MONTH, 21);
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			cal.set(Calendar.YEAR, 3000);
			tCampaing.setValidTo(new Timestamp(cal.getTimeInMillis()));
		}
		tCampaing = confTxServices.addTransactionCampaing(tCampaing);
		List<TransactionCampaing> customerCampaings = this.properties.get(tCampaing.getCustomerId());
		if (customerCampaings == null) {
			customerCampaings = new ArrayList<>();
		}
		customerCampaings.add(tCampaing);
		this.properties.put(tCampaing.getCustomerId(), customerCampaings);
		return tCampaing;
	}

}
