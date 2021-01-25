package com.us.weavx.core.cli.command.impl;

import org.apache.commons.lang3.RandomStringUtils;

import com.us.weavx.core.cli.command.Command;
import com.us.weavx.core.cli.dao.CLICoreServicesDAO;
import com.us.weavx.core.model.DeveloperKey;

public class GenerateDeveloperKeysImpl implements Command {

	
	public boolean validate(String[] args) {
		return (args.length == 3);
	}
	
	public void execute(String args[]) {
		try {
			CLICoreServicesDAO dao = new CLICoreServicesDAO();
			long developerId = Long.parseLong(args[1]);
			boolean isProduction = Boolean.parseBoolean(args[2]);
			String devKey = null;
			for (int i = 0; i < 10; i++) {
				devKey = RandomStringUtils.random(128, true, true);
				if (!dao.existsDeveloperKey(devKey)) {
					break;
				}
			}
			if (devKey != null) {
				String devSecret = RandomStringUtils.random(128,true,true);
				String devSecretHex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(devSecret);
				DeveloperKey key = new DeveloperKey(devKey,devSecretHex,isProduction,developerId);
				dao.registerNewDeveloperKey(key);
				System.out.println("DEV_KEY:"+devKey);
				System.out.println("DEV_SECRET:"+devSecret);
			} else {
				System.out.println("ERROR: Developer key generation fails. maximum tries exceeded.");
			}
			
			
		} catch (Exception e) {
			System.out.println("ERROR: "+e);
		}
	}

	public String help(String[] args) {
		return "Use: generateDevKeys <developerId> <productionEnabled>";
	}

	

		
}
