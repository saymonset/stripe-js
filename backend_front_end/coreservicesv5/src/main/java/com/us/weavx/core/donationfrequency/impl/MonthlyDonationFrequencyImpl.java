package com.us.weavx.core.donationfrequency.impl;

import java.sql.Timestamp;
import java.util.Calendar;

import com.us.weavx.core.donationfrequency.DonationFrequencyHandler;
import com.us.weavx.core.exception.DonationFrequencyHandlerGeneralException;
import com.us.weavx.core.model.ScheduledDonation;

public class MonthlyDonationFrequencyImpl implements DonationFrequencyHandler {

	public MonthlyDonationFrequencyImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long findNextDonationDateMillis(ScheduledDonation donation) throws DonationFrequencyHandlerGeneralException {
		Timestamp startsAt = donation.getStarts_at();
		Calendar startsAtCal = Calendar.getInstance();
		startsAtCal.setTimeInMillis(startsAt.getTime());
		int dayOfDonation = startsAtCal.get(Calendar.DAY_OF_MONTH);
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE,0);
		currentDate.set(Calendar.SECOND,0);
		currentDate.set(Calendar.MILLISECOND,0);
		if (currentDate.before(startsAtCal)) {
			return startsAtCal.getTimeInMillis();
		} else {
			int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
			int actualMaximumDay = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
			int dayOfEffectiveDonation = -1;
			if (currentDay < dayOfDonation) {
				dayOfEffectiveDonation = (dayOfDonation<actualMaximumDay)?dayOfDonation:actualMaximumDay;
				currentDate.set(Calendar.DAY_OF_MONTH, dayOfDonation);
				return currentDate.getTimeInMillis();
			} else {
				currentDate.add(Calendar.MONTH, 1);
				actualMaximumDay = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
				dayOfEffectiveDonation = (dayOfDonation<actualMaximumDay)?dayOfDonation:actualMaximumDay;
				currentDate.set(Calendar.DAY_OF_MONTH, dayOfEffectiveDonation);
				return currentDate.getTimeInMillis();
			}
		}
	}

}
