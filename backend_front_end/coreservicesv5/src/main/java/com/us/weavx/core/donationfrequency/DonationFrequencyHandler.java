package com.us.weavx.core.donationfrequency;

import com.us.weavx.core.exception.DonationFrequencyHandlerGeneralException;
import com.us.weavx.core.model.ScheduledDonation;

public interface DonationFrequencyHandler {
	
	public long findNextDonationDateMillis(ScheduledDonation donation) throws DonationFrequencyHandlerGeneralException;

}
