package com.us.weavx.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.model.ScheduledDonationSettingsLang;
import com.us.weavx.core.model.ScheduledDonationSettingsLangKey;
import com.us.weavx.core.services.tx.DonationTxServices;

@Component
public class ScheduledDonationSettingsManager {
	@Autowired
	private DonationTxServices donationServices;

	private Map<ScheduledDonationSettingsLangKey, ScheduledDonationSettingsLang> donationSettings;
	
	
	public ScheduledDonationSettingsManager() {
		donationSettings =  new HashMap<>();
	}
	
	
	private void loadDonationSettings() {
		List<ScheduledDonationSettingsLang> settings = donationServices.listAllScheduledDonationSettingsLang();
		for (ScheduledDonationSettingsLang item : settings) {
			ScheduledDonationSettingsLangKey keyTmp = new ScheduledDonationSettingsLangKey(item.getCustomer_id(),item.getApplication_id(),item.getLang_id());
			donationSettings.put(keyTmp, item);
		}
	}
	
	public ScheduledDonationSettingsLang getScheduledDonationSettingsLang(ScheduledDonationSettingsLangKey key) {
		ScheduledDonationSettingsLang result = donationSettings.get(key);
		if (result == null) {
			loadDonationSettings();
		}
		return donationSettings.get(key);
	}
	
	public List<ScheduledDonationSettingsLang> getAlScheduledlDonationSettingsLang() {
		return new ArrayList<ScheduledDonationSettingsLang>(donationSettings.values());
	}
	
	public void reoladDonationSettings() {
		loadDonationSettings();
	}
}
