package com.us.weavx.core.cli.command.impl;

import org.apache.commons.lang3.RandomStringUtils;

import com.us.weavx.core.cli.command.Command;
import com.us.weavx.core.cli.dao.CLICoreServicesDAO;
import com.us.weavx.core.data.CoreServicesDAO;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.Developer;
import com.us.weavx.core.model.DeveloperKey;

public class RegisterAppImpl implements Command {

	
	public boolean validate(String[] args) {
		return (args.length == 8);
	}
	
	public void execute(String args[]) {
		try {
			CLICoreServicesDAO dao = new CLICoreServicesDAO();
			String name = args[1];
			String description = args[2];
			Integer shortTokenDuration = Integer.parseInt(args[3]);
			Integer longTokenDuration = Integer.parseInt(args[4]);
			Integer shortUserTokenDuration = Integer.parseInt(args[5]);
			Integer longUserTokenDuration = Integer.parseInt(args[6]);
			Long developerId = Long.parseLong(args[7]);
			Application app = new Application(name,description,shortTokenDuration,longTokenDuration,shortUserTokenDuration,longUserTokenDuration,developerId,null);
			long appId = dao.registerNewApplication(app);
			System.out.println("App registered successfully:Id="+appId);
		} catch (Exception e) {
			System.out.println("ERROR: "+e);
		}
	}

	public String help(String[] args) {
		return "Use: registerApp <name> <description> <shortTokenDuration> <longTokenDuration> <shortUserTokenDuration> <longUserTokenDuration> <developer>";
	}

	

		
}
