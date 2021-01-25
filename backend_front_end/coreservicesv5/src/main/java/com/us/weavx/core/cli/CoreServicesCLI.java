package com.us.weavx.core.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.us.weavx.core.cli.command.Command;

public class CoreServicesCLI {
	
	private static HashMap<String, String> implementorClassNames;
	private static HashMap<String, Class> implementors;
	
	public static void main(String...args) {
		implementorClassNames = new HashMap<String, String>();
		implementorClassNames.put("generateDevKeys", "com.us.weavx.core.cli.command.impl.GenerateDeveloperKeysImpl");
		implementorClassNames.put("registerDeveloper", "com.us.weavx.core.cli.command.impl.RegisterDeveloperImpl");
		implementorClassNames.put("registerApp", "com.us.weavx.core.cli.command.impl.RegisterAppImpl");
		implementorClassNames.put("associateApp", "com.us.weavx.core.cli.command.impl.AssociateAppToDeveloperImpl");
		implementorClassNames.put("generateCustomerAccessKeys", "com.us.weavx.core.cli.command.impl.GenerateCustomerAccessKeysImpl");
		implementors = new HashMap<String, Class>();
		boolean executeCommands = true;
		BufferedReader bR = null;
		try {
			bR = new BufferedReader(new InputStreamReader((System.in)));
			while (executeCommands) {
				String commandLine = bR.readLine();
				String[] commandArgs = commandLine.split("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				for (int i = 0; i < commandArgs.length; i++) {
					commandArgs[i] = commandArgs[i].replaceAll("\"","");
				}
				String commandStr = commandArgs[0];
				if (commandStr.equalsIgnoreCase("exit")) {
					executeCommands = false;
					continue;
				}
				Class commandClass = loadClass(commandArgs[0]);
				if (commandClass == null) {
					System.out.println("wrong command name.");
				} else {
					//Load implementor class
					Command command = (Command) commandClass.newInstance();
					//Validate args
					if (command.validate(commandArgs)) {
						//Execute command
						command.execute(commandArgs);
					} else {
						System.out.println(command.help(commandArgs));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR: "+e);
		} finally {
			if (bR != null) {
				try {
					bR.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private static Class loadClass(String commandName) {
		
		try {
			Class c =  implementors.get(commandName);
			if (c == null) {
				String implementorClassName = implementorClassNames.get(commandName);
				if (implementorClassName == null) {
					return null;
				} 
				c = Class.forName(implementorClassName);
				implementors.put(commandName, c);
			} 
			return c;
		} catch (Exception e) {
			return null;
		}
	}
}
