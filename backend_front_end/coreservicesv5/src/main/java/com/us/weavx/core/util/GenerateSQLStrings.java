package com.us.weavx.core.util;
import java.lang.reflect.Field;

import com.us.weavx.core.model.DonationSchedule;
import com.us.weavx.core.model.ScheduledDonation;
import com.us.weavx.core.model.ScheduledDonationFrequency;
import com.us.weavx.core.model.ScheduledDonationFrequencyDescription;
import com.us.weavx.core.model.ScheduledDonationSettingsLang;
import com.us.weavx.core.model.ScheduledDonationStatus;

public class GenerateSQLStrings {
	
	public GenerateSQLStrings(Class modelDTO, String tableName) {
		Field[] fields = modelDTO.getDeclaredFields();
		//Print insert statement
		String insertSQL = "insert into "+tableName+" (";
		String selectSQL = "select ";
		int fieldCount = 0;
		for (Field f : fields) {
			insertSQL += (((fieldCount>0)?", ":"")+f.getName());
			selectSQL += (((fieldCount>0)?", ":"")+f.getName());
			fieldCount++;
		}
		insertSQL += ") values (";
		selectSQL += (" from "+tableName);
		for (int i = 0; i < fieldCount; i++) {
			insertSQL += (((i != 0)?",":"")+"?");
		}
		insertSQL += ")";
		System.out.println("INSERT: "+insertSQL);
		System.out.println("SELECT: "+selectSQL);
		
	}
	
	public static void main(String...args) {
		new GenerateSQLStrings(DonationSchedule.class, "donation_scheduler");
	}

}
