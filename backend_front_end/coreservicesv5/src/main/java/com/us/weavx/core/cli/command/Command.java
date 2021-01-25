package com.us.weavx.core.cli.command;

public interface Command {
	
	public String help(String args[]);
	public boolean validate(String args[]);
	public void execute(String args[]);

}
