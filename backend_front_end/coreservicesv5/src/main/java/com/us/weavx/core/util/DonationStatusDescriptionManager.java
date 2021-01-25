package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.DonationStatusDescriptionKey;
import com.us.weavx.core.model.ScheduledDonationStatusDescription;
import com.us.weavx.core.services.tx.DonationTxServices;
@Component
public class DonationStatusDescriptionManager {
	@Autowired
	private DonationTxServices donationServices;
	
	private HashMap<DonationStatusDescriptionKey, ScheduledDonationStatusDescription> descriptions;
	
	public DonationStatusDescriptionManager() {
		descriptions = new HashMap<>();
	}
		
	private void loadDonationStatusDescriptions() {
		List<ScheduledDonationStatusDescription> donDescs = donationServices.listAllScheduledDonationStatusDescriptions();
		for (ScheduledDonationStatusDescription dD : donDescs) {
			descriptions.put(new DonationStatusDescriptionKey(dD.getDonation_status_id(), dD.getLang_id()), dD);
		}
	}
	
	public ScheduledDonationStatusDescription getScheduledDonationStatusDescription(DonationStatusDescriptionKey key) {
		ScheduledDonationStatusDescription result = descriptions.get(key);
		if (result == null) {
			loadDonationStatusDescriptions();
		}
		return descriptions.get(key);
	}
	
	public List<ScheduledDonationStatusDescription> getAllDonationStatusDescriptions() {
		return new ArrayList<>(descriptions.values());
	}
	
	public void reoladDonationStatusDescriptions() {
		loadDonationStatusDescriptions();
	}
}
