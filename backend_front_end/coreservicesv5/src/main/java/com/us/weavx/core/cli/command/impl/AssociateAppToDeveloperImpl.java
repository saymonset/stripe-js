package com.us.weavx.core.cli.command.impl;

import org.apache.commons.lang3.RandomStringUtils;

import com.us.weavx.core.cli.command.Command;
import com.us.weavx.core.cli.dao.CLICoreServicesDAO;
import com.us.weavx.core.data.CoreServicesDAO;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.Developer;
import com.us.weavx.core.model.DeveloperKey;

public class AssociateAppToDeveloperImpl implements Command {

	
	public boolean validate(String[] args) {
		return (args.length == 3);
	}
	
	public void execute(String args[]) {
		try {
			CLICoreServicesDAO dao = new CLICoreServicesDAO();
			Long appId = Long.parseLong(args[1]);
			Long developerId = Long.parseLong(args[2]);
			dao.associateApplication(appId, developerId);
			System.out.println("OK");
		} catch (Exception e) {
			System.out.println("ERROR: "+e);
		}
	}

	public String help(String[] args) {
		return "Use: associateApp <appId> <developerId>";
	}

	

		
}
