package com.us.weavx.core.util;

import com.us.weavx.core.model.ScheduledDonationFrequency;
import com.us.weavx.core.services.tx.DonationTxServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class DonationFrequencyManager {
	@Autowired
	private DonationTxServices donationServices;

	private HashMap<Integer, ScheduledDonationFrequency> frequencies;

	@PostConstruct
	public void init() {
		frequencies = new HashMap<>();
		loadDonationFrequencies();
	}

	private void loadDonationFrequencies() {
		List<ScheduledDonationFrequency> donFreqs = donationServices.listAllScheduledDonationFrequencies();
		for (ScheduledDonationFrequency dF : donFreqs) {
			frequencies.put(dF.getId(), dF);
		}
	}

	public ScheduledDonationFrequency getScheduledDonationFrequency(int donationFrequencyId) {
		return frequencies.get(donationFrequencyId);
	}

	public List<ScheduledDonationFrequency> getAllDonationfrequencies() {
		return new ArrayList<ScheduledDonationFrequency>(frequencies.values());
	}

	public void reoladDonationFrequencies(int donationFrequencyId) {
		loadDonationFrequencies();
	}
}
