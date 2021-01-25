package com.us.weavx.core.cli.command.impl;

import org.apache.commons.lang3.RandomStringUtils;

import com.us.weavx.core.cli.command.Command;
import com.us.weavx.core.cli.dao.CLICoreServicesDAO;
import com.us.weavx.core.data.CoreServicesDAO;
import com.us.weavx.core.model.Developer;
import com.us.weavx.core.model.DeveloperKey;

public class RegisterDeveloperImpl implements Command {

	
	public boolean validate(String[] args) {
		return (args.length == 5);
	}
	
	public void execute(String args[]) {
		try {
			CLICoreServicesDAO dao = new CLICoreServicesDAO();
			String name = args[1];
			String email = args[2];
			String password = args[3];	
			String confirmation = args[4];
			if (!password.equals(confirmation)) {
				System.out.println("Passwords does not match.");
			} else {
				String passwordHex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
				Developer dev = new Developer(name, email, passwordHex);
				long newId = dao.registerNewDeveloper(dev);
				System.out.println("Developer registered successfully:ID="+newId);
			}
		} catch (Exception e) {
			System.out.println("ERROR: "+e);
		}
	}

	public String help(String[] args) {
		return "Use: registerDeveloper <name> <email> <password> <confirmation>";
	}

	

		
}
