package com.us.weavx.core.donationfrequency.impl;

import java.sql.Timestamp;
import java.util.Calendar;

import com.us.weavx.core.donationfrequency.DonationFrequencyHandler;
import com.us.weavx.core.exception.DonationFrequencyHandlerGeneralException;
import com.us.weavx.core.model.ScheduledDonation;

public class Day1And15DonationFrequencyImpl implements DonationFrequencyHandler {

	public Day1And15DonationFrequencyImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long findNextDonationDateMillis(ScheduledDonation donation) throws DonationFrequencyHandlerGeneralException {
		Timestamp startsAt = donation.getStarts_at();
		Calendar startsAtCal = Calendar.getInstance();
		startsAtCal.setTimeInMillis(startsAt.getTime());
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE,0);
		currentDate.set(Calendar.SECOND,0);
		currentDate.set(Calendar.MILLISECOND,0);
		Calendar nextDonationDate = (currentDate.before(startsAtCal))?startsAtCal:currentDate;
		int currentDay = nextDonationDate.get(Calendar.DAY_OF_MONTH);
		if (currentDay > 15) {
			nextDonationDate.add(Calendar.MONTH, 1);
			nextDonationDate.set(Calendar.DAY_OF_MONTH, 1);
		} else {
			nextDonationDate.set(Calendar.DAY_OF_MONTH, 15);
		}
		return nextDonationDate.getTimeInMillis();
	}

}
