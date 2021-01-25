package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.DonationFrequencyDescriptionKey;
import com.us.weavx.core.model.ScheduledDonationFrequencyDescription;
import com.us.weavx.core.services.tx.DonationTxServices;
@Component
public class DonationFrequencyDescriptionManager {
	@Autowired
	private DonationTxServices donationServices;

	private HashMap<DonationFrequencyDescriptionKey, ScheduledDonationFrequencyDescription> frequencies;
	
	
	public DonationFrequencyDescriptionManager() {
		frequencies = new HashMap<>();
	}
		
	private void loadDonationFrequencies() {
		List<ScheduledDonationFrequencyDescription> donFreqs = donationServices.listAllScheduledDonationFrequencyDescriptions();
		for (ScheduledDonationFrequencyDescription dF : donFreqs) {
			frequencies.put(new DonationFrequencyDescriptionKey(dF.getDonation_frequency_id(), dF.getLang_id()), dF);
		}
	}
	
	public ScheduledDonationFrequencyDescription getScheduledDonationFrequency(DonationFrequencyDescriptionKey key) {
		ScheduledDonationFrequencyDescription result = frequencies.get(key);
		if (result == null) {
			loadDonationFrequencies();
		}
		return frequencies.get(key);
	}
	
	public List<ScheduledDonationFrequencyDescription> getAllDonationFrequencyDescriptions() {
		return new ArrayList<ScheduledDonationFrequencyDescription>(frequencies.values());
	}
	
	public void reoladDonationFrequencyDescriptions(DonationFrequencyDescriptionKey key) {
		loadDonationFrequencies();
	}
}
