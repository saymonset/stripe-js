package com.us.weavx.core.cli.command.impl;

import org.apache.commons.lang3.RandomStringUtils;

import com.us.weavx.core.cli.command.Command;
import com.us.weavx.core.cli.dao.CLICoreServicesDAO;
import com.us.weavx.core.model.AccessKey;
import com.us.weavx.core.model.DeveloperKey;

public class GenerateCustomerAccessKeysImpl implements Command {

	
	public boolean validate(String[] args) {
		return (args.length >= 3 && args.length < 5);
	}
	
	public void execute(String args[]) {
		try {
			CLICoreServicesDAO dao = new CLICoreServicesDAO();
			long customerId = Long.parseLong(args[1]);
			long appId = Long.parseLong(args[2]);
			boolean isAdmin = false;
			if (args.length == 4)
				isAdmin = Boolean.parseBoolean(args[3]);
			String accessKey = null;
			for (int i = 0; i < 10; i++) {
				accessKey = RandomStringUtils.random(128, true, true);
				if (!dao.existsAccessKey(accessKey)) {
					break;
				}
			}
			if (accessKey != null) {
				String accessSecret = RandomStringUtils.random(128,true,true);
				String accessSecretHex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(accessSecret);
				AccessKey key = new AccessKey(accessKey,accessSecretHex,appId,isAdmin,customerId);
				dao.registerNewAccessKey(key);
				System.out.println("ACCESS_KEY:"+accessKey);
				System.out.println("ACCESS_SECRET:"+accessSecret);
			} else {
				System.out.println("ERROR: Access key generation fails. maximum tries exceeded.");
			}
		} catch (Exception e) {
			System.out.println("ERROR: "+e);
		}
	}

	public String help(String[] args) {
		return "Use: generateCustomerAccessKeys <customerId> <applicationId> [isAdmin]";
	}

	

		
}
