package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.Terminal;

public interface Command {
	void execute(String command, Terminal terminal);

	String getCommand();
	String getHelp();
}
